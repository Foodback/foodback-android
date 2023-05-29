package com.example.foodback.ui.food

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.foodback.R
import com.example.foodback.databinding.ActivityDetailBinding
import com.example.foodback.databinding.ActivityFoodBinding
import com.example.foodback.ui.scan.ScanActivity

class FoodActivity : AppCompatActivity() {

    private var _activitFoodBinding: ActivityFoodBinding? = null
    private val binding get() = _activitFoodBinding!!

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            startCameraX()
        }else{
            Toast.makeText(this, "Please allow permission on settings to use this features", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activitFoodBinding = ActivityFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCamera.setOnClickListener {
            if (checkPermission(Manifest.permission.CAMERA))  startCameraX()
            else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun checkPermission(permission: String) = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    private fun startCameraX() {
        startActivity(Intent(this, ScanActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _activitFoodBinding = null
    }
}