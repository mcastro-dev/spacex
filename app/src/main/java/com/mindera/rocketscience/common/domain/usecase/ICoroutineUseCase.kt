package com.mindera.rocketscience.common.domain.usecase

interface ICoroutineUseCase<P,R> {
    suspend operator fun invoke(params: P) : R
}