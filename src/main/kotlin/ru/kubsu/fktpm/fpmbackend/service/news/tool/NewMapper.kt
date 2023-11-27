package ru.kubsu.fktpm.fpmbackend.service.news.tool

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.RowMapper
import ru.kubsu.fktpm.fpmbackend.service.news.dto.New
import java.sql.ResultSet
import java.sql.SQLException

class NewMapper : RowMapper<New> {
    private val objectMapper = ObjectMapper()

    @Throws(SQLException::class)
    override fun mapRow(rs: ResultSet, rowNum: Int): New {
        return New(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("text"),
            objectMapper.readValue(rs.getString("images"), object : TypeReference<List<String>>() {}),
            rs.getBoolean("status"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at"),
        )
    }
}
