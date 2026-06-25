package com.example.rent.core.utils

object FormValidators {
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

    fun isRequired(value: String): Boolean = value.trim().isNotEmpty()

    fun isValidEmail(value: String): Boolean = emailRegex.matches(value.trim())

    fun isValidPhone(value: String): Boolean {
        val digits = value.filter(Char::isDigit)
        return digits.length in 9..11
    }

    fun isValidEmailOrPhone(value: String): Boolean {
        val trimmedValue = value.trim()
        return isValidEmail(trimmedValue) || isValidPhone(trimmedValue)
    }

    fun isValidPassword(value: String): Boolean = value.length >= 6

    fun isValidPositiveAmount(value: String): Boolean = parseNumber(value)?.let { it > 0.0 } == true

    fun isValidNumber(value: String): Boolean = parseNumber(value) != null

    fun isValidNonNegativeNumber(value: String): Boolean = parseNumber(value)?.let { it >= 0.0 } == true

    fun isEndReadingValid(start: String, end: String): Boolean {
        val startValue = parseNumber(start) ?: return false
        val endValue = parseNumber(end) ?: return false
        return endValue >= startValue
    }

    fun parseNumber(value: String): Double? {
        val trimmedValue = value.trim()
        if (trimmedValue.isBlank()) return null

        val dotCount = trimmedValue.count { it == '.' }
        val commaCount = trimmedValue.count { it == ',' }
        val normalizedValue = when {
            dotCount > 0 && commaCount > 0 ->
                trimmedValue.replace(".", "").replace(",", ".")
            commaCount == 1 && dotCount == 0 ->
                trimmedValue.replace(",", ".")
            dotCount == 1 && commaCount == 0 && trimmedValue.substringAfter(".").length != 3 ->
                trimmedValue
            else -> trimmedValue.replace(".", "").replace(",", "")
        }

        return normalizedValue.toDoubleOrNull()
    }
}
