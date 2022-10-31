package com.pdf.music

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import com.pdf.music.PathUtil.getPath
import com.pdf.music.databinding.ActivityMainBinding
import java.io.File
import java.net.URI
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.e("TestT", "hello")
        }

    private val getLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it ?: return@registerForActivityResult

        Log.e("TestT", it.path.toString())
        Log.e("TestT", it.toString())
        Log.e("TestT", it.encodedPath.toString())

        Log.e("TestT", getPath(this, it).toString())

        val test = applicationContext.contentResolver.openFileDescriptor(it, "r").use {
            it
        }

        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("KEY", getPath(this, it))
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

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
    }


//    fun getRealPath(contentUri: Uri): String? {
//        if (contentUri.path?.startsWith("/storage") == true) {
//            return if (contentUri.path != null) {
//                contentUri.path
//            } else null
//        }
//
//        val id = DocumentsContract.getDocumentId(contentUri).split(":")[1]
//        val columns = arrayOf(MediaStore.Files.FileColumns.DATA)
//        val selection = MediaStore.Files.FileColumns._ID + " = " + id;
//        val cursor = contentResolver.query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
//        cursor.use {
//            val columnIndex = cursor?.getColumnIndex(columns[0])
//            if (cursor?.moveToFirst() == true) {
//                return cursor.getString(columnIndex!!)
//            }
//        }
//        return null
//    }

}
