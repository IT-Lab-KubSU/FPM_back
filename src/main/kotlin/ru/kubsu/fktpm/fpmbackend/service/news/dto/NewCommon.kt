package ru.kubsu.fktpm.fpmbackend.service.news.dto

interface NewCommon {
    val title: String
    val text: String
    val images: List<String>
    val status: Boolean
}