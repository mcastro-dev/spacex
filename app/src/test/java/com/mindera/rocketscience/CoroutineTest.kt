package com.mindera.rocketscience

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before

@ExperimentalCoroutinesApi
abstract class CoroutineTest {

    protected lateinit var scheduler: TestCoroutineScheduler
    protected  lateinit var dispatcher: TestDispatcher

    @Before
    open fun setUp() {
        scheduler = TestCoroutineScheduler()
        dispatcher = StandardTestDispatcher(scheduler)
        Dispatchers.setMain(dispatcher)
    }

    @After
    open fun tearDown() {
        Dispatchers.resetMain()
    }
}
