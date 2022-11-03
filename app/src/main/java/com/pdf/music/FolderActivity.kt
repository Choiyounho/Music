package com.pdf.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pdf.music.databinding.ActivityFolderBinding
import com.pdf.music.db.AppDatabase

class FolderActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFolderBinding.inflate(layoutInflater) }

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val deleteAdapter = MusicDeleteAdapter {
            // 삭제
        }

        val verticalAdapter = MusicVerticalAdapter {
        // 추기
        }

        binding.musicDeleteRecyclerView.adapter = deleteAdapter
        binding.musicDeleteRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.musicAddRecyclerView.adapter = verticalAdapter
        binding.musicAddRecyclerView.layoutManager = LinearLayoutManager(this)

        db = AppDatabase.getInstance(this)

        Thread {
            val list = db.musicDao().musics()

            val list2 = db.musicDao().musics()

            runOnUiThread {
                deleteAdapter.setItems(list)

                verticalAdapter.setItems(list2)
            }
        }.start()
    }
}