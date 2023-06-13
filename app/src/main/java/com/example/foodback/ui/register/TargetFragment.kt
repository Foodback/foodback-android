package com.example.foodback.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
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
import com.example.foodback.databinding.FragmentTargetBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.login.LoginActivity

class TargetFragment : Fragment() {

    private var _binding: FragmentTargetBinding? = null
    private val binding get() = _binding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val registerViewModel: RegisterViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTargetBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var target: String = ""
    private var targetDisable : Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
        binding.edWeight.doOnTextChanged { text, start, before, count ->
            targetDisable = if (count < 1) 0 else 1
            setValue()
            setButton()
        }

        binding.ivBack.setOnClickListener {
            navigateFragment(3)
        }

        binding.btnNextTarget.setOnClickListener {
            registerViewModel.addData(TARGET_KEY, target)
            navigateFragment(5)
        }
    }

    private fun navigateFragment(fragmentIndex: Int) {
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        viewPager?.currentItem = fragmentIndex
    }

    private fun setValue(){
        target = binding.edWeight.text.toString()
    }

    private fun setButton(){
        binding.btnNextTarget.isEnabled = targetDisable == 1
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TARGET_KEY, target)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val TARGET_KEY = "target"
    }
}