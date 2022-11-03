package com.pdf.music

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pdf.music.Converter.bitmapToString
import com.pdf.music.PathUtil.getPath
import com.pdf.music.databinding.ActivityMainBinding
import com.pdf.music.db.AppDatabase
import com.pdf.music.db.FolderEntity
import com.pdf.music.db.MusicEntity
import com.shockwave.pdfium.PdfiumCore

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var db: AppDatabase

    private val musicAdapter = MusicAdapter(
        click = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("KEY", it.path)
            intent.putExtra("TITLE", it.title)
            startActivity(intent)
        }
    )

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }

    var folderName = ""

    private val getLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it ?: return@registerForActivityResult
        val path = getPath(this, it) ?: return@registerForActivityResult

        val pdfium = PdfiumCore(this)
        val name = path.split("/").last().split(".").first()

        // https://stackoverflow.com/questions/38828396/generate-thumbnail-of-pdf-in-android
        // 썸네일 만드는 기능

        // 14일
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
                db.musicDao().insertMusic(
                    MusicEntity(
                        title = name,
                        path = path,
                        folderName = folderName,
                        bitmapToString(bmp)
                    )
                )

                val list: List<MusicEntity> = if (folderName != "") {
                    db.musicDao().musicsFolder(folderName)
                } else {
                    db.musicDao().musics()
                }

                runOnUiThread {
                    musicAdapter.setItems(list)
                }
            }.start()

        } catch (e: Exception) {
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

        binding.addFolderButton.setOnClickListener {
            startActivity(
                Intent(this, FolderActivity::class.java)
            )
        }

        binding.menuButton.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT).not()) {
                binding.drawerLayout.openDrawer(Gravity.LEFT)
                binding.drawerLayout.translationZ = 4f
            } else {
                binding.drawerLayout.closeDrawer(Gravity.LEFT)
                binding.drawerLayout.translationZ = -4f
            }
        }

        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {
                binding.drawerLayout.translationZ = 4f
            }

            override fun onDrawerClosed(drawerView: View) {
                binding.drawerLayout.translationZ = -4f
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })

        // 데이터 불러오기
        db = AppDatabase.getInstance(this)
    }

    override fun onResume() {
        super.onResume()

        binding.musicRecyclerView.adapter = musicAdapter
        binding.musicRecyclerView.layoutManager = GridLayoutManager(this, 2)

        Thread {
            val list = db.musicDao().musics()
            runOnUiThread {
                musicAdapter.setItems(list)
            }
        }.start()

        Thread {
            val list = mutableListOf<FolderEntity>()
            list.addAll(db.musicDao().folders())

            val folderAdapter = FolderAdapter(
                click = {
                    val musics = db.musicDao().musicsFolder(it)
                    folderName = it
                    runOnUiThread {
                        musicAdapter.setItems(musics)
                    }
                }
            )

            runOnUiThread {
                binding.folderRecyclerView.adapter = folderAdapter
                binding.folderRecyclerView.layoutManager = LinearLayoutManager(this)
            }

        }.start()



    }
}
