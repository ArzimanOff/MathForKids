package com.arziman_off.mathforkids.domain.entity

import java.io.Serializable

data class GameSettings(
    val maxSumValue: Int,
    val minCountOfRightAnswers: Int,
    val minPercentOfRightAnswers: Int,
    val gameTimeLimitInSeconds: Int
):Serializable