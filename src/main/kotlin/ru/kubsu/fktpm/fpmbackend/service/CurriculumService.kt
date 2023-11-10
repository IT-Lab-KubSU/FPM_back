package ru.kubsu.fktpm.fpmbackend.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.set
import org.springframework.stereotype.Service


interface CurriculumCommon {
    val directionCode: String
    val department: String
    val plan: Map<String, Array<Subject>>
}

data class Subject(
    val title: String,
    val exam: Boolean,
    val credit: Boolean,
    val hours: Int
) {
    constructor() : this("", false, false, 0)
}


data class CurriculumRequest(
    override val department: String,
    override val directionCode: String,
    override val plan: Map<String, Array<Subject>>,
) : CurriculumCommon

data class Curriculum(
    override val department: String,
    override val directionCode: String,
    override val plan: Map<String, Array<Subject>>,
    val updatedAt: Long,
    val creationTime: Long,
) : CurriculumCommon


@Service
class CurriculumService(@Autowired val jdbcTemplate: JdbcTemplate) {
    val objectMapper = ObjectMapper()
    val namedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    fun save(curriculumRequest: CurriculumRequest) {
        val parameters = MapSqlParameterSource()
        parameters.set("directionCode", curriculumRequest.directionCode)
        parameters.set("department", curriculumRequest.department)
        parameters.set("plan", objectMapper.writeValueAsString(curriculumRequest.plan))

        namedParameterJdbcTemplate.update(
            "INSERT INTO curriculum (direction_code, department, plan) VALUES ((:directionCode), (:department), (:plan)::jsonb) ON CONFLICT (direction_code, department) DO UPDATE SET plan = (:plan)::jsonb",
            parameters
        )
    }

    fun getById(directionCode: String, department: String): Curriculum? {
        println(department)
        println(directionCode)
        return try {
            jdbcTemplate.queryForObject(
                "SELECT * FROM curriculum WHERE direction_code = ? and department = ?",
                arrayOf(directionCode, department)
            ) { rs, _ ->
                Curriculum(
                    rs.getString("direction_code"),
                    rs.getString("department"),
                    objectMapper.readValue(
                        rs.getString("plan"),
                        object : TypeReference<Map<String, Array<Subject>>>() {}),
                    rs.getLong("updated_at"),
                    rs.getLong("creation_time")
                )
            }
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }
}