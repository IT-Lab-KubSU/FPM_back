package ru.kubsu.fktpm.fpmbackend.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import ru.kubsu.fktpm.fpmbackend.dto.Page
import ru.kubsu.fktpm.fpmbackend.dto.SortOrder
import kotlin.math.ceil


interface NewCommon {
    val title: String
    val text: String
    val images: List<String>
    val status: Boolean
}

data class NewRequest(
    override val title: String,
    override val text: String,
    override val images: List<String>,
    override val status: Boolean,
) : NewCommon

enum class NewSortField {
    ID, TITLE, STATUS, CREATION_TIME
}

data class NewsFilter(
    val limit: Int,
    val page: Int,
    val sortField: NewSortField? = NewSortField.ID,
    val sortOrder: SortOrder? = SortOrder.DESC,
    val search: String? = ""
)

data class New(
    val id: Long,
    override val title: String,
    override val text: String,
    override val images: List<String>,
    override val status: Boolean,
    val creationTime: Long
) : NewCommon

@Service
class NewService {
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate
    val objectMapper = ObjectMapper()

    fun create(new: NewRequest) {
        val sql = "INSERT INTO news (title, text, images, status) VALUES (?::text, ?::text, ?::jsonb, ?)"
        jdbcTemplate.update(
            sql, new.title, new.text, objectMapper.writeValueAsString(new.images), new.status
        )
    }

    fun getById(id: Long): New? {
        val sql = "SELECT * FROM news WHERE id = ?"

        return try {
            jdbcTemplate.queryForObject(sql, arrayOf(id)) { rs, _ ->
                New(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("text"),
                    objectMapper.readValue(rs.getString("images"), object : TypeReference<List<String>>() {}),
                    rs.getBoolean("status"),
                    rs.getLong("creation_time")
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getWithFilter(filter: NewsFilter): Page<New> {
        val safeSort = "${filter.sortField.toString().lowercase()} ${filter.sortOrder}, id DESC"

        val count = jdbcTemplate.queryForObject(
            "SELECT count(*) FROM news WHERE title ILIKE CONCAT('%', ?::text, '%')", Long::class.java, filter.search
        )
        if (count == 0L)
            return Page(arrayListOf(), filter.page, filter.limit, 0, 0)

        val sql = "SELECT * FROM news WHERE title ILIKE CONCAT('%', ?::text, '%') ORDER BY $safeSort LIMIT ? OFFSET ?"


        val news = jdbcTemplate.query(sql, arrayOf(filter.search, filter.limit, filter.page * filter.limit)) { rs, _ ->
            New(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("text"),
                objectMapper.readValue(rs.getString("images"), object : TypeReference<List<String>>() {}),
                rs.getBoolean("status"),
                rs.getLong("creation_time")
            )
        }

        return Page(news, filter.page, filter.limit, ceil(count.toDouble() / filter.limit).toInt(), count)
    }

    fun update(new: New) {
        val sql = "UPDATE news SET title = ?, text = ?, images = ?::jsonb, status = ? WHERE id = ?"

        jdbcTemplate.update(
            sql,
            new.title,
            new.text,
            objectMapper.writeValueAsString(new.images),
            new.status,
            new.id
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