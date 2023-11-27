package ru.kubsu.fktpm.fpmbackend.service.curriculum.dto

data class Subject(
    val title: String,
    val exam: Boolean,
    val credit: Boolean,
    val hours: Int
) {
    constructor() : this("", false, false, 0)
}
