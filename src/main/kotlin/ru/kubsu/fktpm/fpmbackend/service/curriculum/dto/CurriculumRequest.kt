package ru.kubsu.fktpm.fpmbackend.service.curriculum.dto

data class CurriculumRequest(
    override val department: String,
    override val directionCode: String,
    override val plan: Map<String, Array<Subject>>,
) : CurriculumCommon
