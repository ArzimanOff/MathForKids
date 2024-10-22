package com.arziman_off.mathforkids.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.arziman_off.mathforkids.R
import com.arziman_off.mathforkids.domain.entity.GameResult


@BindingAdapter("resultEmoji")
fun bindResultEmoji(imgView: ImageView, gameResult: GameResult){
    val resource = if (gameResult.winner) {
        R.drawable.ic_win
    } else {
        R.drawable.ic_loose
    }
    imgView.setImageResource(resource)
}

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, cnt: Int){
    textView.text = String.format(
        textView.context.getString(R.string.required_score),
        cnt
    )
}

@BindingAdapter("scoredAnswers")
fun bindScoredAnswers(textView: TextView, cnt: Int){
    textView.text = String.format(
        textView.context.getString(R.string.score_answers),
        cnt
    )
}

@BindingAdapter("scoredPercentage")
fun bindScoredPercentage(textView: TextView, gameResult: GameResult){
    textView.text = String.format(
        textView.context.getString(R.string.score_percentage),
        calculatePercentOfRightAnswers(gameResult)
    )
}

@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(textView: TextView, gameResult: GameResult){
    textView.text = String.format(
        textView.context.getString(R.string.required_percentage),
        gameResult.gameSettings.minPercentOfRightAnswers
    )
}

private fun calculatePercentOfRightAnswers(gameResult: GameResult): Int {
    if (gameResult.countOfQuestions == 0) {
        return 0
    }
    return ((gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble()) * 100).toInt()
}

