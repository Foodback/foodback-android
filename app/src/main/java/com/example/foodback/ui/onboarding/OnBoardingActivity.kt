package com.example.foodback.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodback.R
import com.example.foodback.databinding.ActivityOnBoardingBinding
import com.example.foodback.ui.auth.AuthActivity

class OnBoardingActivity : AppCompatActivity() {

    private var _activityOnBoardingBinding: ActivityOnBoardingBinding? = null
    private val binding get() = _activityOnBoardingBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityOnBoardingBinding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMoveRegister.setOnClickListener{
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        binding.btnMoveLogin.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityOnBoardingBinding = null
    }
}