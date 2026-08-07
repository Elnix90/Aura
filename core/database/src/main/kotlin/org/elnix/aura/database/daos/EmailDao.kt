package org.elnix.aura.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.elnix.aura.database.entities.EmailEntity

@Dao
public interface EmailDao {
    @Insert
    public suspend fun insert(email: EmailEntity): Long

    @Query("SELECT * FROM emails ORDER BY email ASC")
    public fun getAll(): Flow<List<EmailEntity>>

    @Query("SELECT * FROM emails WHERE email = :email LIMIT 1")
    public suspend fun getByValue(email: String): EmailEntity?

    @Query("SELECT * FROM emails WHERE id = :id LIMIT 1")
    public suspend fun getById(id: Long): EmailEntity?

    @Query("DELETE FROM emails WHERE id = :id")
    public suspend fun deleteById(id: Long)
}
