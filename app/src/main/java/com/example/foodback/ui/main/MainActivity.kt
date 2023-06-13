package com.example.foodback.ui.main

import android.content.Context
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
import com.example.foodback.ui.login.LoginViewModel

class MainActivity : AppCompatActivity() {

    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val mainViewModel: MainViewModel by viewModels {
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

        supportActionBar?.hide()

        setMode(R.id.home)

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
//        setActionBarTitle(title)
    }

    private fun navigateToFragment(mFragment: Fragment){
        mFragmentManager.commit {
//            addToBackStack(null)
            replace(R.id.frame_home, mFragment, mFragment::class.java.simpleName)
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