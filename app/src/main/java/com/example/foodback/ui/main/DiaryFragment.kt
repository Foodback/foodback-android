package com.example.foodback.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodback.R
import com.example.foodback.databinding.FragmentDiaryBinding
import com.example.foodback.ui.exercise.ExerciseActivity
import com.example.foodback.ui.food.FoodActivity

class DiaryFragment : Fragment() {

    private var _binding: FragmentDiaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddFood.setOnClickListener {
            startActivity(Intent(requireActivity(), FoodActivity::class.java))
        }

        binding.btnAddExercise.setOnClickListener {
            startActivity(Intent(requireActivity(), ExerciseActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}