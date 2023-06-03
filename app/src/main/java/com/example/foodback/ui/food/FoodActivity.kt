package com.example.foodback.ui.food

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodback.adapter.AddAdapter
import com.example.foodback.data.FakeDataSource
import com.example.foodback.databinding.ActivityFoodBinding
import com.example.foodback.ui.scan.ScanActivity

class FoodActivity : AppCompatActivity() {

    private var _activityFoodBinding: ActivityFoodBinding? = null
    private val binding get() = _activityFoodBinding!!

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            startCameraX()
        }else{
            Toast.makeText(this, "Please allow permission on settings to use this features", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityFoodBinding = ActivityFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.searchFood.apply {
            setIconifiedByDefault(false)
            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if(query != null){
                        Toast.makeText(this@FoodActivity, "Searching $query", Toast.LENGTH_SHORT).show()
                        binding.searchFood.clearFocus()
                    }else{
                        Toast.makeText(this@FoodActivity, "Reset search filter", Toast.LENGTH_SHORT).show()
                        binding.searchFood.clearFocus()
                    }
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        binding.rvFood.apply {
            val addAdapter = AddAdapter(FakeDataSource.dummyAdd){Toast.makeText(this@FoodActivity, it.name, Toast.LENGTH_SHORT).show()}
            val mLayoutManager = LinearLayoutManager(this@FoodActivity)
            addItemDecoration(DividerItemDecoration(this@FoodActivity, mLayoutManager.orientation))
            layoutManager = mLayoutManager
            adapter = addAdapter
        }

        binding.btnScan.setOnClickListener {
            if (checkPermission(Manifest.permission.CAMERA))  startCameraX()
            else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun checkPermission(permission: String) = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    private fun startCameraX() {
        startActivity(Intent(this, ScanActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFoodBinding = null
    }
}