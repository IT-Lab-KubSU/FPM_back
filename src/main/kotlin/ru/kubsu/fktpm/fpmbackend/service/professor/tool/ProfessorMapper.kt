package ru.kubsu.fktpm.fpmbackend.service.professor.tool

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.RowMapper
import ru.kubsu.fktpm.fpmbackend.dto.Gender
import ru.kubsu.fktpm.fpmbackend.service.lead.dto.LeadReasons
import ru.kubsu.fktpm.fpmbackend.service.professor.dto.Achievements
import ru.kubsu.fktpm.fpmbackend.service.professor.dto.Professor
import java.sql.ResultSet
import java.sql.SQLException

class ProfessorMapper : RowMapper<Professor> {
    private val objectMapper = ObjectMapper()

    @Throws(SQLException::class)
    override fun mapRow(rs: ResultSet, rowNum: Int): Professor {
        return Professor(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("surname"),
            rs.getString("patronymic"),
            rs.getDate("birth_date"),
            Gender.valueOf(rs.getString("gender")),
            rs.getString("contact_number"),
            rs.getString("email"),
            rs.getString("address"),
            rs.getDate("employment_start_date"),
            rs.getString("academic_degree"),
            rs.getString("academic_rank"),
            objectMapper.readValue(
                rs.getString("achievements"), object : TypeReference<Achievements>() {}),
            rs.getString("other_information"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        )
    }
}