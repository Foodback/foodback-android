package com.example.foodback.ui.preview

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodback.data.DetailModel
import com.example.foodback.databinding.ActivityPreviewBinding
import com.example.foodback.ml.Mobilenet
import com.example.foodback.ui.detail.DetailActivity
import com.example.foodback.ui.scan.ScanActivity
import com.example.foodback.utils.rotateFile
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class PreviewActivity : AppCompatActivity() {

    private var getFile: File? = null

    private var _activityPreviewBinding : ActivityPreviewBinding? = null
    private val binding get() = _activityPreviewBinding!!

    private val imageProcessor = ImageProcessor.Builder()
        .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
        .build()

    private lateinit var labels: List<String>

    private lateinit var label: String
    private lateinit var calorieCsv: InputStreamReader
    private lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityPreviewBinding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        labels = application.assets.open("class.txt").bufferedReader().readLines()
        calorieCsv = InputStreamReader(assets.open("calorie.csv"))

        val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(EXTRA_PICTURE, File::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(EXTRA_PICTURE)
        } as? File
        label = intent.getStringExtra(EXTRA_LABEL)?: ""
        date = intent.getStringExtra(EXTRA_DATE)?: ""
        val isBackCamera = intent.getBooleanExtra(EXTRA_IS_BACK_CAMERA, true)
        val gallery = intent.getStringExtra(EXTRA_GALLERY)
        val galleryUri: Uri? = if(gallery != null) Uri.parse(gallery) else null

        myFile?.let { file ->
            rotateFile(file, isBackCamera)
            getFile = file
            if(galleryUri == null){
                binding.ivPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }else{
                binding.ivPreview.setImageURI(galleryUri)
            }
        }

        binding.btnAdd.setOnClickListener {
            if (getFile != null) {
                val output = outputGenerator(BitmapFactory.decodeFile(getFile!!.path))
                val intent = Intent(this@PreviewActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_LABEL, label)
                intent.putExtra(DetailActivity.EXTRA_DATE, date)
//                intent.putExtra(DetailActivity.EXTRA_GALLERY, gallery)
                intent.putExtra(DetailActivity.EXTRA_PICTURE, myFile)
                intent.putExtra(DetailActivity.EXTRA_IS_BACK_CAMERA, isBackCamera)
                intent.putExtra(DetailActivity.EXTRA_DATA, output)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@PreviewActivity, "Image empty", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnCancel.setOnClickListener {
            startActivity(Intent(this@PreviewActivity, ScanActivity::class.java))
            finish()
        }
    }

    private fun outputGenerator(bitmap: Bitmap): DetailModel{

        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)

        tensorImage = imageProcessor.process(tensorImage)

        val model = Mobilenet.newInstance(this)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorImage.buffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

        var maxIdx = 0
        outputFeature0.forEachIndexed { index, fl ->
            if(outputFeature0[maxIdx] < fl){
                maxIdx = index
            }
        }

        model.close()
        return readCalorie(calorieCsv, maxIdx)
    }

    private fun readCalorie(input: InputStreamReader, maxIdx: Int): DetailModel{
        val reader = BufferedReader(input)
        var line: String?
        val listCsv = mutableListOf<DetailModel>()

        while (reader.readLine().also { line = it } != null){
            val row: List<String> = line!!.split(";")
            val name = row[0].replace("_", " ")
            val amount = row[1].trim().replaceAfter(" ", "").toFloat()
            val calorie = row[2].replace("cal", "").toFloat()
            val detailModel = DetailModel(name, (calorie/amount).toLong(), row[3], row[4], row[8], row[9], row[11], row[12])
            listCsv.add(detailModel)
        }
        return listCsv[maxIdx]
    }


    override fun onDestroy() {
        super.onDestroy()
        _activityPreviewBinding = null
    }

    companion object{
        const val EXTRA_LABEL = "extra_label"
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_PICTURE = "extra_picture"
        const val EXTRA_GALLERY = "extra_gallery"
        const val EXTRA_IS_BACK_CAMERA = "extra_isBackCamera"
    }
}