package ru.kubsu.fktpm.fpmbackend.service.lead

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
import ru.kubsu.fktpm.fpmbackend.service.lead.dto.Lead
import ru.kubsu.fktpm.fpmbackend.service.lead.dto.LeadFilter
import ru.kubsu.fktpm.fpmbackend.service.lead.dto.LeadRequest
import ru.kubsu.fktpm.fpmbackend.service.lead.tool.LeadMapper
import kotlin.math.ceil


@Service
class LeadService(@Autowired val jdbcTemplate: JdbcTemplate) {
    val namedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    fun create(leadRequest: LeadRequest) {
        val parameters = MapSqlParameterSource()
        parameters.set("name", leadRequest.name)
        parameters.set("number", leadRequest.number)
        parameters.set("reason", leadRequest.reason.toString())
        parameters.set("status", leadRequest.status)

        val query = "INSERT INTO leads (name, number, email, reason, status) VALUES (:name, :number, :email, :reason, :status)"

        namedParameterJdbcTemplate.update(
            query,
            parameters
        )
    }

    fun getById(id: Long): Lead? {
        val parameters = MapSqlParameterSource("id", id)
        val query = "SELECT * FROM leads WHERE id = :id"

        return try {
            namedParameterJdbcTemplate.queryForObject(query, parameters, LeadMapper())
        } catch (e: EmptyResultDataAccessException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Объект не найден!")
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
        val condition = arrayListOf<String>(searchFilter, statusFilter).filter { it.isNotEmpty() }.joinToString(" AND ").trim()
        val conditionQuery = if (condition.isEmpty()) "" else "WHERE $condition"

        val parameters = MapSqlParameterSource()
        parameters.set("search", filter.search)
        parameters.set("limit", filter.limit)
        parameters.set("offset", filter.limit * filter.page)

        val count = namedParameterJdbcTemplate.queryForObject(
            "SELECT count(*) FROM leads $conditionQuery;",
            parameters,
            Long::class.java
        ) ?: 0L

        if (count == 0L)
            return Page(arrayListOf(), filter.page, filter.limit, 0, 0)

        val query = "SELECT * FROM leads $conditionQuery ORDER BY $safeSort LIMIT :limit OFFSET :offset;"

        val items = namedParameterJdbcTemplate.query(
            query,
            parameters,
            LeadMapper()
        )

        return Page(items, filter.page, filter.limit, ceil(count.toDouble() / filter.limit).toInt(), count)
    }

    fun update(lead: Lead) {
        val parameters = MapSqlParameterSource()
        parameters.set("name", lead.name)
        parameters.set("number", lead.number)
        parameters.set("reason", lead.reason.toString())
        parameters.set("id", lead.id)
        parameters.set("status", lead.status)

        val query = "UPDATE leads SET name=:name, number=:number, email=:email, reason=:reason, status=:status WHERE id=:id"

        namedParameterJdbcTemplate.update(
            query,
            parameters
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