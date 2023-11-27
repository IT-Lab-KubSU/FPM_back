package ru.kubsu.fktpm.fpmbackend.dto

import java.sql.Timestamp

interface Time {
    val createdAt: Timestamp

    val updatedAt: Timestamp
}