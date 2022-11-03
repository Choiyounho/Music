package com.pdf.music.bookmark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.pdf.music.DetailActivity
import com.pdf.music.R
import com.pdf.music.databinding.ActivityBookmarkBinding
import com.pdf.music.db.AppDatabase
import com.pdf.music.db.BookmarkEntity

class BookmarkActivity : AppCompatActivity() {

    private val binding by lazy { ActivityBookmarkBinding.inflate(layoutInflater) }

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)

        val path = intent.extras?.getString("KEY") ?: ""

        val adapter = BookmarkAdapter {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("KEY", it.path)
            intent.putExtra("PAGE", it.page - 1)
            startActivity(intent)
        }

        binding.bookmarkRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookmarkRecyclerView.adapter = adapter

        Thread {
            val list = db.musicDao().bookmarks()
            runOnUiThread {
                adapter.setItems(list)
            }
        }.start()


        binding.add.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_bookmark, null)

            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            dialog.show()

            val pageEdit = dialog.findViewById<EditText>(R.id.pageEdit) ?: return@setOnClickListener
            val nameEdit = dialog.findViewById<EditText>(R.id.nameEdit) ?: return@setOnClickListener
            dialog.findViewById<Button>(R.id.button)?.setOnClickListener {
                if (path != "") {
                    Thread {
                        db.musicDao().insertBookmark(
                            BookmarkEntity(
                                pageEdit.text.toString().toInt(),
                                nameEdit.text.toString(),
                                path
                            )
                        )

                        val list = db.musicDao().bookmarks()

                        runOnUiThread {
                            adapter.setItems(list)
                        }
                    }.start()
                }

                dialog.dismiss()
            }
        }
    }

}