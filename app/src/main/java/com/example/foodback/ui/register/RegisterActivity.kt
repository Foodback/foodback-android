package com.example.foodback.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodback.databinding.ActivityRegisterBinding
import com.example.foodback.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private var _activityRegisterBinding: ActivityRegisterBinding? = null
    private val binding get() = _activityRegisterBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupAction()
    }

    private fun setupAction(){
        val fragmentList = arrayListOf(
            StartFragment(),
            GoalFragment(),
            GenderFragment(),
            AgeFragment(),
            CurrentFragment(),
            TargetFragment(),
            LevelFragment(),
            RegisterFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager)
        binding.viewPager.isUserInputEnabled = false

        binding.tvMoveToLogin.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityRegisterBinding = null
    }
}