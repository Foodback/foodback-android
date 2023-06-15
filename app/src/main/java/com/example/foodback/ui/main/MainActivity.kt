package com.example.foodback.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.foodback.R
import com.example.foodback.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding!!

    private val mFragmentManager = supportFragmentManager
    private val mHomeFragment = HomeFragment()
    private val mDiaryFragment = DiaryFragment()
    private val mProfileFragment = ProfileFragment()
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
    }

    private fun navigateToFragment(mFragment: Fragment){
        mFragmentManager.commit {
            replace(R.id.frame_home, mFragment, mFragment::class.java.simpleName)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityMainBinding = null
    }
}