package ru.kubsu.fktpm.fpmbackend.service.curriculum.tool

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.RowMapper
import ru.kubsu.fktpm.fpmbackend.service.curriculum.dto.Curriculum
import ru.kubsu.fktpm.fpmbackend.service.curriculum.dto.Subject
import java.sql.ResultSet
import java.sql.SQLException

class CurriculumMapper : RowMapper<Curriculum> {
    private val objectMapper = ObjectMapper()

    @Throws(SQLException::class)
    override fun mapRow(rs: ResultSet, rowNum: Int): Curriculum {
        return Curriculum(
            rs.getString("department"),
            rs.getString("direction_code"),
            objectMapper.readValue(
                rs.getString("plan"),
                object : TypeReference<Map<String, Array<Subject>>>() {}),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        )
    }
}
