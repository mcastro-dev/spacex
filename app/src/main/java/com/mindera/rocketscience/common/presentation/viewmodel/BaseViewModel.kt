package com.mindera.rocketscience.common.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseViewModel<S : UIState> : ViewModel() {

    protected val _state = MutableLiveData<S>()
    protected val _command = PublishSubject.create<VMCommand>()

    val state : LiveData<S> = _state
    val command : Flowable<VMCommand> = _command.toFlowable(BackpressureStrategy.DROP)

    override fun onCleared() {
        _command.onComplete()
        super.onCleared()
    }

    protected suspend fun setState(state: S) {
        withContext(Dispatchers.Main) {
            _state.value = state
        }
    }

    protected fun emitCommand(command: VMCommand) {
        _command.onNext(command)
    }

    abstract fun onEvent(event: UIEvent)
}