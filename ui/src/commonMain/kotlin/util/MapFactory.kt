package util

fun <K, V> linkedHashMapOf(k: K, v: V) = LinkedHashMap<K, V>().apply {
    this[k] = v
}