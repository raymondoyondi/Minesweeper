package com.raymondoyondi.minesweeper

import com.raymondoyondi.decompose.Cancellation
import com.raymondoyondi.decompose.value.Value
import com.raymondoyondi.mvikotlin.core.rx.observer
import com.raymondoyondi.mvikotlin.core.store.Store

internal fun <T : Any> Store<*, T, *>.asValue(): Value<T> =
    object : Value<T>() {
        override val value: T get() = state

        override fun subscribe(observer: (T) -> Unit): Cancellation {
            val disposable = states(observer(onNext = observer))
            return Cancellation(disposable::dispose)
        }
    }

internal inline fun <T> T.runUnless(condition: Boolean, block: T.() -> T): T =
    if (condition) {
        this
    } else {
        block()
    }
