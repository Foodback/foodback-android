package com.example.foodback.ui.detail

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.foodback.R
import com.example.foodback.data.DetailModel
import com.example.foodback.data.Result
import com.example.foodback.databinding.ActivityDetailBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.main.DiaryFragment
import java.io.File

class DetailActivity : AppCompatActivity() {

    private var getFile: File? = null

    private var _activityDetailBinding: ActivityDetailBinding? = null
    private val binding get() = _activityDetailBinding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val detailViewModel : DetailViewModel by viewModels {
        ViewModelFactory.getInstance(dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val detailData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA, DetailModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA)
        }?:DetailModel("",0, "", "", "", "", "", "")

        val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(EXTRA_PICTURE, File::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(EXTRA_PICTURE)
        } as? File
        val label = intent.getStringExtra(EXTRA_LABEL)?:""
        val date = intent.getStringExtra(EXTRA_DATE)?:""
        myFile?.let { file ->
            getFile = file
            binding.ivImageDetail.setImageBitmap(BitmapFactory.decodeFile(file.path))
        }

        binding.tvNameDetail.text = detailData.name
        binding.tvCaloriesDetail2.text = "${detailData.calorie} Cal"
        binding.tvFatDetail2.text = detailData.fat
        binding.tvCholestrolDetail2.text = detailData.cholesterol
        binding.tvSugarDetail2.text = detailData.sugar
        binding.tvProteinDetail2.text = detailData.protein
        binding.tvCalciumDetail2.text = detailData.calcium
        binding.tvIronDetail2.text = detailData.iron


        val dialog = Dialog(this)
        dialog.apply {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_food)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        val btnYes = dialog.findViewById<Button>(R.id.alert_yes_food)
        val btnNo = dialog.findViewById<Button>(R.id.alert_no_food)
        val edAmount = dialog.findViewById<EditText>(R.id.ed_amount_alert)

        binding.btnAddToDiaryDetail.setOnClickListener {
            btnYes.setOnClickListener {
                val amount = edAmount.text.toString().toLong()
                detailViewModel.addMeal(label, detailData.name, amount, detailData.calorie*amount, date).observe(this){ addResult ->
                    when(addResult){
                        is Result.Loading ->{
                            binding.pbDetail.visibility = View.VISIBLE
                        }
                        is Result.Success ->{
                            binding.pbDetail.visibility = View.GONE
                            Toast.makeText(this@DetailActivity, addResult.data.message, Toast.LENGTH_SHORT).show()
                            setResult(DiaryFragment.RESULT_OK, Intent())
                            finish()
                        }
                        is Result.Error ->{
                            binding.pbDetail.visibility = View.GONE
                            Toast.makeText(this@DetailActivity, addResult.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            btnNo.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityDetailBinding = null
    }

    companion object{
        const val EXTRA_LABEL = "extra_label"
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_PICTURE = "extra_picture"
        const val EXTRA_IS_BACK_CAMERA = "extra_isBackCamera"
    }
}