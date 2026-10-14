package com.example.core.utils

import java.text.NumberFormat
import java.util.Locale

object Formatters {
    private val formatter = NumberFormat.getNumberInstance(Locale.US).apply {
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }

    fun formatPkr(amount: Double): String {
        return "Rs. ${formatter.format(amount)}"
    }

    fun formatCompactPkr(amount: Double): String {
        return when {
            amount >= 100000 -> "Rs. ${String.format(Locale.US, "%.1f", amount / 100000)} Lakh"
            amount >= 1000 -> "Rs. ${String.format(Locale.US, "%.1f", amount / 1000)}k"
            else -> formatPkr(amount)
        }
    }
}
