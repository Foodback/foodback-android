package com.example.foodback.ui.main

import android.content.Context
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
import com.example.foodback.databinding.FragmentHomeBinding
import com.example.foodback.ui.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _fragmentHomeBinding: FragmentHomeBinding? = null
    private val binding get() = _fragmentHomeBinding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private var date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private val mainViewModel: MainViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore, date)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.profileData.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Loading ->{
                    binding.pbHome.visibility = View.VISIBLE
                }
                is Result.Success ->{
                    binding.pbHome.visibility = View.GONE
                    binding.tvNameHome.text = result.data.profileData.username
                    binding.tvTbHome.text = "${result.data.profileData.height}CM/${result.data.profileData.weight}KG"
                }
                is Result.Error ->{
                    binding.pbHome.visibility = View.GONE
                    Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        mainViewModel.homeData.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Loading ->{
                    binding.pbHome.visibility = View.VISIBLE
                }
                is Result.Success ->{
                    binding.pbHome.visibility = View.GONE
                    binding.circularProgressBar.apply {
                        setProgressWithAnimation((result.data.foodCalories - result.data.exerciseCalories).toFloat(), 1000)
                        progressMax = result.data.calorieNeeds.toFloat()
                    }
                    binding.eatProgress.apply {
                        setProgressWithAnimation(result.data.foodCalories.toFloat(), 1000)
                        progressMax = (result.data.calorieNeeds + result.data.exerciseCalories).toFloat()
                    }
                    binding.burnProgress.apply {
                        setProgressWithAnimation((result.data.exerciseCalories.toFloat()), 1000)
                        progressMax = (result.data.foodCalories + result.data.target).toFloat()
                    }
                    binding.tvGoalHome.text = result.data.goal
                    binding.tvTargetHome.text = "${result.data.target} Kg"
                    binding.tvFoodHome.text = "${result.data.foodCalories} Cal"
                    binding.tvExerciseHome.text =  "${result.data.exerciseCalories} Cal"
                    binding.tvTotalHome.text = "${result.data.foodCalories - result.data.exerciseCalories} Cal"
                    binding.tvCaloriesNeedsHome.text = "${result.data.calorieNeeds} Cal"
                }
                is Result.Error ->{
                    binding.pbHome.visibility = View.GONE
                    Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentHomeBinding = null
    }

}