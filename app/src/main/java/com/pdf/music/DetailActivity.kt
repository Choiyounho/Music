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

        var path = ""

        intent.extras?.getString("KEY")?.let {
            path = it
            val pdf = binding.pdf.fromUri(Uri.fromFile(File(it)))
                .swipeHorizontal(true)
                .pageSnap(true)
                .autoSpacing(true)
                .pageFling(true)

            intent.extras?.getInt("PAGE")?.let {
                pdf.defaultPage(it)
                    .load()
            } ?: run {
                pdf.load()
            }
        } ?: run {
            Toast.makeText(this, "없음", Toast.LENGTH_SHORT).show()
        }

        intent.extras?.getString("TITLE")?.let {
            binding.title.text = it
        }

        binding.backImage.setOnClickListener {
            finish()
        }

        binding.bookmark.setOnClickListener {
            startActivity(
                Intent(this, BookmarkActivity::class.java).apply {
                    putExtra("KEY", path)
                }
            )
        }
    }
}