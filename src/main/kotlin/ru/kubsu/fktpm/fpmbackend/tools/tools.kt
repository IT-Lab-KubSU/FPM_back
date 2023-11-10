package ru.kubsu.fktpm.fpmbackend.tools

fun camelToSnake(input: String): String {
    val result = StringBuilder()

    for (char in input) {
        if (char.isUpperCase()) {
            result.append('_').append(char.toLowerCase())
        } else {
            result.append(char)
        }
    }

    return result.toString()
}