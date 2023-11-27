package ru.kubsu.fktpm.fpmbackend.service.news.dto

data class NewRequest(
    override val title: String,
    override val text: String,
    override val images: List<String>,
    override val status: Boolean,
) : NewCommon