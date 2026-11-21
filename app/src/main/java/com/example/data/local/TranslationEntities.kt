package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translation_history")
data class HistoryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sourceText: String,
    val translatedText: String,
    val direction: String,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorite_translations")
data class FavoriteItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sourceText: String,
    val translatedText: String,
    val direction: String,
    val createdAtMillis: Long = System.currentTimeMillis()
)
