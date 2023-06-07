package com.example.foodback.ui.login

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
import com.example.foodback.data.Result
import com.example.foodback.databinding.ActivityLoginBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.main.MainActivity
import com.example.foodback.ui.onboarding.OnBoardingActivity
import com.example.foodback.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private var _activityLoginBinding: ActivityLoginBinding? = null
    private val binding get() = _activityLoginBinding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val authViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupAction()
        playAnimation()
    }

    private fun setupAction(){
        binding.btnLogin.setOnClickListener {
            val email = binding.edEmailLogin.text.toString().trim()
            val password = binding.edPasswordLogin.text.toString()
            authViewModel.login(email, password).observe(this@LoginActivity){ result ->
                when(result){
                    is Result.Loading ->{
                        binding.pbLogin.visibility = View.VISIBLE
                    }
                    is Result.Success ->{
                        binding.pbLogin.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        OnBoardingActivity.fa.finish()
                        finish()
                    }
                    is Result.Error ->{
                        binding.pbLogin.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.tvMoveToRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
    }

    private fun playAnimation(){

    }

    override fun onDestroy() {
        super.onDestroy()
        _activityLoginBinding = null
    }
}