package com.mindera.rocketscience.common.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mindera.rocketscience.CoroutineTest
import com.mindera.rocketscience.RxImmediateSchedulerRule
import com.mindera.rocketscience.home.itself.viewmodel.HomeState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class ViewModelBaseTest : CoroutineTest() {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxImmediateSchedulerRule = RxImmediateSchedulerRule()

    protected val states = mutableListOf<HomeState>()
    protected val commands = mutableListOf<VMCommand>()

    override fun setUp() {
        super.setUp()

        states.clear()
        commands.clear()
    }

    abstract fun observeStates()

    abstract fun observeCommands()
}