package com.example.foodback.ui.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.commit
import com.example.foodback.R
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.main.MainActivity

class AuthActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(dataStore)
    }

    private val mFragmentManager = supportFragmentManager
    private val mLoginFragment = LoginFragment()
    private val fragment = mFragmentManager.findFragmentByTag(LoginFragment::class.java.simpleName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

//        authViewModel.isLogin().observe(this){
//            if(it != null){
//                Log.i("TEST", "onCreate: LOGGED IN")
//                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
//                finish()
//            }
//        }

        if (fragment !is LoginFragment){
            mFragmentManager.commit {
                add(R.id.frame_container, mLoginFragment, LoginFragment::class.java.simpleName)
            }
        }
    }
}