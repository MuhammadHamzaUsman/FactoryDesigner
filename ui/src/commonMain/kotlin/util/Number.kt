package util

import kotlin.math.pow
import kotlin.math.roundToInt

fun String?.toDoubleRoundedStringOrEmpty(roundUpTo: Int): String {
    val result = this?.toDoubleOrNull() ?: return ""

    val base = 10.0.pow(roundUpTo.toDouble())

    return "${(result * base).roundToInt() / base}"
}

fun Double.round(roundUpTo: Int): Double{
    val base = 10.0.pow(roundUpTo.toDouble())

    return (this * base).roundToInt() / base
}