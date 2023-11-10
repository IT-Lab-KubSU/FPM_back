package ru.kubsu.fktpm.fpmbackend.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.set
import org.springframework.stereotype.Service
import ru.kubsu.fktpm.fpmbackend.dto.Page
import ru.kubsu.fktpm.fpmbackend.dto.SortOrder
import kotlin.math.ceil


interface LeadCommon {
    val name: String
    val number: String
    val email: String
    val reason: LeadReasons
    val status: Boolean
}

data class LeadRequest(
    override val name: String,
    override val number: String,
    override val email: String,
    override val reason: LeadReasons,
    override val status: Boolean,
) : LeadCommon

enum class LeadSortField {
    ID, NAME, STATUS, REASON, CREATION_TIME
}

enum class LeadReasons {
    CALLBACK, ADMISSION
}

data class LeadFilter(
    val limit: Int,
    val page: Int,
    val sortField: LeadSortField? = LeadSortField.ID,
    val sortOrder: SortOrder? = SortOrder.DESC,
    val search: String? = "",
    val status: Boolean?
)

data class Lead(
    val id: Long,
    override val name: String,
    override val number: String,
    override val email: String,
    override val reason: LeadReasons,
    override val status: Boolean,
    val creationTime: Long
) : LeadCommon

@Service
class LeadService(@Autowired val jdbcTemplate: JdbcTemplate) {
    val objectMapper = ObjectMapper()
    val namedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    fun create(leads: LeadRequest) {
        val sql = "INSERT INTO leads (name, number, email, reason, status) VALUES (?, ?, ?, ?, ?)"
        jdbcTemplate.update(
            sql,
            leads.name,
            leads.number,
            leads.email,
            leads.reason.toString(),
            leads.status
        )
    }

    fun getById(id: Long): Lead? {
        val sql = "SELECT * FROM leads WHERE id = ?"

        return try {
            jdbcTemplate.queryForObject(sql, arrayOf(id)) { rs, _ ->
                Lead(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("number"),
                    rs.getString("email"),
                    LeadReasons.valueOf(rs.getString("reason")),
                    rs.getBoolean("status"),
                    rs.getLong("creation_time")
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getWithFilter(filter: LeadFilter): Page<Lead> {
        val safeSort = "${filter.sortField.toString().lowercase()} ${filter.sortOrder}, id DESC"
        val statusFilter = when (filter.status) {
            true -> "status = true"
            false -> "status = false"
            null -> ""
        }
        val searchFilter = if (!filter.search.isNullOrBlank()) "(name ILIKE CONCAT('%', :search, '%') or number ILIKE CONCAT('%', :search, '%'))" else ""
        val query = arrayListOf<String>(searchFilter, statusFilter).filter { it.isNotEmpty() }.joinToString(" AND ")

        val parameters = MapSqlParameterSource()
        parameters.set("search", filter.search)
        parameters.set("limit", filter.limit)
        parameters.set("offset", filter.limit * filter.page)

        val count = namedParameterJdbcTemplate.queryForObject(
            "SELECT count(*) FROM leads ${if (query.trim().isEmpty()) "" else "WHERE $query"};",
            parameters,
            Long::class.java
        ) ?: 0L

        if (count == 0L)
            return Page(arrayListOf(), filter.page, filter.limit, 0, 0)

        val items = namedParameterJdbcTemplate.query(
            "SELECT * FROM leads ${if (query.trim().isEmpty()) "" else "WHERE $query"} ORDER BY $safeSort LIMIT :limit OFFSET :offset;",
            parameters
        ) { rs, _ ->
            Lead(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("number"),
                rs.getString("email"),
                LeadReasons.valueOf(rs.getString("reason")),
                rs.getBoolean("status"),
                rs.getLong("creation_time")
            )
        }

        return Page(items, filter.page, filter.limit, ceil(count.toDouble() / filter.limit).toInt(), count)
    }

    fun update(lead: Lead) {
        val sql = "UPDATE leads SET name = ?, number = ?, email = ?, reason = ?, status = ? WHERE id = ?"

        jdbcTemplate.update(
            sql,
            lead.name,
            lead.number,
            lead.email,
            lead.reason.toString(),
            lead.status,
            lead.id,
        )
    }

    fun deleteById(id: Long) {
        val sql = "DELETE FROM leads WHERE id = ?"

        jdbcTemplate.update(sql, id)
    }

    fun deleteByIds(ids: List<Long>) {
        val sql = "DELETE FROM leads WHERE id = any(?)"

        jdbcTemplate.update(sql, ids.toTypedArray())
    }
}