package com.pdf.music

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pdf.music.bookmark.BookmarkActivity
import com.pdf.music.databinding.ActivityDetailBinding
import java.io.File

class DetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.extras?.getString("KEY")?.let {
            binding.pdf.fromUri(Uri.fromFile(File(it)))
                .swipeHorizontal(true)
                .pageSnap(true)
                .autoSpacing(true)
                .pageFling(true)
                .load()
        } ?: run {
            Toast.makeText(this, "없음", Toast.LENGTH_SHORT).show()
        }

        binding.backImage.setOnClickListener {
            finish()
        }

        binding.bookmark.setOnClickListener {
            startActivity(
                Intent(this, BookmarkActivity::class.java)
            )
        }
    }
}