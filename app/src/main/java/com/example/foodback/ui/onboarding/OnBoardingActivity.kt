package com.example.foodback.ui.onboarding

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodback.databinding.ActivityOnBoardingBinding
import com.example.foodback.ui.login.LoginActivity
import com.example.foodback.ui.register.RegisterActivity

class OnBoardingActivity : AppCompatActivity() {

    private var _activityOnBoardingBinding: ActivityOnBoardingBinding? = null
    private val binding get() = _activityOnBoardingBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityOnBoardingBinding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        fa = this

        binding.btnMoveLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnMoveRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _activityOnBoardingBinding = null
    }

    companion object{
        lateinit var fa: Activity
    }
}