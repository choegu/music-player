package com.choegozip.domain.usecase

interface ReleaseMediaControllerUseCase {
    suspend operator fun invoke(): Result<Unit>
}