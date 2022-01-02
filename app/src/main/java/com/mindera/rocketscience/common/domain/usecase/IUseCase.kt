package com.mindera.rocketscience.common.domain.usecase

interface IUseCase<P,R> {
    operator fun invoke(params: P) : R
}