package com.example.foodback.ui.register

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
import com.example.foodback.R
import com.example.foodback.ui.ViewModelFactory

class LightFragment : Fragment() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val registerViewModel: RegisterViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }
    private var level: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        level = "light"
        val bundle = Bundle()
        bundle.putString(LEVEL_KEY, level)
        registerViewModel.addData(LEVEL_KEY, level)
        return inflater.inflate(R.layout.fragment_light, container, false)
    }

    companion object{
        var LEVEL_KEY = "level"
    }
}