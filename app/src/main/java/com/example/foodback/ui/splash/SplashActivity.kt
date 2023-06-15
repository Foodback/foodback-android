package com.example.foodback.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.foodback.R
import com.example.foodback.data.Result
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.login.LoginViewModel
import com.example.foodback.ui.main.MainActivity
import com.example.foodback.ui.onboarding.OnBoardingActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SplashActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")
    private var date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(dataStore, date)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed({
            loginViewModel.isLogin().observe(this){ result ->
                when(result){
                    is Result.Loading -> {}
                    is Result.Success -> {
                        if(result.data != null){
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        }else{
                            startActivity(Intent(this@SplashActivity, OnBoardingActivity::class.java))
                        }
                        finish()
                    }
                    is Result.Error -> { Toast.makeText(this@SplashActivity, result.error, Toast.LENGTH_SHORT).show() }
                }
            }
        }, DELAY_TIME)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    companion object{
        private const val DELAY_TIME: Long = 3000
    }
}