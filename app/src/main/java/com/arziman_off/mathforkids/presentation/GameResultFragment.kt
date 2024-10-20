package com.arziman_off.mathforkids.presentation

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import com.arziman_off.mathforkids.R
import com.arziman_off.mathforkids.databinding.FragmentGameResultBinding
import com.arziman_off.mathforkids.domain.entity.GameResult

class GameResultFragment : Fragment() {
    private lateinit var gameResult: GameResult
    private var _binding: FragmentGameResultBinding? = null
    private val binding: FragmentGameResultBinding
        get() = _binding ?: throw RuntimeException("FragmentGameResultBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setGameResultInfo()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    retryGame()
                }
            }
        )

        binding.btnRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun setGameResultInfo() {
        val resource = if (gameResult.winner){
            R.drawable.ic_win
        } else {
            R.drawable.ic_loose
        }
        binding.emojiResult.setImageResource(resource)

        binding.tvRequiredAnswers.text = "Необходимое количество правильных ответов: " + gameResult.gameSettings.minCountOfRightAnswers.toString()

        binding.tvScoreAnswers.text = "Ваш счет: " + gameResult.countOfRightAnswers.toString()

        binding.tvRequiredPercentage.text = "Необходимый процент правильных ответов: " + gameResult.gameSettings.minPercentOfRightAnswers.toString()
        binding.tvScorePercentage.text = "Ваш процент правильных ответов:" + calculatePercentOfRightAnswers(gameResult)
    }

    private fun calculatePercentOfRightAnswers(gameResult: GameResult): Int {
        if (gameResult.countOfQuestions == 0) {
            return 0
        }
        return ((gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble()) * 100).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(KEY_GAME_RESULT, GameResult::class.java)?.let {
                gameResult = it
            }
        } else {
            requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
                gameResult = it
            }
        }
        Log.d(LOG_TAG, "Открыт экран результатов игры с аргументом gameResult = $gameResult")
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {
        const val NAME = "GameResultFragment"
        private const val LOG_TAG = "NEED_LOGS"
        private const val KEY_GAME_RESULT = "game_result"
        fun newInstance(gameResult: GameResult): GameResultFragment {
            return GameResultFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }
}
