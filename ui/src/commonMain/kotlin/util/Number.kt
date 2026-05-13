package util

import kotlin.math.pow
import kotlin.math.roundToInt

fun String.toDoubleRoundedStringOrEmpty(roundUpTo: Int): String {
    return toDoubleOrNull()?.toDoubleRoundedStringOrEmpty(roundUpTo) ?: return ""
}

fun Double?.toDoubleRoundedStringOrEmpty(roundUpTo: Int): String {
    val number = this ?: return ""

    return "%.${roundUpTo}f".format(number)
        .replace(Regex("0*$"), "") // Remove trailing zeros
        .replace(Regex("\\.$"), "")
}

fun Double.round(roundUpTo: Int): Double{
    val base = 10.0.pow(roundUpTo.toDouble())

    return (this * base).roundToInt() / base
}