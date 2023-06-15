package com.example.foodback.ui.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.foodback.R
import com.example.foodback.databinding.FragmentLevelBinding
import com.example.foodback.ui.ViewModelFactory

class LevelFragment : Fragment() {

    private var _binding: FragmentLevelBinding? = null
    private val binding get() = _binding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val registerViewModel: RegisterViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val goal: String? = registerViewModel.data[CurrentFragment.GOAL_KEY]

        binding.ivBack.setOnClickListener {
            if (goal == "maintain") {
                navigateFragment(4)
            } else {
                navigateFragment(5)
            }
        }

        binding.btnNextLevel.setOnClickListener {
            navigateFragment(7)
        }

        val fragmentList = arrayListOf(
            LightFragment(),
            ModerateFragment(),
            ActiveFragment(),
            VeryActiveFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            childFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager)
    }

    private fun navigateFragment(fragmentIndex: Int) {
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        viewPager?.currentItem = fragmentIndex
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}