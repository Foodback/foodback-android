package com.example.foodback.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.foodback.R
import com.example.foodback.databinding.FragmentGenderBinding
import com.example.foodback.databinding.FragmentStartBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.onboarding.OnBoardingActivity

class GenderFragment : Fragment() {

    private var _binding: FragmentGenderBinding? = null
    private val binding get() = _binding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val registerViewModel: RegisterViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }

    private var gender: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        // Inflate the layout for this fragment
        _binding = FragmentGenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButton()

        binding.btnNextGender.setOnClickListener {
            registerViewModel.addData(GENDER_KEY, gender)
            navigateFragment(3)
        }

        binding.male.setOnClickListener {
            gender = "male"
            binding.male.isChecked = !binding.male.isChecked
            binding.female.isChecked = false
            setColorBackground()
            setButton()
        }

        binding.female.setOnClickListener {
            gender = "female"
            binding.female.isChecked = !binding.female.isChecked
            binding.male.isChecked = false
            setColorBackground()
            setButton()
        }

        binding.ivBack.setOnClickListener {
            navigateFragment(1)
        }
    }

    private fun setColorBackground(){
        val primaryColor = ContextCompat.getColor(requireContext(), R.color.primary)
        val whiteColor = ContextCompat.getColor(requireContext(), R.color.white)
        val textColor = ContextCompat.getColor(requireContext(), R.color.second_text2)
        val maleAfter = ContextCompat.getDrawable(requireContext(), R.drawable.male_after)
        val male = ContextCompat.getDrawable(requireContext(), R.drawable.male)
        val femaleAfter = ContextCompat.getDrawable(requireContext(), R.drawable.female_after)
        val female = ContextCompat.getDrawable(requireContext(), R.drawable.female)

        binding.imageMale.setImageDrawable(if (binding.male.isChecked) maleAfter else male)
        binding.male.setCardBackgroundColor(if (binding.male.isChecked) primaryColor else whiteColor)
        binding.textMale.setTextColor(if (binding.male.isChecked) whiteColor else textColor)

        binding.imageFemale.setImageDrawable(if (binding.female.isChecked) femaleAfter else female)
        binding.female.setCardBackgroundColor(if (binding.female.isChecked) primaryColor else whiteColor)
        binding.textFemale.setTextColor(if (binding.female.isChecked) whiteColor else textColor)
    }

    private fun setButton(){
        binding.btnNextGender.isEnabled = binding.male.isChecked || binding.female.isChecked
    }

    private fun navigateFragment(fragmentIndex: Int) {
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        viewPager?.currentItem = fragmentIndex
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(GENDER_KEY, gender)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        private const val GENDER_KEY: String = "gender"
    }
}