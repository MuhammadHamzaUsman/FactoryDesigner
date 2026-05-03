package util

import java.lang.Math.pow
import kotlin.math.pow
import kotlin.math.roundToInt

fun String?.toDoubleRoundedStringOrEmpty(roundUpTo: Int): String {
    val result = this?.toDoubleOrNull() ?: return ""

    val base = 10.0.pow(roundUpTo.toDouble())

    return "${(result * base).roundToInt() / base}"
}