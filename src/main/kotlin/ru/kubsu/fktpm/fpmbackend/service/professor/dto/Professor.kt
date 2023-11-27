package ru.kubsu.fktpm.fpmbackend.service.professor.dto

import ru.kubsu.fktpm.fpmbackend.dto.Entity
import ru.kubsu.fktpm.fpmbackend.dto.Gender
import ru.kubsu.fktpm.fpmbackend.dto.Time
import java.sql.Timestamp
import java.util.*

data class Professor(
    override val id: Long,
    override val name: String,
    override val surname: String,
    override val patronymic: String,
    override val birthDate: Date,
    override val gender: Gender,
    override val contactNumber: String,
    override val email: String,
    override val address: String,
    override val employmentStartDate: Date,
    override val academicDegree: String,
    override val academicRank: String,
    override val achievements: Achievements,
    override val otherInformation: String,
    override val createdAt: Timestamp,
    override val updatedAt: Timestamp
) : Time, Entity, ProfessorCommon
