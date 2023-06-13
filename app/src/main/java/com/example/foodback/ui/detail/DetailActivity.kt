package com.example.foodback.ui.detail

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.foodback.R
import com.example.foodback.data.Result
import com.example.foodback.databinding.ActivityDetailBinding
import com.example.foodback.databinding.ActivityMainBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.edit.EditProfileViewModel
import com.example.foodback.ui.food.FoodActivity
import com.example.foodback.ui.food.FoodViewModel
import com.example.foodback.ui.main.ProfileFragment
import com.example.foodback.ui.preview.PreviewActivity

class DetailActivity : AppCompatActivity() {

    private var _activitDetailBinding: ActivityDetailBinding? = null
    private val binding get() = _activitDetailBinding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val detailViewModel : DetailViewModel by viewModels {
        ViewModelFactory.getInstance(dataStore)
    }

    private val foodViewModel: FoodViewModel by viewModels {
        ViewModelFactory.getInstance(dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activitDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val label = intent.getStringExtra(PreviewActivity.EXTRA_LABEL)?:""
        val date = intent.getStringExtra(PreviewActivity.EXTRA_DATE)?:""
        val output: String? = intent.getStringExtra(EXTRA_OUTPUT)
        binding.tvNameDetail.text = output

        val dialog = Dialog(this)
        dialog.apply {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_food)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        val btnYes = dialog.findViewById<Button>(R.id.alert_yes_food)
        val btnNo = dialog.findViewById<Button>(R.id.alert_no_food)
        val edAmount = dialog.findViewById<EditText>(R.id.ed_amount_alert)



        foodViewModel.foodData.observe(this@DetailActivity){ foodResult ->
            when(foodResult){
                is Result.Loading ->{
                    binding.pbDetail.visibility = View.VISIBLE
                }
                is Result.Success ->{
                    binding.pbDetail.visibility = View.GONE
                    Glide.with(this@DetailActivity)
                        .load(foodResult.data.foodData.menuItems[0].image)
                        .error(R.drawable.danny)
                        .into(binding.ivImageDetail)
                    binding.tvCaloriesDetail2.text = foodResult.data.foodData.menuItems[0].nutrition.calories.toString()
                    binding.btnAddToDiaryDetail.setOnClickListener {
                        val name = foodResult.data.foodData.menuItems[0].title
                        val calories = foodResult.data.foodData.menuItems[0].nutrition.calories
                        btnYes.setOnClickListener {
                            detailViewModel.addMeal(label, name, edAmount.toString().toLong(), calories, date).observe(this){ addResult ->
                                when(addResult){
                                    is Result.Loading ->{
                                        binding.pbDetail.visibility = View.VISIBLE
                                    }
                                    is Result.Success ->{
                                        binding.pbDetail.visibility = View.GONE
                                        Toast.makeText(this@DetailActivity, addResult.data.message, Toast.LENGTH_SHORT).show()
                                        setResult(ProfileFragment.RESULT_OK, Intent())
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
                is Result.Error ->{
                    binding.pbDetail.visibility = View.GONE
                    Toast.makeText(this@DetailActivity, foodResult.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activitDetailBinding = null
    }

    companion object{
        const val EXTRA_LABEL = "extra_label"
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_OUTPUT = "extra_output"
    }
}