package ru.kubsu.fktpm.fpmbackend.service.professor.dto

import ru.kubsu.fktpm.fpmbackend.dto.Gender
import java.util.*

data class ProfessorRequest(
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
    override val otherInformation: String
) : ProfessorCommon
