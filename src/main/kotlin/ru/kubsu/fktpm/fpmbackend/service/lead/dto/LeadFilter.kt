package ru.kubsu.fktpm.fpmbackend.service.lead.dto

import ru.kubsu.fktpm.fpmbackend.dto.SortOrder

data class LeadFilter(
    val limit: Int,
    val page: Int,
    val sortField: LeadSortField? = LeadSortField.ID,
    val sortOrder: SortOrder? = SortOrder.DESC,
    val search: String? = "",
    val status: Boolean?
)