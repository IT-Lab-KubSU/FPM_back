package ru.kubsu.fktpm.fpmbackend.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.Entity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import ru.kubsu.fktpm.fpmbackend.dto.Page
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

    fun createNew(new: NewRequest) {
        val sql = "INSERT INTO news (title, text, images, status) VALUES (?, ?, ?::jsonb, ?)"
        jdbcTemplate.update(
            sql,
            new.title,
            new.text,
            objectMapper.writeValueAsString(new.images),
            new.status
        )
    }

    fun getNewById(id: Long): New? {
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

    fun getNews(limit: Int, page: Int): Page<New> {
        val sql = "SELECT * FROM news ORDER BY news.id DESC LIMIT ? OFFSET ?"

        val news = jdbcTemplate.query(sql, arrayOf(limit, page * limit)) { rs, _ ->
            New(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("text"),
                objectMapper.readValue(rs.getString("images"), object : TypeReference<List<String>>() {}),
                rs.getBoolean("status"),
                rs.getLong("creation_time")
            )
        }
        val count = jdbcTemplate.queryForObject("SELECT count(id) FROM news", Long::class.java) ?: 0

        return Page(news, page, limit, ceil(count.toDouble() / limit).toInt(), count)
    }

    fun updateNew(new: New) {
        val sql = "UPDATE news SET title = ?, text = ?, images = ?::jsonb, creation_time = ? WHERE id = ?"

        jdbcTemplate.update(
            sql,
            new.title,
            new.text,
            new.images,
            new.creationTime,
            new.id
        )
    }

    fun deleteNew(id: Long) {
        val sql = "DELETE FROM news WHERE id = ?"

        jdbcTemplate.update(sql, id)
    }
}