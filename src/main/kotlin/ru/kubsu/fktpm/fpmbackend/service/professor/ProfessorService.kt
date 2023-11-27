package ru.kubsu.fktpm.fpmbackend.service.professor

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.set
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import ru.kubsu.fktpm.fpmbackend.dto.Page
import ru.kubsu.fktpm.fpmbackend.service.professor.dto.Professor
import ru.kubsu.fktpm.fpmbackend.service.professor.dto.ProfessorRequest
import ru.kubsu.fktpm.fpmbackend.service.professor.tool.ProfessorMapper
import kotlin.math.ceil

@Service
class ProfessorService(@Autowired val jdbcTemplate: JdbcTemplate) {
    val namedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)
    val objectMapper = ObjectMapper()

    fun create(professorRequest: ProfessorRequest) {
        val parameters = MapSqlParameterSource()
        parameters.set("name", professorRequest.name)
        parameters.set("surname", professorRequest.surname)
        parameters.set("patronymic", professorRequest.patronymic)
        parameters.set("birthDate", professorRequest.birthDate)
        parameters.set("gender", professorRequest.gender.toString())
        parameters.set("contactNumber", professorRequest.contactNumber)
        parameters.set("email", professorRequest.email)
        parameters.set("address", professorRequest.address)
        parameters.set("employmentStartDate", professorRequest.employmentStartDate)
        parameters.set("academicDegree", professorRequest.academicDegree)
        parameters.set("academicRank", professorRequest.academicRank)
        parameters.set("achievements", objectMapper.writeValueAsString(professorRequest.achievements))
        parameters.set("otherInformation", professorRequest.otherInformation)

        val query =
            "INSERT INTO professors (name, surname, patronymic, birth_date, gender, contact_number, email, address, employment_start_date, academic_degree, academic_rank, achievements, other_information) " +
                    "VALUES (:name, :surname, :patronymic, :birthDate, :gender, :contactNumber, :email, :address, :employmentStartDate, :academicDegree, :academicRank, :achievements::jsonb, :otherInformation)"
        namedParameterJdbcTemplate.update(
            query,
            parameters
        )
    }

    fun getById(id: Long): Professor? {
        val parameters = MapSqlParameterSource("id", id)
        val query = "SELECT * FROM professors WHERE id = :id"

        return try {
            namedParameterJdbcTemplate.queryForObject(
                query,
                parameters,
                ProfessorMapper()
            )
        } catch (e: EmptyResultDataAccessException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Объект не найден!")
        }
    }

    fun update(professor: Professor) {
        val parameters = MapSqlParameterSource()
        parameters.set("id", professor.id)
        parameters.set("name", professor.name)
        parameters.set("surname", professor.surname)
        parameters.set("patronymic", professor.patronymic)
        parameters.set("birthDate", professor.birthDate)
        parameters.set("gender", professor.gender.toString())
        parameters.set("contactNumber", professor.contactNumber)
        parameters.set("email", professor.email)
        parameters.set("address", professor.address)
        parameters.set("employmentStartDate", professor.employmentStartDate)
        parameters.set("academicDegree", professor.academicDegree)
        parameters.set("academicRank", professor.academicRank)
        parameters.set("achievements", objectMapper.writeValueAsString(professor.achievements))
        parameters.set("otherInformation", professor.otherInformation)

        val query =
            "UPDATE professors SET name=:name, surname=:surname, patronymic=:patronymic, birth_date=:birthDate, gender=:gender, contact_number=:contactNumber, email=:email, address=:address, employment_start_date=:employmentStartDate, academic_degree=:academicDegree, academic_rank=:academicRank, achievements=:achievements::jsonb, other_information=:otherInformation WHERE id=:id"

        namedParameterJdbcTemplate.update(
            query,
            parameters
        )
    }

    fun deleteById(id: Long) {
        val sql = "DELETE FROM professors WHERE id = ?"

        jdbcTemplate.update(sql, id)
    }
}