package ru.kubsu.fktpm.fpmbackend.service.lead.dto

interface LeadCommon {
    val name: String
    val number: String
    val email: String
    val reason: LeadReasons
    val status: Boolean
}