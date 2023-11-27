package ru.kubsu.fktpm.fpmbackend.service.news.dto

import ru.kubsu.fktpm.fpmbackend.dto.Entity
import ru.kubsu.fktpm.fpmbackend.dto.Time
import java.sql.Timestamp

data class New(
    override val id: Long,
    override val title: String,
    override val text: String,
    override val images: List<String>,
    override val status: Boolean,
    override val createdAt: Timestamp,
    override val updatedAt: Timestamp
) : NewCommon, Time, Entity