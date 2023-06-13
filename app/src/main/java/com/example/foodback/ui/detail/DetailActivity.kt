package com.example.foodback.ui.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.foodback.R
import com.example.foodback.data.Result
import com.example.foodback.databinding.ActivityDetailBinding
import com.example.foodback.databinding.ActivityMainBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.edit.EditProfileViewModel
import com.example.foodback.ui.food.FoodActivity
import com.example.foodback.ui.main.ProfileFragment

class DetailActivity : AppCompatActivity() {

    private var _activitDetailBinding: ActivityDetailBinding? = null
    private val binding get() = _activitDetailBinding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val detailViewModel : DetailViewModel by viewModels {
        ViewModelFactory.getInstance(dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activitDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val output: String? = intent.getStringExtra(EXTRA_OUTPUT)
        binding.tvNameDetail.text = output

        binding.btnAddToDiary.setOnClickListener {
            val label = "breakfast"
            val name = "brekafast2"
            val amount: Long= 10
            val calories: Long = 1000
            val date = "2023-6-12"
            detailViewModel.addMeal(label, name, amount, calories, date).observe(this){ result ->
                when(result){
                    is Result.Loading ->{
                        binding.pbDetail.visibility = View.VISIBLE
                    }
                    is Result.Success ->{
                        binding.pbDetail.visibility = View.GONE
                        Toast.makeText(this@DetailActivity, result.data.message, Toast.LENGTH_SHORT).show()
//                        setResult(ProfileFragment.RESULT_OK, Intent())
//                        finish()
                    }
                    is Result.Error ->{
                        binding.pbDetail.visibility = View.GONE
                        Toast.makeText(this@DetailActivity, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activitDetailBinding = null
    }

    companion object{
        const val EXTRA_OUTPUT = "extra_output"
    }
}