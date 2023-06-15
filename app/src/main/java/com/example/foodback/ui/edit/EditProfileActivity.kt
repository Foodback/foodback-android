package com.example.foodback.ui.edit

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.foodback.R
import com.example.foodback.data.Result
import com.example.foodback.data.remote.response.ProfileData
import com.example.foodback.databinding.ActivityEditProfileBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.main.ProfileFragment

class EditProfileActivity : AppCompatActivity() {

    private var _activityEditProfileBinding: ActivityEditProfileBinding? = null
    private val binding get() = _activityEditProfileBinding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val editProfileViewModel: EditProfileViewModel by viewModels {
        ViewModelFactory.getInstance(dataStore)
    }

    private var gender = ""
    private var activity = ""
    private var goal = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityEditProfileBinding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val profileData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_PROFILE_DATA, ProfileData::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_PROFILE_DATA)
        }

        binding.edUsernameEdit.setText(profileData?.username)
        binding.edEmailEdit.setText(profileData?.email)
        binding.edHeightEdit.setText(profileData?.height.toString())
        binding.edWeightEdit.setText(profileData?.weight.toString())
        binding.edTargetEdit.setText(profileData?.target.toString())
        binding.edAgeEdit.setText(profileData?.age.toString())
        binding.autoCompleteGender.setText(profileData?.gender.toString())
        binding.autoCompleteActivity.setText(profileData?.activity.toString())
        binding.autoCompleteGoal.setText(profileData?.goal.toString())

        gender = profileData?.gender.toString()
        activity = profileData?.activity.toString()
        goal = profileData?.goal.toString()

        val adapterGender = ArrayAdapter(this@EditProfileActivity, R.layout.item_dropdown, listOf("male", "female"))
        val adapterActivity = ArrayAdapter(this@EditProfileActivity, R.layout.item_dropdown, listOf("light", "moderate", "active","very active"))
        val adapterGoal = ArrayAdapter(this@EditProfileActivity, R.layout.item_dropdown, listOf("loss", "maintain", "gain"))

        binding.autoCompleteGender.apply {
            setAdapter(adapterGender)
            onItemClickListener = object: AdapterView.OnItemClickListener{
                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    gender = parent?.getItemAtPosition(position).toString()
                }
            }
        }

        binding.autoCompleteActivity.apply {
            setAdapter(adapterActivity)
            onItemClickListener = object: AdapterView.OnItemClickListener{
                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    activity = parent?.getItemAtPosition(position).toString()
                }
            }
        }
        binding.autoCompleteGoal.apply {
            setAdapter(adapterGoal)
            onItemClickListener = object: AdapterView.OnItemClickListener{
                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    goal = parent?.getItemAtPosition(position).toString()
                }
            }
        }

        binding.btnSaveEdit.setOnClickListener {
            val name = binding.edUsernameEdit.text.toString().trim()
            val age = binding.edAgeEdit.text.toString().trim()
            val email =  binding.edEmailEdit.text.toString().trim()
            val height  = binding.edHeightEdit.text.toString().trim()
            val weight = binding.edWeightEdit.text.toString().trim()
            val target = binding.edTargetEdit.text.toString().trim()
            if(name .isNotBlank() && email.isNotBlank() && gender.isNotBlank() && height.isNotBlank() && weight.isNotBlank() && activity.isNotBlank() && goal.isNotBlank() && target.isNotBlank()){
                editProfileViewModel.editProfile(name, email, gender, age.toLong(), height.toLong(), weight.toLong(), activity, goal, target.toLong()).observe(this){ result ->
                    when(result){
                        is Result.Loading ->{
                            binding.pbEditProfile.visibility = View.VISIBLE
                        }
                        is Result.Success ->{
                            binding.pbEditProfile.visibility = View.GONE
                            Toast.makeText(this@EditProfileActivity, result.data.message, Toast.LENGTH_SHORT).show()
                            setResult(ProfileFragment.RESULT_OK, Intent())
                            finish()
                        }
                        is Result.Error ->{
                            binding.pbEditProfile.visibility = View.GONE
                            Toast.makeText(this@EditProfileActivity, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Toast.makeText(this@EditProfileActivity, "Can't be blank", Toast.LENGTH_SHORT).show()
            }

        }

        binding.ivBack.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityEditProfileBinding = null
    }

    companion object{
        const val EXTRA_PROFILE_DATA = "extra_profile_data"
    }
}