package com.mindera.rocketscience.common.domain.event

import io.reactivex.rxjava3.core.Flowable

interface IDomainEventsPublisher<T : IDomainEvent> {
    fun publish(event: T)
    fun events(): Flowable<T>
}