package ru.kubsu.fktpm.fpmbackend.service.lead.tool

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.RowMapper
import ru.kubsu.fktpm.fpmbackend.service.lead.dto.Lead
import ru.kubsu.fktpm.fpmbackend.service.lead.dto.LeadReasons
import java.sql.ResultSet
import java.sql.SQLException

class LeadMapper : RowMapper<Lead> {
    @Throws(SQLException::class)
    override fun mapRow(rs: ResultSet, rowNum: Int): Lead {
        return Lead(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("number"),
            rs.getString("email"),
            LeadReasons.valueOf(rs.getString("reason")),
            rs.getBoolean("status"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        )
    }
}
