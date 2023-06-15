package com.example.foodback.ui.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.foodback.R
import com.example.foodback.databinding.FragmentCurrentBinding
import com.example.foodback.ui.ViewModelFactory

class CurrentFragment : Fragment() {

    private var _binding: FragmentCurrentBinding? = null
    private val binding get() = _binding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val registerViewModel: RegisterViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }

    private var weight: String = ""
    private var height: String = ""
    private var weightDisable : Int = 0
    private var heightDisable : Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCurrentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
        binding.edWeight.doOnTextChanged { _, _, _, count ->
            weightDisable = if (count < 1) 0 else 1
            setValue()
            setButton()
        }
        binding.edHeight.doOnTextChanged { _, _, _, count ->
            heightDisable = if (count < 1) 0 else 1
            setValue()
            setButton()
        }

        binding.btnNextCurrent.setOnClickListener {
            registerViewModel.addData(HEIGHT_KEY, height)
            registerViewModel.addData(WEIGHT_KEY, weight)
            val goal: String? = registerViewModel.data[GOAL_KEY]
            if (goal == "maintain") navigateFragment(6)
             else navigateFragment(5)
        }

        binding.ivBack.setOnClickListener {
            navigateFragment(3)
        }
    }

    private fun navigateFragment(fragmentIndex: Int) {
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        viewPager?.currentItem = fragmentIndex
    }

    private fun setValue(){
        weight = binding.edWeight.text.toString()
        height = binding.edHeight.text.toString()
    }

    private fun setButton(){
        binding.btnNextCurrent.isEnabled = weightDisable == 1 && heightDisable == 1
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(HEIGHT_KEY, height)
        outState.putString(WEIGHT_KEY, weight)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val HEIGHT_KEY = "height"
        const val WEIGHT_KEY = "weight"
        const val GOAL_KEY = "goal"
    }
}