package ru.kubsu.fktpm.fpmbackend.service.professor.dto

import ru.kubsu.fktpm.fpmbackend.dto.Gender
import java.util.Date

interface ProfessorCommon {
    val name: String
    val surname: String
    val patronymic: String
    val birthDate: Date
    val gender: Gender
    val contactNumber: String
    val email: String
    val address: String
    val employmentStartDate: Date
    val academicDegree: String
    val academicRank: String
    val achievements: Achievements
    val otherInformation: String
}