package ru.kubsu.fktpm.fpmbackend.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service


interface NewCommon {
    val title: String
    val text: String
    val images: List<String>
}

data class New(
    val id: Long,
    override val title: String,
    override val text: String,
    override val images: List<String>,
    val creationTime: Long
) : NewCommon

@Service
class NewService {
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate
    val objectMapper = ObjectMapper()

    fun createNew(new: NewCommon) {
        val jsonImages = objectMapper.writeValueAsString(new.images)

        val sql = "INSERT INTO news (title, text, images) VALUES (?, ?, ?::jsonb)"

        jdbcTemplate.update(
            sql,
            new.title,
            new.text,
            jsonImages,
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
                    rs.getLong("creation_time")
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getNews(limit: Int, page: Int): MutableList<New> {
        val sql = "SELECT * FROM news LIMIT ? OFFSET ?"

        return jdbcTemplate.query(sql, arrayOf(limit, page * limit)) { rs, _ ->
            New(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("text"),
                objectMapper.readValue(rs.getString("images"), object : TypeReference<List<String>>() {}),
                rs.getLong("creation_time")
            )
        }
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