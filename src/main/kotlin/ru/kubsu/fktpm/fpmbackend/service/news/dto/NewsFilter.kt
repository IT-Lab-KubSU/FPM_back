package ru.kubsu.fktpm.fpmbackend.service.news.dto

import ru.kubsu.fktpm.fpmbackend.dto.SortOrder

data class NewsFilter(
    val limit: Int,
    val page: Int,
    val sortField: NewSortField? = NewSortField.ID,
    val sortOrder: SortOrder? = SortOrder.DESC,
    val search: String? = ""
)