package com.example.data.repository

import com.example.data.local.FavoriteDao
import com.example.data.local.FavoriteItemEntity
import com.example.data.local.HistoryDao
import com.example.data.local.HistoryItemEntity
import com.example.data.remote.MyMemoryApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

sealed interface TranslationOutcome {
    data class Success(val translatedText: String) : TranslationOutcome
    data object NotFound : TranslationOutcome
    data object NetworkError : TranslationOutcome
}

class TranslatorRepository(
    private val api: MyMemoryApi,
    private val historyDao: HistoryDao,
    private val favoriteDao: FavoriteDao
) {
    val history: Flow<List<HistoryItemEntity>> = historyDao.observeHistory()
    val favorites: Flow<List<FavoriteItemEntity>> = favoriteDao.observeFavorites()

    suspend fun translate(text: String, langPair: String): TranslationOutcome =
        withContext(Dispatchers.IO) {
            try {
                val response = api.translate(text = text, langPair = langPair)
                val translated = response.responseData?.translatedText?.trim()
                if (!translated.isNullOrEmpty()) {
                    historyDao.insert(
                        HistoryItemEntity(
                            sourceText = text,
                            translatedText = translated,
                            direction = langPair
                        )
                    )
                    historyDao.trimToLatest100()
                    TranslationOutcome.Success(translated)
                } else {
                    TranslationOutcome.NotFound
                }
            } catch (_: Exception) {
                TranslationOutcome.NetworkError
            }
        }

    suspend fun saveFavorite(sourceText: String, translatedText: String, direction: String): Boolean {
        val alreadyExists = favoriteDao.exists(sourceText, translatedText)
        if (alreadyExists) return false
        favoriteDao.insert(
            FavoriteItemEntity(
                sourceText = sourceText,
                translatedText = translatedText,
                direction = direction
            )
        )
        return true
    }

    suspend fun removeFavorite(id: Long) {
        favoriteDao.deleteById(id)
    }

    suspend fun removeHistoryItem(id: Long) {
        historyDao.deleteById(id)
    }

    suspend fun clearHistory() {
        historyDao.clearAll()
    }
}
