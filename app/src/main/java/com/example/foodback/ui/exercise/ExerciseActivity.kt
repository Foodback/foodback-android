package com.example.foodback.ui.exercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodback.R
import com.example.foodback.adapter.AddAdapter
import com.example.foodback.data.FakeDataSource
import com.example.foodback.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {

    private var _activitExerciseBinding: ActivityExerciseBinding? = null
    private val binding get() = _activitExerciseBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activitExerciseBinding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.searchExercise.apply {
            setIconifiedByDefault(false)
            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if(query != null){
                        Toast.makeText(this@ExerciseActivity, "Searching $query", Toast.LENGTH_SHORT).show()
                        binding.searchExercise.clearFocus()
                    }else{
                        Toast.makeText(this@ExerciseActivity, "Reset search filter", Toast.LENGTH_SHORT).show()
                        binding.searchExercise.clearFocus()
                    }
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        binding.rvExercise.apply {
            val addAdapter = AddAdapter(FakeDataSource.dummyAdd){Toast.makeText(this@ExerciseActivity, it.name, Toast.LENGTH_SHORT).show()}
            val mLayoutManager = LinearLayoutManager(this@ExerciseActivity)
            addItemDecoration(DividerItemDecoration(this@ExerciseActivity, mLayoutManager.orientation))
            layoutManager = mLayoutManager
            adapter = addAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activitExerciseBinding = null
    }
}