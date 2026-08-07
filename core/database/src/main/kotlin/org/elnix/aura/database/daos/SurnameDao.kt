package org.elnix.aura.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.elnix.aura.database.entities.SurnameEntity

@Dao
public interface SurnameDao {
    @Insert
    public suspend fun insert(surname: SurnameEntity): Long

    @Query("SELECT * FROM surnames ORDER BY surname ASC")
    public fun getAll(): Flow<List<SurnameEntity>>

    @Query("SELECT * FROM surnames WHERE surname = :surname LIMIT 1")
    public suspend fun getByValue(surname: String): SurnameEntity?

    @Query("SELECT * FROM surnames WHERE id = :id LIMIT 1")
    public suspend fun getById(id: Long): SurnameEntity?

    @Query("DELETE FROM surnames WHERE id = :id")
    public suspend fun deleteById(id: Long)
}
