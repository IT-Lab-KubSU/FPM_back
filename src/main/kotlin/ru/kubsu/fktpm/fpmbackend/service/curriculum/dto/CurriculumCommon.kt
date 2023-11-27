package ru.kubsu.fktpm.fpmbackend.service.curriculum.dto

interface CurriculumCommon {
    val directionCode: String
    val department: String
    val plan: Map<String, Array<Subject>>
}