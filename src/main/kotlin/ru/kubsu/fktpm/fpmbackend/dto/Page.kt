package ru.kubsu.fktpm.fpmbackend.dto

data class Page<T>(
    val data: List<T>,
    val page: Int,
    val limit: Int,
    val totalPages: Int,
    val totalElements: Long
)