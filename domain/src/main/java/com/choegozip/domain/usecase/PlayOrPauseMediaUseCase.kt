package com.choegozip.domain.usecase

interface PlayOrPauseMediaUseCase {
    suspend operator fun invoke(): Result<Unit>
}