package org.elnix.aura.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.elnix.aura.database.entities.PhoneEntity

@Dao
public interface PhoneDao {
    @Insert
    public suspend fun insert(phone: PhoneEntity): Long

    @Query("SELECT * FROM phones ORDER BY phone ASC")
    public fun getAll(): Flow<List<PhoneEntity>>

    @Query("SELECT * FROM phones WHERE phone = :phone LIMIT 1")
    public suspend fun getByValue(phone: String): PhoneEntity?

    @Query("SELECT * FROM phones WHERE id = :id LIMIT 1")
    public suspend fun getById(id: Long): PhoneEntity?

    @Query("DELETE FROM phones WHERE id = :id")
    public suspend fun deleteById(id: Long)
}
