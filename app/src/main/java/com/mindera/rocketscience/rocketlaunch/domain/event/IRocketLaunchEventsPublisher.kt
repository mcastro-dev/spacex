package com.mindera.rocketscience.rocketlaunch.domain.event

import com.mindera.rocketscience.common.domain.event.IDomainEventsPublisher
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

interface IRocketLaunchEventsPublisher : IDomainEventsPublisher<IRocketLaunchEvent>

class RocketLaunchEventsPublisher(
    private val eventsSubject: PublishSubject<IRocketLaunchEvent>
) : IRocketLaunchEventsPublisher {

    override fun publish(event: IRocketLaunchEvent) {
        eventsSubject.onNext(event)
    }

    override fun events(): Flowable<IRocketLaunchEvent> {
        return eventsSubject
            .toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.computation())
    }
}