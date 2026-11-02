package com.bruna.stardust.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: FavoriteApod)

    @Query("SELECT * FROM favorites ORDER BY savedAt DESC")
    fun getAll(): Flow<List<FavoriteApod>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE date = :date)")
    fun isFavorite(date: String): Flow<Boolean>

    @Query("UPDATE favorites SET note = :note WHERE date = :date")
    suspend fun updateNote(date: String, note: String)

    @Query("DELETE FROM favorites WHERE date = :date")
    suspend fun delete(date: String)
}