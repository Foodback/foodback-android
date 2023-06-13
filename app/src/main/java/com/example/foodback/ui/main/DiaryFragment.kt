package com.example.foodback.ui.main

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodback.adapter.DiaryAdapter
import com.example.foodback.data.Result
import com.example.foodback.databinding.FragmentDiaryBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.exercise.ExerciseActivity
import com.example.foodback.ui.food.FoodActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DiaryFragment : Fragment() {

    private var _fragmentDiaryBinding: FragmentDiaryBinding? = null
    private val binding get() = _fragmentDiaryBinding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val mainViewModel: MainViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }

    private var date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _fragmentDiaryBinding = FragmentDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.getDiary(date)

        val launcherAddDiaryActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == ProfileFragment.RESULT_OK) {
                mainViewModel.getDiary(date)
            }
        }

        val dialogDate = DatePickerDialog(requireActivity(), { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            date = selectedDate
            binding.tvDate.text = selectedDate
            mainViewModel.getDiary(date)
            Toast.makeText(requireActivity(), "Load diary on $selectedDate", Toast.LENGTH_SHORT).show()
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

        binding.tvDate.text = date

        mainViewModel.diaryData.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Loading ->{
                    binding.pbDiary.visibility = View.VISIBLE
                }
                is Result.Success ->{
                    binding.pbDiary.visibility = View.GONE
                    val diaryAdapter = DiaryAdapter(
                        data = result.data,
                        onClickData = { Toast.makeText(requireActivity(), "${it} clicked", Toast.LENGTH_SHORT).show() },
                        onDeleteData = { Toast.makeText(requireActivity(), "${it} deleted", Toast.LENGTH_SHORT).show() },
                        onAddFood = {
                            val intent = Intent(requireActivity(), FoodActivity::class.java)
                            intent.putExtra(FoodActivity.EXTRA_DATE, date)
                            intent.putExtra(FoodActivity.EXTRA_LABEL, it)
                            launcherAddDiaryActivity.launch(intent)
                        },
                        onAddExercises = {
                            val intent = Intent(requireActivity(), ExerciseActivity::class.java)
                            intent.putExtra(ExerciseActivity.EXTRA_DATE, date)
                            launcherAddDiaryActivity.launch(intent)
                        }
                    )
                    binding.rvDiary.apply {
                        layoutManager = LinearLayoutManager(requireActivity())
                        adapter = diaryAdapter
                    }
                }
                is Result.Error ->{
                    Log.i("TEST", "diaryData Error")
                    binding.pbDiary.visibility = View.GONE
                    Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnDatePicker.setOnClickListener {
            dialogDate.show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentDiaryBinding = null
    }

    companion object{
        const val RESULT_OK = 200
    }
}