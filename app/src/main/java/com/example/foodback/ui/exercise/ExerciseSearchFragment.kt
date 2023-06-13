package com.example.foodback.ui.exercise

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodback.R
import com.example.foodback.adapter.ExerciseAdapter
import com.example.foodback.data.Result
import com.example.foodback.databinding.FragmentExerciseSearchBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.main.DiaryFragment
import com.example.foodback.ui.main.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExerciseSearchFragment : Fragment() {

    private var _fragmentExerciseSearchBinding: FragmentExerciseSearchBinding? = null
    private val binding get() = _fragmentExerciseSearchBinding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val exerciseViewModel: ExerciseViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _fragmentExerciseSearchBinding = FragmentExerciseSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val builder = AlertDialog.Builder(requireActivity())

        exerciseViewModel.exerciseData.observe(viewLifecycleOwner){ resultData ->
            when(resultData){
                is Result.Loading ->{
                    binding.pbSearchExercise.visibility = View.VISIBLE
                }
                is Result.Success ->{
                    binding.pbSearchExercise.visibility = View.GONE
                    val exerciseAdapter = ExerciseAdapter(resultData.data.exerciseData){
                        builder.setTitle("Add to Diary?")
                            .setCancelable(true)
                            .setPositiveButton("Yes"){ _, _ ->
                                exerciseViewModel.addExercise(it.name, it.total_calories, it.duration_minutes, 0, 0, exerciseViewModel.date).observe(viewLifecycleOwner){ result ->
                                    when(result){
                                        is Result.Loading ->{
                                            binding.pbSearchExercise.visibility = View.VISIBLE
                                        }
                                        is Result.Success ->{
                                            binding.pbSearchExercise.visibility = View.GONE
                                            Toast.makeText(requireActivity(), result.data.message, Toast.LENGTH_SHORT).show()
                                            requireActivity().setResult(DiaryFragment.RESULT_OK, Intent())
                                            requireActivity().finish()
                                        }
                                        is Result.Error -> {
                                            binding.pbSearchExercise.visibility = View.GONE
                                            Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                            .setNegativeButton("No"){dialogInterface, _, -> dialogInterface.cancel()}.show()
                    }
                    binding.rvSearchExercise.apply {
                        val mLayoutManager = LinearLayoutManager(requireActivity())
                        addItemDecoration(DividerItemDecoration(requireActivity(), mLayoutManager.orientation))
                        layoutManager = mLayoutManager
                        adapter = exerciseAdapter
                    }
                }
                is Result.Error ->{
                    binding.pbSearchExercise.visibility = View.GONE
                    Toast.makeText(requireActivity(), resultData.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentExerciseSearchBinding = null
    }
}