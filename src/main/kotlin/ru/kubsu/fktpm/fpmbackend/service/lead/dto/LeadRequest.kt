package ru.kubsu.fktpm.fpmbackend.service.lead.dto

data class LeadRequest(
    override val name: String,
    override val number: String,
    override val email: String,
    override val reason: LeadReasons,
    override val status: Boolean,
) : LeadCommon