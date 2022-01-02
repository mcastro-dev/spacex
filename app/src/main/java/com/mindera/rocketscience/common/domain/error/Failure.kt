package com.mindera.rocketscience.common.domain.error

abstract class Failure(cause: Throwable? = null) : Exception(cause)