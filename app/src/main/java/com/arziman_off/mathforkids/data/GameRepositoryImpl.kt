package com.arziman_off.mathforkids.data

import com.arziman_off.mathforkids.domain.entity.GameSettings
import com.arziman_off.mathforkids.domain.entity.Level
import com.arziman_off.mathforkids.domain.entity.Question
import com.arziman_off.mathforkids.domain.repository.GameRepository
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {

    private const val MIN_SUM_VALUE = 5
    private const val MIN_ANSWER_VALUE = 0

    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val randomGenerator = Random(System.currentTimeMillis())
        val sum = randomGenerator.nextInt(MIN_SUM_VALUE, maxSumValue + 1)
        val visibleNumber = randomGenerator.nextInt(MIN_ANSWER_VALUE, sum+1)
        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNumber
        options.add(rightAnswer)
        while (options.size < countOfOptions){
            options.add(randomGenerator.nextInt(MIN_ANSWER_VALUE, sum))
        }
        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when(level){
            Level.TEST -> {
                GameSettings(
                    10,
                    3,
                    80,
                    10
                )
            }
            Level.EASY -> {
                GameSettings(
                    30,
                    5,
                    60,
                    100
                )
            }
            Level.NORMAL -> {
                GameSettings(
                    60,
                    20,
                    80,
                    80
                )
            }
            Level.HARD -> {
                GameSettings(
                    200,
                    30,
                    95,
                    60
                )
            }
        }
    }
}