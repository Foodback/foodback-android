package com.example.foodback.ui.food

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodback.R
import com.example.foodback.adapter.FoodAdapter
import com.example.foodback.data.Result
import com.example.foodback.databinding.ActivityExerciseBinding
import com.example.foodback.databinding.FragmentFoodSearchBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.exercise.ExerciseViewModel
import com.example.foodback.ui.main.DiaryFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FoodSearchFragment : Fragment() {

    private var _fragmentFoodSearchBinding: FragmentFoodSearchBinding? = null
    private val binding get() = _fragmentFoodSearchBinding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val foodViewModel: FoodViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }

    private var date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _fragmentFoodSearchBinding = FragmentFoodSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val builder = AlertDialog.Builder(requireActivity())

        foodViewModel.foodData.observe(requireActivity()){ resultData ->
            when(resultData){
                is Result.Loading ->{
                    binding.pbSearchFood.visibility = View.VISIBLE
                }
                is Result.Success ->{
                    binding.pbSearchFood.visibility = View.GONE
                    val foodAdapter = FoodAdapter(resultData.data.foodData.menuItems){
                        builder.setTitle("Add to Diary?")
                            .setCancelable(true)
                            .setPositiveButton("Yes"){ _, _ ->
                                foodViewModel.addMeal(foodViewModel.label, it.title, 0, it.nutrition.calories, foodViewModel.date).observe(viewLifecycleOwner){ result ->
                                    when(result){
                                        is Result.Loading ->{
                                            binding.pbSearchFood.visibility = View.VISIBLE
                                        }
                                        is Result.Success ->{
                                            binding.pbSearchFood.visibility = View.GONE
                                            Toast.makeText(requireActivity(), result.data.message, Toast.LENGTH_SHORT).show()
                                            requireActivity().setResult(DiaryFragment.RESULT_OK, Intent())
                                            requireActivity().finish()
                                        }
                                        is Result.Error -> {
                                            binding.pbSearchFood.visibility = View.GONE
                                            Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                            .setNegativeButton("No"){dialogInterface, _, -> dialogInterface.cancel()}.show()
                    }
                    binding.rvSearchFood.apply {
                        val mLayoutManager = LinearLayoutManager(requireActivity())
                        addItemDecoration(DividerItemDecoration(requireActivity(), mLayoutManager.orientation))
                        layoutManager = mLayoutManager
                        adapter = foodAdapter
                    }
                }
                is Result.Error ->{
                    binding.pbSearchFood.visibility = View.GONE
                    Log.i("TEST", "onViewCreated: ${resultData.error}")
                    Toast.makeText(requireActivity(), resultData.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentFoodSearchBinding = null
    }
}