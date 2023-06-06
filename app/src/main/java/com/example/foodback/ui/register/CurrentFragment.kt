package com.example.foodback.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.viewpager2.widget.ViewPager2
import com.example.foodback.R
import com.example.foodback.databinding.FragmentCurrentBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.login.LoginActivity

class CurrentFragment() : Fragment() {

    private var _binding: FragmentCurrentBinding? = null
    private val binding get() = _binding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val registerViewModel: RegisterViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }

    private var weight: String = ""

    private var height: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCurrentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val height = binding.edHeight.text.toString().trim()
        val weight = binding.edWeight.text.toString()
        binding.btnNextCurrent.setOnClickListener {
            registerViewModel.addData(EXTRA_HEIGHT, height)
            registerViewModel.addData(EXTRA_WEIGHT, weight)
            val goal: String? = registerViewModel.data[GOAL_KEY]
            if (goal == "maintain") {
                navigateFragment(5)
            } else {
                navigateFragment(4)
            }
        }

        binding.ivBack.setOnClickListener {
            navigateFragment(2)
        }
    }

    private fun navigateFragment(fragmentIndex: Int) {
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        viewPager?.currentItem = fragmentIndex
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_HEIGHT, height)
        outState.putString(EXTRA_WEIGHT, weight)
    }

    private fun setButton(){

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        var EXTRA_HEIGHT = "height"
        var EXTRA_WEIGHT = "extra_weight"
        var GOAL_KEY = "goal"
    }
}