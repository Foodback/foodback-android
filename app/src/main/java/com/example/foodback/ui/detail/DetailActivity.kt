package com.example.foodback.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodback.R
import com.example.foodback.databinding.ActivityDetailBinding
import com.example.foodback.databinding.ActivityMainBinding

class DetailActivity : AppCompatActivity() {

    private var _activitDetailBinding: ActivityDetailBinding? = null
    private val binding get() = _activitDetailBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activitDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnAddToDiary.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activitDetailBinding = null
    }
}