package com.example.foodback.ui.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.example.foodback.R
import com.example.foodback.data.Result
import com.example.foodback.databinding.FragmentRegisterBinding
import com.example.foodback.ui.ViewModelFactory

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val authViewModel: AuthViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        playAnimation()
    }

    private fun setupAction(){
        binding.btnRegister.setOnClickListener {
            val email = binding.edEmailRegister.text.toString().trim()
            val password = binding.edPasswordRegister.text.toString()
            Log.i("TEST", email)
            Log.i("TEST", password)
            authViewModel.register(email, password).observe(requireActivity()){ result ->
                when(result){
                    is Result.Loading ->{
                        Log.i("TEST", "LOADING..........")
                    }
                    is Result.Success ->{
                        Log.i("TEST", "SUCCESS..........")
                    }
                    is Result.Error ->{
                        Log.i("TEST", "ERROR.......... ${result.error}")
                    }
                }
            }
        }

        binding.btnMoveToLogin.setOnClickListener {
            val mLoginFragment = LoginFragment()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.popBackStack()
            mFragmentManager.commit {
                replace(R.id.frame_container, mLoginFragment, LoginFragment::class.java.simpleName)
            }
        }
    }

    private fun playAnimation(){

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}