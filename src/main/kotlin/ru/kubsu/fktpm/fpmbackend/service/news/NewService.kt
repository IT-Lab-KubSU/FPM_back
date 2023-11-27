package ru.kubsu.fktpm.fpmbackend.service.news

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
import ru.kubsu.fktpm.fpmbackend.service.news.dto.New
import ru.kubsu.fktpm.fpmbackend.service.news.dto.NewRequest
import ru.kubsu.fktpm.fpmbackend.service.news.dto.NewsFilter
import ru.kubsu.fktpm.fpmbackend.service.news.tool.NewMapper
import kotlin.math.ceil


@Service
class NewService(@Autowired val jdbcTemplate: JdbcTemplate) {
    val namedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)
    val objectMapper = ObjectMapper()

    fun create(newRequest: NewRequest) {
        val parameters = MapSqlParameterSource()
        parameters.set("title", newRequest.title)
        parameters.set("text", newRequest.text)
        parameters.set("images", objectMapper.writeValueAsString(newRequest.images))
        parameters.set("status", newRequest.status)

        val query =
            "INSERT INTO news (title, text, images, status) VALUES (:title::text, :text::text, :images::jsonb, :status)"
        namedParameterJdbcTemplate.update(
            query,
            parameters
        )
    }

    fun getById(id: Long): New? {
        val parameters = MapSqlParameterSource("id", id)
        val query = "SELECT * FROM news WHERE id = :id"

        return try {
            namedParameterJdbcTemplate.queryForObject(
                query,
                parameters,
                NewMapper()
            )
        } catch (e: EmptyResultDataAccessException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Объект не найден!")
        }
    }

    fun getWithFilter(filter: NewsFilter): Page<New> {
        val safeSort = "${filter.sortField.toString().lowercase()} ${filter.sortOrder}, id DESC"

        val parameters = MapSqlParameterSource()
        parameters.set("title", filter.search)
        parameters.set("limit", filter.limit)
        parameters.set("offset", filter.limit * filter.page)

        val count = namedParameterJdbcTemplate.queryForObject(
            "SELECT count(*) FROM news WHERE title ILIKE CONCAT('%', :title::text, '%')",
            parameters,
            Long::class.java
        ) ?: 0L

        if (count == 0L)
            return Page(arrayListOf(), filter.page, filter.limit, 0, 0)

        val query =
            "SELECT * FROM news WHERE title ILIKE CONCAT('%', :title::text, '%') ORDER BY $safeSort LIMIT :limit OFFSET :offset"

        val news = namedParameterJdbcTemplate.query(
            query,
            parameters,
            NewMapper()
        )

        return Page(news, filter.page, filter.limit, ceil(count.toDouble() / filter.limit).toInt(), count)
    }

    fun update(new: New) {
        val parameters = MapSqlParameterSource()
        parameters.set("id", new.id)
        parameters.set("title", new.title)
        parameters.set("text", new.text)
        parameters.set("images", objectMapper.writeValueAsString(new.images))
        parameters.set("status", new.status)

        val query =
            "UPDATE news SET title = :title, text = :text, images = :images::jsonb, status = :status WHERE id=:id"

        namedParameterJdbcTemplate.update(
            query,
            parameters
        )
    }

    fun deleteById(id: Long) {
        val sql = "DELETE FROM news WHERE id = ?"

        jdbcTemplate.update(sql, id)
    }

    fun deleteByIds(ids: List<Long>) {
        val sql = "DELETE FROM news WHERE id = any(?)"

        jdbcTemplate.update(sql, ids.toTypedArray())
    }
}