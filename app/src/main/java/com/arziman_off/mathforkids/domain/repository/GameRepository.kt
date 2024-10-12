package com.arziman_off.mathforkids.domain.repository

import com.arziman_off.mathforkids.domain.entity.GameSettings
import com.arziman_off.mathforkids.domain.entity.Level
import com.arziman_off.mathforkids.domain.entity.Question

interface GameRepository {
    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question
    fun getGameSettings(level: Level): GameSettings
}