package com.example.livedataandflow.string

fun String.defaultEmpty(default: String): String {
    ifEmpty { return default }
    return this
}

fun String?.default(default: String): String {
    return this ?: default
}