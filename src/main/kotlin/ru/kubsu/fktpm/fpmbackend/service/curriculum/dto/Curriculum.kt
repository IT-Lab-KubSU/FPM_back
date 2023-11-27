package ru.kubsu.fktpm.fpmbackend.service.curriculum.dto

import ru.kubsu.fktpm.fpmbackend.dto.Time
import java.sql.Timestamp

data class Curriculum(
    override val department: String,
    override val directionCode: String,
    override val plan: Map<String, Array<Subject>>,
    override val createdAt: Timestamp,
    override val updatedAt: Timestamp
) : CurriculumCommon, Time


