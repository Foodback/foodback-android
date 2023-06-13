package com.example.foodback.ui.exercise

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodback.R
import com.example.foodback.adapter.ExerciseAdapter
import com.example.foodback.adapter.FoodAdapter
import com.example.foodback.data.FakeDataSource
import com.example.foodback.data.Result
import com.example.foodback.databinding.ActivityExerciseBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.detail.DetailViewModel
import com.example.foodback.ui.edit.EditProfileViewModel
import com.example.foodback.ui.food.FoodActivity
import com.example.foodback.ui.main.DiaryFragment
import com.example.foodback.ui.main.HomeFragment
import com.example.foodback.ui.main.ProfileFragment

class ExerciseActivity : AppCompatActivity() {

    private var _activitExerciseBinding: ActivityExerciseBinding? = null
    private val binding get() = _activitExerciseBinding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val exerciseViewModel: ExerciseViewModel by viewModels {
        ViewModelFactory.getInstance(dataStore)
    }

    private val mFragmentManager = supportFragmentManager
    private val mExerciseFormFragment = ExerciseFormFragment()
    private val mExerciseSearchFragment = ExerciseSearchFragment()
    private val fragment = mFragmentManager.findFragmentByTag(ExerciseFormFragment::class.java.simpleName)
    private var title : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activitExerciseBinding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val date: String? = intent.getStringExtra(EXTRA_DATE)
        if (date != null) exerciseViewModel.setDate(date)

        if (fragment !is ExerciseFormFragment){
            mFragmentManager.commit {
                add(R.id.frame_exercise, mExerciseFormFragment, ExerciseFormFragment::class.java.simpleName)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.exercise_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        val searchItem = menu.findItem(R.id.search)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.queryHint = "Search Exercise..."
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null){
                    exerciseViewModel.getExercise(query)
                    searchView.clearFocus()
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                mFragmentManager.popBackStack()
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.search -> {
                navigateToFragment(mExerciseSearchFragment)
                true
            }
            else -> true
        }
    }

    private fun navigateToFragment(mFragment: Fragment){
        mFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.frame_exercise, mFragment, mFragment::class.java.simpleName)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activitExerciseBinding = null
    }

    companion object{
        const val EXTRA_DATE = "extra_date"
    }
}