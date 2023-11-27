package ru.kubsu.fktpm.fpmbackend.service.curriculum

import com.fasterxml.jackson.core.type.TypeReference
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
import ru.kubsu.fktpm.fpmbackend.service.curriculum.dto.Curriculum
import ru.kubsu.fktpm.fpmbackend.service.curriculum.dto.CurriculumRequest
import ru.kubsu.fktpm.fpmbackend.service.curriculum.dto.Subject
import ru.kubsu.fktpm.fpmbackend.service.curriculum.tool.CurriculumMapper


@Service
class CurriculumService(@Autowired val jdbcTemplate: JdbcTemplate) {
    val objectMapper = ObjectMapper()
    val namedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    fun save(curriculumRequest: CurriculumRequest) {
        val parameters = MapSqlParameterSource()
        parameters.set("directionCode", curriculumRequest.directionCode)
        parameters.set("department", curriculumRequest.department)
        parameters.set("plan", objectMapper.writeValueAsString(curriculumRequest.plan))

        val query =
            "INSERT INTO curriculum (direction_code, department, plan) VALUES (:directionCode, :department, :plan::jsonb) ON CONFLICT (direction_code, department) DO UPDATE SET plan = :plan::jsonb"

        namedParameterJdbcTemplate.update(query, parameters)
    }

    fun getById(directionCode: String, department: String): Curriculum? {
        val parameters = MapSqlParameterSource()
        parameters.set("directionCode", directionCode)
        parameters.set("department", department)

        val query = "SELECT * FROM curriculum WHERE direction_code=:directionCode and department=:department"

        return try {
            namedParameterJdbcTemplate.queryForObject(query, parameters, CurriculumMapper())
        } catch (e: EmptyResultDataAccessException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Объект не найден!")
        }
    }
}