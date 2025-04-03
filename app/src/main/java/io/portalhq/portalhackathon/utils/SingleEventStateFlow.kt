package io.portalhq.portalhackathon.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow

class SingleEventStateFlow<T> : Flow<T> {
    private val sharedFlow = MutableSharedFlow<T>(replay = 0, extraBufferCapacity = 1)

    suspend fun emit(value: T) {
        sharedFlow.emit(value)
    }

    override suspend fun collect(collector: FlowCollector<T>) {
        sharedFlow.collect(collector)
    }

}