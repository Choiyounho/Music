package com.pdf.music.bookmark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.pdf.music.R
import com.pdf.music.databinding.ActivityBookmarkBinding
import com.pdf.music.db.AppDatabase

class BookmarkActivity : AppCompatActivity() {

    private val binding by lazy { ActivityBookmarkBinding.inflate(layoutInflater) }

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)

        binding.add.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_bookmark, null)

            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            dialog.show()

            dialog.findViewById<Button>(R.id.button)?.setOnClickListener {

                dialog.dismiss()
            }
        }


    }
}