package com.example.foodback.ui.register

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.foodback.R
import com.example.foodback.databinding.FragmentGoalBinding
import com.example.foodback.ui.ViewModelFactory

class GoalFragment : Fragment() {

    private var _binding: FragmentGoalBinding? = null
    private val binding get() = _binding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val registerViewModel: RegisterViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }

    private var goal: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if (savedInstanceState != null) goal = savedInstanceState.getString(goal, "")
        _binding = FragmentGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButton()

        binding.weightGain.setOnClickListener {
            goal = "gain"
            binding.weightGain.isChecked = !binding.weightGain.isChecked
            binding.weightLoss.isChecked = false
            binding.calorieManage.isChecked = false
            setColorBackground()
            setButton()
        }

        binding.weightLoss.setOnClickListener {
            goal = "loss"
            binding.weightLoss.isChecked = !binding.weightLoss.isChecked
            binding.weightGain.isChecked = false
            binding.calorieManage.isChecked = false
            setColorBackground()
            setButton()
        }

        binding.calorieManage.setOnClickListener {
            goal = "maintain"
            binding.calorieManage.isChecked = !binding.weightLoss.isChecked
            binding.weightGain.isChecked = false
            binding.weightLoss.isChecked = false
            setColorBackground()
            setButton()
        }

        binding.btnNext.setOnClickListener {
            registerViewModel.addData(GOAL_KEY, goal)
            navigateFragment(2)
        }

        binding.ivBack.setOnClickListener {
            navigateFragment(0)
        }
    }


    private fun setColorBackground(){
        val primaryColor = ContextCompat.getColor(requireContext(), R.color.primary)
        val whiteColor = ContextCompat.getColor(requireContext(), R.color.white)
        val textColor = ContextCompat.getColor(requireContext(), R.color.second_text2)


        binding.weightLoss.setCardBackgroundColor(if (binding.weightLoss.isChecked) primaryColor else whiteColor)
        binding.weightGain.setCardBackgroundColor(if (binding.weightGain.isChecked) primaryColor else whiteColor)
        binding.calorieManage.setCardBackgroundColor(if (binding.calorieManage.isChecked) primaryColor else whiteColor)

        binding.textWeightLoss.setTextColor(if (binding.weightLoss.isChecked) whiteColor else textColor)
        binding.textWeightGain.setTextColor(if (binding.weightGain.isChecked) whiteColor else textColor)
        binding.textCalorieManage.setTextColor(if (binding.calorieManage.isChecked) whiteColor else textColor)

        binding.descWeightLoss.setTextColor(if (binding.weightLoss.isChecked) whiteColor else textColor)
        binding.descWeightGain.setTextColor(if (binding.weightGain.isChecked) whiteColor else textColor)
        binding.descCalorieManage.setTextColor(if (binding.calorieManage.isChecked) whiteColor else textColor)
    }

    private fun setButton(){
        binding.btnNext.isEnabled = binding.weightGain.isChecked || binding.weightLoss.isChecked || binding.calorieManage.isChecked
    }

    private fun navigateFragment(fragmentIndex: Int) {
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        val bundle = Bundle()
        bundle.putString(GOAL_KEY, goal)
        viewPager?.currentItem = fragmentIndex
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(GOAL_KEY, goal)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        var GOAL_KEY: String = "goal"
    }
}