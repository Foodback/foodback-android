package com.example.foodback.ui.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.commit
import com.example.foodback.R
import com.example.foodback.databinding.ActivityRegisterBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private var _activityRegisterBinding: ActivityRegisterBinding? = null
    private val binding get() = _activityRegisterBinding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(dataStore)
    }

    private val mFragmentManager = supportFragmentManager
    private val mStartFragment = StartFragment()
    private val fragment = mFragmentManager.findFragmentByTag(StartFragment::class.java.simpleName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupAction()
        playAnimation()
    }

    private fun setupAction(){

        if (fragment !is StartFragment){
            mFragmentManager.commit {
                add(R.id.frame_register, mStartFragment, StartFragment::class.java.simpleName)
            }
        }
    }

    private fun playAnimation(){

    }

    override fun onDestroy() {
        super.onDestroy()
        _activityRegisterBinding = null
    }
}