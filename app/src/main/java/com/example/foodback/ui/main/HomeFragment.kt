package com.example.foodback.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import com.example.foodback.R
import com.example.foodback.data.Result
import com.example.foodback.databinding.FragmentDiaryBinding
import com.example.foodback.databinding.FragmentHomeBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.login.LoginActivity
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
                        progress = result.data.homeData.target.toFloat()
                        progressMax = 2650f
                    }
                    binding.eatProgress.apply {
                        progress = result.data.homeData.foodCalories.toFloat()
                        progressMax = 2650f
                    }
                    binding.burnProgress.apply {
                        progress = result.data.homeData.exerciseCalories.toFloat()
                        progressMax = 2650f
                    }
                    binding.tvGoalHome.text = result.data.homeData.goal
                    binding.tvTargetHome.text = "${result.data.homeData.target} KG"
                    binding.tvFoodHome.text = "${result.data.homeData.foodCalories} cal"
                    binding.tvExerciseHome.text =  "${result.data.homeData.exerciseCalories} cal"
                    binding.tvTotalHome.text = "2650"
                    binding.tvCaloriesTargetHome.text = "2650"
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