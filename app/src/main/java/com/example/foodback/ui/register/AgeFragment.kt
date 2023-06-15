package com.example.foodback.ui.register

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.foodback.R
import com.example.foodback.databinding.FragmentAgeBinding
import com.example.foodback.ui.ViewModelFactory
import java.util.Calendar

class AgeFragment : Fragment() {

    private var _binding: FragmentAgeBinding? = null
    private val binding get() = _binding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val registerViewModel: RegisterViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }

    private var age: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAgeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNextAge.isEnabled = false
        binding.ivBack.setOnClickListener {
            navigateFragment(2)
        }

        binding.btnNextAge.setOnClickListener {
            registerViewModel.addData(AGE_KEY,age)
            navigateFragment(4)
        }

        binding.dateBirth.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, monthOfYear, dayOfMonth)

            calculateAge(selectedDate.timeInMillis)
        }
    }

    private fun calculateAge(dateInMillis: Long): String {
        val dob = Calendar.getInstance()
        dob.timeInMillis = dateInMillis

        val today = Calendar.getInstance()

        val date = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        age = date.toString()

        binding.btnNextAge.isEnabled = date >= 1

        return age
    }

    private fun navigateFragment(fragmentIndex: Int) {
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        viewPager?.currentItem = fragmentIndex
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(AGE_KEY, age)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val AGE_KEY: String = "age"
    }
}
