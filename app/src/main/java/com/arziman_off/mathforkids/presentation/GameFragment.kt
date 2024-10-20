package com.arziman_off.mathforkids.presentation

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.arziman_off.mathforkids.R
import com.arziman_off.mathforkids.databinding.FragmentGameBinding
import com.arziman_off.mathforkids.domain.entity.GameResult
import com.arziman_off.mathforkids.domain.entity.Level


class GameFragment : Fragment() {

    private lateinit var level: Level
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            AndroidViewModelFactory.getInstance(requireActivity().application)
        )[GameViewModel::class.java]
    }
    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
    }

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModelObservers()
        setEventListeners()
        viewModel.startGame(level)
    }

    private fun setViewModelObservers() {
        viewModel.question.observe(viewLifecycleOwner){
            binding.ans.text = it.sum.toString()
            binding.visibleNum.text = it.visibleNumber.toString()
            for (i in 0 until tvOptions.size){
                tvOptions[i].text = it.options[i].toString()
            }
        }
        viewModel.percentOfRightAnswers.observe(viewLifecycleOwner){
            binding.progressBar.setProgress(it, true)
        }
        viewModel.stat.observe(viewLifecycleOwner){
            binding.answersStat.text = it
        }
        viewModel.formatedTimeLimit.observe(viewLifecycleOwner) {
            binding.timerText.text = it
        }
        viewModel.progressBarStat.observe(viewLifecycleOwner){
            binding.progressBarStatText.text = it
        }
        viewModel.gameResult.observe(viewLifecycleOwner){
            launchGameResultFragment(it)
        }
    }

    private fun setEventListeners() {
        for(tvOption in tvOptions){
            tvOption.setOnClickListener {
                viewModel.chooseAnswer(tvOption.text.toString().toInt())
            }
        }
    }

    private fun launchGameResultFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameResultFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(KEY_LEVEL, Level::class.java)?.let {
                level = it
            }
        } else {
            requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
                level = it
            }
        }
        Log.d(LOG_TAG, "Открыт экран игры с аргументом Level = $level")
    }

    companion object {
        const val NAME = "GameFragment"
        private const val LOG_TAG = "NEED_LOGS"
        private const val KEY_LEVEL = "level"
        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}