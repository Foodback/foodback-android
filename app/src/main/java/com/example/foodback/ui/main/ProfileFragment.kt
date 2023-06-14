package com.example.foodback.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.foodback.data.Result
import com.example.foodback.data.remote.response.ProfileData
import com.example.foodback.databinding.FragmentProfileBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.edit.EditProfileActivity
import com.example.foodback.ui.login.LoginViewModel
import com.example.foodback.ui.onboarding.OnBoardingActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileFragment : Fragment() {

    private var _fragmentProfileBinding: FragmentProfileBinding? = null
    private val binding get() = _fragmentProfileBinding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private var date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private val mainViewModel: MainViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore, date)
    }
    private val loginViewModel: LoginViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }

    private lateinit var profileData: ProfileData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val launcherEditProfileActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                mainViewModel.getProfile()
            }
        }

        val builder = AlertDialog.Builder(requireActivity())

        mainViewModel.profileData.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Loading -> {
                    binding.pbProfile.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.pbProfile.visibility = View.GONE
                    profileData = result.data.profileData
                    binding.tvNameProfile.text = result.data.profileData.username
                    binding.tvEmailProfile.text = result.data.profileData.email
                    binding.tvGenderProfile.text = result.data.profileData.gender
                    binding.tvHeightProfile.text = "${result.data.profileData.height}"
                    binding.tvWeightProfile.text = "${result.data.profileData.weight}"
                    binding.tvActivityLevelProfile.text = result.data.profileData.activity
                    binding.tvGoalProfile.text = result.data.profileData.goal
                    binding.tvTargetProfile.text = result.data.profileData.target.toString()
                }
                is Result.Error -> {
                    binding.pbProfile.visibility = View.GONE
                    Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnLogout.setOnClickListener {
            builder
                .setTitle("Logout")
                .setMessage("Do you want to logout?")
                .setCancelable(true)
                .setPositiveButton("Yes"){_, _ ->
                    loginViewModel.logout().observe(viewLifecycleOwner) { result ->
                        when(result){
                            is Result.Loading -> {
                                binding.pbProfile.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.pbProfile.visibility = View.GONE
                                Toast.makeText(requireActivity(), result.data, Toast.LENGTH_SHORT).show()
                                startActivity(Intent(requireActivity(), OnBoardingActivity::class.java))
                                requireActivity().finish()
                            }
                            is Result.Error -> {
                                binding.pbProfile.visibility = View.GONE
                                Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }.setNegativeButton("No"){dialogInterface, _, -> dialogInterface.cancel()}
                .show()
        }

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(requireActivity(), EditProfileActivity::class.java)
            intent.putExtra(EditProfileActivity.EXTRA_PROFILE_DATA, profileData)
            launcherEditProfileActivity.launch(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentProfileBinding = null
    }

    companion object{
        const val RESULT_OK = 200
    }

}