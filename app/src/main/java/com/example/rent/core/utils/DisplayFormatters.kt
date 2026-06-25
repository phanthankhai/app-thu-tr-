package com.example.rent.core.utils

object DisplayFormatters {
    fun currencyDisplay(value: String): String = value.trim()

    fun roomCapacity(current: Int, max: Int): String = "$current/$max nguoi"

    fun roomCode(code: String): String = code.trim().uppercase()
}
