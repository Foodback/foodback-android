package com.example.foodback.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import com.example.foodback.R
import com.example.foodback.data.Result
import com.example.foodback.databinding.FragmentRegisterBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.login.LoginActivity

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val registerViewModel: RegisterViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val email = binding.edEmailRegister.text.toString().trim()
            val password = binding.edPasswordRegister.text.toString()
            val name = binding.edNameRegister.text.toString().trim()
            registerViewModel.addData("name", name)
            registerViewModel.register(email, password).observe(requireActivity()){ result ->
                when(result){
                    is Result.Loading ->{
                        binding.pbRegister.visibility = View.VISIBLE
                    }
                    is Result.Success ->{
                        binding.pbRegister.visibility = View.GONE
                        Toast.makeText(requireActivity(), "Register success, please login", Toast.LENGTH_SHORT).show()
                        Log.i("TEST", registerViewModel.data.entries.toString())
                        startActivity(Intent(requireActivity(), LoginActivity::class.java))
                        requireActivity().finish()
                    }
                    is Result.Error ->{
                        binding.pbRegister.visibility = View.GONE
                        Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.tvMoveToLogin.setOnClickListener{
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}