package com.example.foodback.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.foodback.R
import com.example.foodback.databinding.FragmentStartBinding
import com.example.foodback.ui.onboarding.OnBoardingActivity

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.btnNextStarted.setOnClickListener {
            viewPager?.currentItem = 1
        }
        binding.tvMoveToLogin.setOnClickListener{
            startActivity(Intent(requireActivity(), OnBoardingActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}