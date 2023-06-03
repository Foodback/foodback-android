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
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.example.foodback.R
import com.example.foodback.databinding.FragmentGoalBinding
import com.example.foodback.ui.ViewModelFactory
import com.example.foodback.ui.login.LoginActivity

class GoalFragment : Fragment() {

    private var _binding: FragmentGoalBinding? = null
    private val binding get() = _binding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val registerViewModel: RegisterViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().dataStore)
    }

    private var goal: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if (savedInstanceState != null) goal = savedInstanceState.getString(goal, "")
        _binding = FragmentGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(goal){
            "Gain" -> { binding.cardGain.setCardBackgroundColor(requireActivity().getColor(R.color.green_400)) }
            "Maintain" -> { binding.cardMaintain.setCardBackgroundColor(requireActivity().getColor(R.color.green_400)) }
            "Loss" -> { binding.cardLoss.setCardBackgroundColor(requireActivity().getColor(R.color.green_400)) }
        }

        binding.cardGain.setOnClickListener {
            goal = "Gain"
            binding.cardGain.setCardBackgroundColor(requireActivity().getColor(R.color.green_400))
            binding.cardMaintain.setCardBackgroundColor(requireActivity().getColor(R.color.white))
            binding.cardLoss.setCardBackgroundColor(requireActivity().getColor(R.color.white))
        }

        binding.cardMaintain.setOnClickListener  {
            goal = "Maintain"
            binding.cardGain.setCardBackgroundColor(requireActivity().getColor(R.color.white))
            binding.cardMaintain.setCardBackgroundColor(requireActivity().getColor(R.color.green_400))
            binding.cardLoss.setCardBackgroundColor(requireActivity().getColor(R.color.white))
        }

        binding.cardLoss.setOnClickListener  {
            goal = "Loss"
            binding.cardGain.setCardBackgroundColor(requireActivity().getColor(R.color.white))
            binding.cardMaintain.setCardBackgroundColor(requireActivity().getColor(R.color.white))
            binding.cardLoss.setCardBackgroundColor(requireActivity().getColor(R.color.green_400))
        }

        binding.btnNextGoal.setOnClickListener {
            if (goal.isEmpty()){
                Toast.makeText(requireActivity(), "Please select your goal", Toast.LENGTH_SHORT).show()
            }else{
                registerViewModel.addData(GOAL_KEY, goal)
                if(goal == "Maintain") navigateFragment(LevelFragment())
                else navigateFragment(TargetFragment())
            }
        }

        binding.tvMoveToLogin.setOnClickListener{
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    fun navigateFragment(mFragment: Fragment) {
        mFragment.apply {
            enterTransition = Slide(Gravity.END)
            exitTransition = Slide(Gravity.START)
        }
        val mFragmentManager = parentFragmentManager
        mFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.frame_register, mFragment, mFragment::class.java.simpleName)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(GOAL_KEY, goal)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        private const val GOAL_KEY: String = "goal"
    }
}