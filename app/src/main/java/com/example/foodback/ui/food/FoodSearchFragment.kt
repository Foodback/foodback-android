package com.example.foodback.ui.food

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.foodback.adapter.FoodAdapter
import com.example.foodback.data.Result
import com.example.foodback.databinding.FragmentFoodSearchBinding
import com.example.foodback.ui.ViewModelFactory
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _fragmentFoodSearchBinding = FragmentFoodSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foodViewModel.getFood("")

        val dialog = Dialog(requireActivity())
        dialog.apply {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_food)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        val btnYes = dialog.findViewById<Button>(R.id.alert_yes_food)
        val btnNo = dialog.findViewById<Button>(R.id.alert_no_food)
        val edAmount = dialog.findViewById<EditText>(R.id.ed_amount_alert)

        foodViewModel.foodData.observe(requireActivity()){ resultData ->
            when(resultData){
                is Result.Loading ->{
                    binding.pbSearchFood.visibility = View.VISIBLE
                }
                is Result.Success ->{
                    binding.pbSearchFood.visibility = View.GONE
                    val foodAdapter = FoodAdapter(resultData.data.foodData.menuItems){ food ->
                        btnYes.setOnClickListener {
                            val amount = edAmount.text.toString()
                            if(amount.isNotBlank()){
                                foodViewModel.addMeal(foodViewModel.label, food.title, amount.toLong(), food.nutrition.calories.toLong(), foodViewModel.date).observe(viewLifecycleOwner){ result ->
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
                            }else{ Toast.makeText(requireActivity(), "Can't be blank", Toast.LENGTH_SHORT).show() }
                        }
                        btnNo.setOnClickListener {
                            dialog.dismiss()
                        }
                        dialog.show()
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