package com.example.foodback.ui.exercise

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.foodback.data.Result
import com.example.foodback.databinding.FragmentExerciseFormBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.main.DiaryFragment

class ExerciseFormFragment : Fragment() {

    private var _fragmentExerciseFormBinding: FragmentExerciseFormBinding? = null
    private val binding get() = _fragmentExerciseFormBinding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val exerciseViewModel: ExerciseViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _fragmentExerciseFormBinding = FragmentExerciseFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddForm.setOnClickListener {
            val name = binding.edFormName.text.toString().trim()
            val calories = binding.edFormCalorie.text.toString().trim()
            val duration = binding.edFormDuration.text.toString().trim()
            val sets = binding.edFormRepetition.text.toString().trim()
            val repetition= binding.edFormCalorie.text.toString().trim()
            if(name.isNotBlank() && calories.isNotBlank() && duration.isNotBlank() && sets.isNotBlank() && repetition.isNotBlank()){
                exerciseViewModel.addExercise(name, calories.toLong(), duration.toLong(), sets.toLong(), repetition.toLong(), exerciseViewModel.date).observe(viewLifecycleOwner){ result ->
                    when(result){
                        is Result.Loading ->{
                            binding.pbFormExercise.visibility = View.VISIBLE
                        }
                        is Result.Success ->{
                            binding.pbFormExercise.visibility = View.GONE
                            Toast.makeText(requireActivity(), result.data.message, Toast.LENGTH_SHORT).show()
                            requireActivity().setResult(DiaryFragment.RESULT_OK, Intent())
                            requireActivity().finish()
                        }
                        is Result.Error ->{
                            binding.pbFormExercise.visibility = View.GONE
                            Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Toast.makeText(requireActivity(), "Can't be blank", Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentExerciseFormBinding = null
    }
}