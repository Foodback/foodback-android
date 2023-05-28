package com.example.foodback.ui.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.foodback.R
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.auth.AuthViewModel
import com.example.foodback.ui.home.HomeActivity
import com.example.foodback.ui.onboarding.OnBoardingActivity

class SplashActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed({
            authViewModel.isLogin().observe(this){
                if(!it.isNullOrEmpty()){
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                }else{
                    startActivity(Intent(this@SplashActivity, OnBoardingActivity::class.java))
                }
                finish()
            }
        }, DELAY_TIME)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    companion object{
        private const val DELAY_TIME: Long = 1000
    }
}