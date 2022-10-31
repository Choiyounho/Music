package com.pdf.music

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.net.toUri
import com.pdf.music.databinding.ActivityDetailBinding
import java.io.File

class DetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.extras?.getString("KEY")?.let {
            binding.pdf.fromUri(Uri.fromFile(File(it)))
//                .swipeHorizontal(false)
//                .enableSwipe(true)
                .swipeHorizontal(true)
                .pageSnap(true)
                .autoSpacing(true)
                .pageFling(true)
                .load()
        } ?: run {
            Toast.makeText(this, "없음", Toast.LENGTH_SHORT).show()
        }
    }
}