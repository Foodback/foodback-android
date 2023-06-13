package com.example.foodback.ui.food

import android.Manifest
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
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
import com.example.foodback.data.remote.response.ProfileData
import com.example.foodback.databinding.ActivityFoodBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.edit.EditProfileActivity
import com.example.foodback.ui.exercise.ExerciseFormFragment
import com.example.foodback.ui.exercise.ExerciseSearchFragment
import com.example.foodback.ui.exercise.ExerciseViewModel
import com.example.foodback.ui.main.HomeFragment
import com.example.foodback.ui.scan.ScanActivity

class FoodActivity : AppCompatActivity() {

    private var _activityFoodBinding: ActivityFoodBinding? = null
    private val binding get() = _activityFoodBinding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val foodViewModel: FoodViewModel by viewModels {
        ViewModelFactory.getInstance(dataStore)
    }

    private val mFragmentManager = supportFragmentManager
    private val mFoodFormFragment = FoodFormFragment()
    private val mFoodSearchFragment = FoodSearchFragment()
    private val fragment = mFragmentManager.findFragmentByTag(FoodFormFragment::class.java.simpleName)
    private var title : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityFoodBinding = ActivityFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val date: String? = intent.getStringExtra(EXTRA_DATE)
        if (date != null) foodViewModel.setDate(date)

        val label: String? = intent.getStringExtra(EXTRA_LABEL)
        if (label != null) foodViewModel.setLabel(label)

        if (fragment !is FoodFormFragment){
            mFragmentManager.commit {
                add(R.id.frame_food, mFoodFormFragment, FoodFormFragment::class.java.simpleName)
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

        searchView.queryHint = "Search Food..."
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null){
                    foodViewModel.getFood(query)
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
                navigateToFragment(mFoodSearchFragment)
                true
            }
            else -> true
        }
    }

    private fun navigateToFragment(mFragment: Fragment){
        mFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.frame_food, mFragment, mFragment::class.java.simpleName)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFoodBinding = null
    }

    companion object{
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_LABEL = "extra_label"
    }
}