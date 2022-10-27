package com.pdf.music

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.pdf.music.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }

    private val getLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val path = it?.path

        it ?: return@registerForActivityResult


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
}