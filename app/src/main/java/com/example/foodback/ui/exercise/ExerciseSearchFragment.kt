package com.example.foodback.ui.exercise

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodback.R
import com.example.foodback.adapter.ExerciseAdapter
import com.example.foodback.data.Result
import com.example.foodback.databinding.FragmentExerciseSearchBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.main.DiaryFragment

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

        val dialog = Dialog(requireActivity())
        dialog.apply {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_exercise)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
        val btnYes = dialog.findViewById<Button>(R.id.alert_yes_exercise)
        val btnNo = dialog.findViewById<Button>(R.id.alert_no_exercise)
        val edDuration = dialog.findViewById<EditText>(R.id.ed_duration_alert)

        exerciseViewModel.exerciseData.observe(viewLifecycleOwner){ resultData ->
            when(resultData){
                is Result.Loading ->{
                    binding.pbSearchExercise.visibility = View.VISIBLE
                }
                is Result.Success ->{
                    binding.pbSearchExercise.visibility = View.GONE
                    val exerciseAdapter = ExerciseAdapter(resultData.data.exerciseData){ exercise ->
                        btnYes.setOnClickListener {
                            if(edDuration.text.toString().isNotBlank()){
                                exerciseViewModel.addExercise(exercise.name, exercise.total_calories/exercise.duration_minutes*edDuration.text.toString().toLong(), edDuration.text.toString().toLong(), 0, 0, exerciseViewModel.date).observe(viewLifecycleOwner){ result ->
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
                            }else{
                                Toast.makeText(requireActivity(), "Can't be blank", Toast.LENGTH_SHORT).show()
                            }
                        }
                        btnNo.setOnClickListener {
                            dialog.dismiss()
                        }
                        dialog.show()
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