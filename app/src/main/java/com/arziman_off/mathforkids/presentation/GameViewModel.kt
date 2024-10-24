package com.arziman_off.mathforkids.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arziman_off.mathforkids.R
import com.arziman_off.mathforkids.data.GameRepositoryImpl
import com.arziman_off.mathforkids.domain.entity.GameResult
import com.arziman_off.mathforkids.domain.entity.GameSettings
import com.arziman_off.mathforkids.domain.entity.Level
import com.arziman_off.mathforkids.domain.entity.Question
import com.arziman_off.mathforkids.domain.usecases.GenerateQuestionUseCase
import com.arziman_off.mathforkids.domain.usecases.GetGameSettingsUseCase

class GameViewModel(
    private val application: Application,
    private val level: Level
) : ViewModel() {
    private lateinit var gameSettings: GameSettings

    private val repository = GameRepositoryImpl

    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)

    private var timer: CountDownTimer? = null

    private var cntOfRightAnswers = 0
    private var cntOfQuestions = 0


    private val _formatedTimeLimit = MutableLiveData<String>()
    val formatedTimeLimit: LiveData<String>
        get() = _formatedTimeLimit

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _progressBarStat = MutableLiveData<String>()
    val progressBarStat: LiveData<String>
        get() = _progressBarStat

    private val _stat = MutableLiveData<String>()
    val stat: LiveData<String>
        get() = _stat

    private val _enoughAnswers = MutableLiveData<Boolean>()
    val enoughAnswers: LiveData<Boolean>
        get() = _enoughAnswers

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    init {
        startGame()
    }

    private fun startGame() {
        getGameSettings()
        startTimer()
        generateQuestion()
        updateProgress()
        //_percentOfRightAnswers.value = FULL_PERCENT
    }

    private fun getGameSettings() {
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gameTimeLimitInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formatedTimeLimit.value = (millisUntilFinished / MILLIS_IN_SECONDS).toString()
            }

            override fun onFinish() {
                finishGame()
            }
        }

        timer?.start()
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentOfRightAnswers.value = percent
        _enoughAnswers.value = cntOfRightAnswers >= gameSettings.minCountOfRightAnswers
        _enoughPercent.value = percent >= gameSettings.minPercentOfRightAnswers

        _stat.value = String.format(
            application.resources.getString(R.string.stat),
            cntOfRightAnswers,
            gameSettings.minCountOfRightAnswers
        )

        _progressBarStat.value = String.format(
            application.resources.getString(R.string.progress_bar_stat),
            percentOfRightAnswers.value,
            application.resources.getString(R.string.percent),
            minPercent.value,
            application.resources.getString(R.string.percent)
        )
    }

    private fun calculatePercentOfRightAnswers(): Int {
        if (cntOfQuestions == 0) {
            return 0
        }
        return ((cntOfRightAnswers / cntOfQuestions.toDouble()) * FULL_PERCENT).toInt()
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAns
        if (number == rightAnswer) {
            cntOfRightAnswers++
        }
        cntOfQuestions++
    }

    fun finishGame() {
        _gameResult.value = GameResult(
            winner = enoughPercent.value == true && enoughAnswers.value == true,
            countOfRightAnswers = cntOfRightAnswers,
            countOfQuestions = cntOfQuestions,
            gameSettings = gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val FULL_PERCENT = 100
    }

}