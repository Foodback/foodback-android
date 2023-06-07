package com.example.foodback.ui.main

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodback.adapter.DiaryAdapter
import com.example.foodback.data.FakeDataSource
import com.example.foodback.databinding.FragmentDiaryBinding
import com.example.foodback.ui.exercise.ExerciseActivity
import com.example.foodback.ui.food.FoodActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DiaryFragment : Fragment() {

    private var _binding: FragmentDiaryBinding? = null
    private val binding get() = _binding!!

    private var date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialogDate = DatePickerDialog(requireActivity(), { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            date = selectedDate
            binding.tvDate.text = selectedDate
            Toast.makeText(requireActivity(), "Load diary on $selectedDate", Toast.LENGTH_SHORT).show()
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

        binding.tvDate.text = date

        binding.rvDiary.apply {
            val diaryAdapter = DiaryAdapter(
                data = FakeDataSource.dummyDiary,
                onClickData = { Toast.makeText(requireActivity(), "${it.name} clicked", Toast.LENGTH_SHORT).show() },
                onDeleteData = { Toast.makeText(requireActivity(), "${it.name} deleted", Toast.LENGTH_SHORT).show() },
                onAddFood = { startActivity(Intent(requireActivity(), FoodActivity::class.java)) },
                onAddExercises = {  startActivity(Intent(requireActivity(), ExerciseActivity::class.java)) }
            )
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = diaryAdapter
        }

        binding.btnDatePicker.setOnClickListener {
            dialogDate.show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}