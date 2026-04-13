package com.app.crowns.ritmo.core.logging

import timber.log.Timber
import javax.inject.Inject

class TimberLogger @Inject constructor(): AppLogger {
    override fun d(message: String) {
        Timber.d(message)
    }

    override fun i(message: String) {
        Timber.i(message)
    }

    override fun w(message: String) {
        Timber.w(message)
    }

    override fun e(throwable: Throwable?, message: String) {
        Timber.e(throwable, message)
    }

}