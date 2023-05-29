package com.example.foodback.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.foodback.R
import com.example.foodback.databinding.ActivityMainBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.auth.AuthViewModel
import com.example.foodback.ui.onboarding.OnBoardingActivity

class MainActivity : AppCompatActivity() {

    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(dataStore)
    }

    private val mFragmentManager = supportFragmentManager
    private val mHomeFragment = HomeFragment()
    private val mDiaryFragment = DiaryFragment()
    private val mProfileFragment = ProfileFragment()
    private val fragment = mFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)
    private var title : String = "Home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        authViewModel.isLogin().observe(this){
//            if(it == null){
//                startActivity(Intent(this@MainActivity, OnBoardingActivity::class.java))
//                finish()
//            }
//        }

        if (fragment !is HomeFragment){
            mFragmentManager.commit {
                add(R.id.frame_container, mHomeFragment, HomeFragment::class.java.simpleName)
            }
        }

        binding.navBottom.setOnItemSelectedListener {
            setMode(it.itemId)
            true
        }

    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode){
            R.id.home -> {
                navigateToFragment(mHomeFragment)
                title ="Home"
            }
            R.id.diary -> {
                navigateToFragment(mDiaryFragment)
                title ="Diary"
            }
            R.id.profile -> {
                navigateToFragment(mProfileFragment)
                title ="Profile"
            }
        }
        setActionBarTitle(title)
    }

    private fun navigateToFragment(mFragment: Fragment){
        mFragmentManager.commit {
//            addToBackStack(null)
            replace(R.id.frame_container, mFragment, mFragment::class.java.simpleName)
        }
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityMainBinding = null
    }
}