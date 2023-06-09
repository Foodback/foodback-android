package com.example.foodback.ui.food

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.foodback.data.Result
import com.example.foodback.databinding.FragmentFoodFormBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.main.DiaryFragment
import com.example.foodback.ui.scan.ScanActivity

class FoodFormFragment : Fragment() {

    private var _fragmentFoodFormBinding: FragmentFoodFormBinding? = null
    private val binding get() = _fragmentFoodFormBinding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val foodViewModel: FoodViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _fragmentFoodFormBinding = FragmentFoodFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startCameraX()
            }else{
                Toast.makeText(requireActivity(), "Please allow permission on settings to use this features", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAddForm.setOnClickListener {
            val name = binding.edFormName.text.toString().trim()
            val amount = binding.edFormAmount.text.toString().trim()
            val calories = binding.edFormCalories.text.toString().trim()
            if(amount.isNotBlank() && amount.isNotBlank() && calories.isNotBlank()){
                foodViewModel.addMeal(foodViewModel.label, name, amount.toLong(), calories.toLong(), foodViewModel.date).observe(requireActivity()){ result ->
                    when(result){
                        is Result.Loading ->{
                            binding.pbFormFood.visibility = View.VISIBLE
                        }
                        is Result.Success ->{
                            binding.pbFormFood.visibility = View.GONE
                            Toast.makeText(requireActivity(), result.data.message, Toast.LENGTH_SHORT).show()
                            requireActivity().setResult(DiaryFragment.RESULT_OK, Intent())
                            requireActivity().finish()
                        }
                        is Result.Error ->{
                            binding.pbFormFood.visibility = View.GONE
                            Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Toast.makeText(requireActivity(), "Can't be blank", Toast.LENGTH_SHORT).show()
            }
        }


        binding.btnScanForm.setOnClickListener {
            if (checkPermission(Manifest.permission.CAMERA))  startCameraX()
            else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun checkPermission(permission: String) = ContextCompat.checkSelfPermission(requireActivity(), permission) == PackageManager.PERMISSION_GRANTED

    private fun startCameraX() {
        val intent = Intent(requireActivity(), ScanActivity::class.java)
        intent.putExtra(ScanActivity.EXTRA_LABEL, foodViewModel.label)
        intent.putExtra(ScanActivity.EXTRA_DATE, foodViewModel.date)
        startActivity(intent)
        requireActivity().finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        _fragmentFoodFormBinding = null
    }
}