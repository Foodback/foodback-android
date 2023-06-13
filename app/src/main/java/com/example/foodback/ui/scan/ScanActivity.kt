package com.example.foodback.ui.scan

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.foodback.R
import com.example.foodback.databinding.ActivityScanBinding
import com.example.foodback.ui.detail.DetailActivity
import com.example.foodback.ui.preview.PreviewActivity
import com.example.foodback.utils.createFile
import com.example.foodback.utils.uriToFile
import java.io.File

class ScanActivity : AppCompatActivity() {

    private var getFile: File? = null

    private var _activityScanBinding : ActivityScanBinding? = null
    private val binding get() = _activityScanBinding!!

    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private lateinit var label: String
    private lateinit var date: String

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@ScanActivity)
                val intent = Intent(this@ScanActivity, PreviewActivity::class.java)
                intent.putExtra(PreviewActivity.EXTRA_LABEL, label)
                intent.putExtra(PreviewActivity.EXTRA_DATE, date)
                intent.putExtra(PreviewActivity.EXTRA_PICTURE, myFile)
                intent.putExtra(PreviewActivity.EXTRA_GALLERY, uri.toString())
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        _activityScanBinding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        label = intent.getStringExtra(EXTRA_LABEL)?:""
        date = intent.getStringExtra(EXTRA_DATE)?:""

        binding.captureImage.setOnClickListener { takePhoto() }
        binding.switchCamera.setOnClickListener { startCamera() }
        binding.btnGallery.setOnClickListener { startGallery() }
    }

    public override fun onResume() {
        super.onResume()
        startCamera()
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Toast.makeText(this@ScanActivity, "Failed to take a picture.", Toast.LENGTH_SHORT).show()
            }
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val intent = Intent(this@ScanActivity, PreviewActivity::class.java)
                intent.putExtra(PreviewActivity.EXTRA_LABEL, label)
                intent.putExtra(PreviewActivity.EXTRA_DATE, date)
                intent.putExtra(PreviewActivity.EXTRA_PICTURE, photoFile)
                intent.putExtra(PreviewActivity.EXTRA_IS_BACK_CAMERA, cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                startActivity(intent)
                finish()
            }
        }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Toast.makeText(this@ScanActivity, "Failed to open the camera.", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))

        binding.switchCamera.setOnClickListener {
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityScanBinding = null
    }

    companion object{
        const val EXTRA_LABEL = "extra_label"
        const val EXTRA_DATE = "extra_date"
    }
}