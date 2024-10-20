package com.arziman_off.mathforkids.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.arziman_off.mathforkids.R
import com.arziman_off.mathforkids.databinding.FragmentStartGameBinding
import com.arziman_off.mathforkids.domain.entity.Level
import com.arziman_off.mathforkids.presentation.GameFragment.Companion.KEY_LEVEL

class StartGameFragment : Fragment() {

    private var _binding: FragmentStartGameBinding? = null
    private val binding: FragmentStartGameBinding
        get() = _binding ?: throw RuntimeException("FragmentWelcomeBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
    }

    private fun initClickListeners() {
        with(binding) {
            btnLevelTest.setOnClickListener {
                launchGameFragment(Level.TEST)
            }
            btnLevelEasy.setOnClickListener {
                launchGameFragment(Level.EASY)
            }
            btnLevelNormal.setOnClickListener {
                launchGameFragment(Level.NORMAL)
            }
            btnLevelHard.setOnClickListener {
                launchGameFragment(Level.HARD)
            }
        }
    }

    private fun launchGameFragment(level: Level) {
        val args = Bundle().apply {
            putParcelable(KEY_LEVEL, level)
        }
        findNavController().navigate(R.id.action_startGameFragment_to_gameFragment, args)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val NAME = "StartGameFragment"
        fun newInstance(): StartGameFragment {
            return StartGameFragment()
        }
    }
}