package com.arziman_off.mathforkids.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arziman_off.mathforkids.R
import com.arziman_off.mathforkids.databinding.FragmentGameResultBinding
import com.arziman_off.mathforkids.domain.entity.GameResult

class GameResultFragment : Fragment() {
    private val args by navArgs<GameResultFragmentArgs>()
    private val gameResult
        get() = args.gameResult
    private var _binding: FragmentGameResultBinding? = null
    private val binding: FragmentGameResultBinding
        get() = _binding ?: throw RuntimeException("FragmentGameResultBinding == null")


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
        setEventListeners()
    }

    private fun setEventListeners() {
        binding.btnRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun setGameResultInfo() {
        val resource = if (gameResult.winner) {
            R.drawable.ic_win
        } else {
            R.drawable.ic_loose
        }
        with(binding) {
            emojiResult.setImageResource(resource)
            tvRequiredAnswers.text = String.format(
                getString(R.string.required_score),
                gameResult.gameSettings.minCountOfRightAnswers
            )

            tvScoreAnswers.text = String.format(
                getString(R.string.score_answers),
                gameResult.countOfRightAnswers
            )

            tvScorePercentage.text = String.format(
                getString(R.string.score_percentage),
                calculatePercentOfRightAnswers(gameResult)
            )

            tvRequiredPercentage.text = String.format(
                getString(R.string.required_percentage),
                gameResult.gameSettings.minPercentOfRightAnswers
            )
        }

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

    private fun retryGame() {
        findNavController().popBackStack()
    }
}
