package moe.smoothie.androidide.themestore.util

import java.util.Locale

fun formatNumber(value: Long): String {
    val locale = Locale.US
    return when {
        value >= 1_000_000_000 -> String.format(locale, "%.1fB", value / 1_000_000_000.0)
        value >= 1_000_000 -> String.format(locale, "%.1fM", value / 1_000_000.0)
        value >= 1_000 -> String.format(locale , "%.1fK", value / 1_000.0)
        else -> value.toString()
    }
}
