package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM translation_history ORDER BY createdAtMillis DESC")
    fun observeHistory(): Flow<List<HistoryItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: HistoryItemEntity)

    @Query(
        "DELETE FROM translation_history WHERE id NOT IN " +
            "(SELECT id FROM translation_history ORDER BY createdAtMillis DESC LIMIT 100)"
    )
    suspend fun trimToLatest100()

    @Query("DELETE FROM translation_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM translation_history")
    suspend fun clearAll()
}

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_translations ORDER BY createdAtMillis DESC")
    fun observeFavorites(): Flow<List<FavoriteItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FavoriteItemEntity)

    @Query(
        "SELECT EXISTS(SELECT 1 FROM favorite_translations WHERE sourceText = :sourceText AND translatedText = :translatedText)"
    )
    suspend fun exists(sourceText: String, translatedText: String): Boolean

    @Query("DELETE FROM favorite_translations WHERE id = :id")
    suspend fun deleteById(id: Long)
}
