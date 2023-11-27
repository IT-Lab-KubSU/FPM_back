package ru.kubsu.fktpm.fpmbackend.service.lead.dto

import ru.kubsu.fktpm.fpmbackend.dto.Entity
import ru.kubsu.fktpm.fpmbackend.dto.Time
import java.sql.Timestamp


data class Lead(
    override val id: Long,
    override val name: String,
    override val number: String,
    override val email: String,
    override val reason: LeadReasons,
    override val status: Boolean,
    override val createdAt: Timestamp,
    override val updatedAt: Timestamp
) : LeadCommon, Time, Entity