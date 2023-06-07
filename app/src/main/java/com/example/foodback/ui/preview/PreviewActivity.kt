package com.example.foodback.ui.preview

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.example.foodback.R
import com.example.foodback.databinding.ActivityPreviewBinding
import com.example.foodback.ui.detail.DetailActivity
import com.example.foodback.ui.scan.ScanActivity
import com.example.foodback.utils.rotateFile
import java.io.File

class PreviewActivity : AppCompatActivity() {

//    private var getFile: File? = null

    private var _activityPreviewBinding : ActivityPreviewBinding? = null
    private val binding get() = _activityPreviewBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityPreviewBinding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("picture", File::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("picture")
        } as? File

        val isBackCamera = intent.getBooleanExtra("isBackCamera", true) as Boolean
        myFile?.let { file ->
            rotateFile(file, isBackCamera)
//            getFile = file
            binding.ivPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
        }

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this@PreviewActivity, DetailActivity::class.java))
            finish()
        }

        binding.btnCancel.setOnClickListener {
            startActivity(Intent(this@PreviewActivity, ScanActivity::class.java))
            finish()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _activityPreviewBinding = null
    }
}