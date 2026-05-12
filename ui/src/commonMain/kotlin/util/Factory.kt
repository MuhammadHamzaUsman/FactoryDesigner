package util

import androidx.compose.ui.geometry.Offset
import kotlin.collections.forEach

fun <K, V> linkedHashMapOf(k: K, v: V) = LinkedHashMap<K, V>().apply {
    this[k] = v
}
fun List<Offset>.deepCopy(): MutableList<Offset>{
    val list = mutableListOf<Offset>()

    forEach { list.add(it) }

    return list
}