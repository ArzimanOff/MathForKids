package com.arziman_off.mathforkids.domain.usecases

import com.arziman_off.mathforkids.domain.entity.GameSettings
import com.arziman_off.mathforkids.domain.entity.Level
import com.arziman_off.mathforkids.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(level: Level): GameSettings{
        return repository.getGameSettings(level)
    }
}