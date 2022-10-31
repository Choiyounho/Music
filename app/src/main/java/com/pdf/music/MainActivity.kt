package com.pdf.music

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.pdf.music.Converter.bitmapToString
import com.pdf.music.PathUtil.getPath
import com.pdf.music.databinding.ActivityMainBinding
import com.pdf.music.db.AppDatabase
import com.pdf.music.db.MusicEntity
import com.shockwave.pdfium.PdfiumCore

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var db: AppDatabase

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }

    private val getLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it ?: return@registerForActivityResult
        val path = getPath(this, it) ?: return@registerForActivityResult

        val pdfium = PdfiumCore(this)

        try {
            val fd = contentResolver.openFileDescriptor(it, "r")
            val pdfDocu = pdfium.newDocument(fd)
            pdfium.openPage(pdfDocu, 0)
            val width = pdfium.getPageWidthPoint(pdfDocu, 0)
            val height = pdfium.getPageHeightPoint(pdfDocu, 0)

            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            pdfium.renderPageBitmap(pdfDocu, bmp, 0, 0, 0, width, height)
            pdfium.closeDocument(pdfDocu)

            Thread {
                db.musicDao().insert(
                    MusicEntity(
                        title = "제목",
                        path = path,
                        folderName = "기본",
                        bitmapToString(bmp)
                    )
                )
            }.start()
        } catch (e: Exception) {
            Log.e("TestT", e.message!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 권한 체크
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            permissionLauncher.launch(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        }

        // 버튼 기능
        binding.fab.setOnClickListener {
            getLauncher.launch("application/pdf")
        }

        binding.menuButton.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT).not()) {
                binding.drawerLayout.openDrawer(Gravity.LEFT)
            } else {
                binding.drawerLayout.closeDrawer(Gravity.LEFT)
            }
        }

        // 데이터 불러오기
        db = AppDatabase.getInstance(this)
    }

    override fun onResume() {
        super.onResume()

        Thread {
            val list = db.musicDao().musics()
            runOnUiThread {
                val adapter = MusicAdapter(
                    list,
                    click = {
                        val intent = Intent(this, DetailActivity::class.java)
                        Log.e("TestT", "${it.path}")
                        intent.putExtra("KEY", it.path)
                        startActivity(intent)
                    }
                )

                binding.musicRecyclerView.adapter = adapter
                binding.musicRecyclerView.layoutManager = GridLayoutManager(this, 2)

                adapter.notifyDataSetChanged()
            }
        }.start()
    }
}
