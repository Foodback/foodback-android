package com.example.foodback.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.foodback.data.Result
import com.example.foodback.databinding.FragmentProfileBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.auth.AuthViewModel
import com.example.foodback.ui.onboarding.OnBoardingActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val authViewModel: AuthViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logout.setOnClickListener {
            Log.i("TEST", "onViewCreated: BTN CLICKED")
            authViewModel.logout().observe(requireActivity()) { result ->
                when(result){
                    is Result.Loading -> {
                        Log.i("TEST", "LOADING..........")
                    }
                    is Result.Success -> {
                    Log.i("TEST", "SUCCESS ${result.data}")
                        startActivity(Intent(requireActivity(), OnBoardingActivity::class.java))
                        requireActivity().finish()
                    }
                    is Result.Error -> {
                    Log.i("TEST", "ERROR.......... ${result.error}")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}