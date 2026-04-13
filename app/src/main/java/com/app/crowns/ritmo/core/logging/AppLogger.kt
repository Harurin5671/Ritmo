package com.app.crowns.ritmo.core.logging

interface AppLogger {
    fun d(message: String)
    fun i(message: String)
    fun w(message: String)
    fun e(throwable: Throwable? = null, message: String)
}