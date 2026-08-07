package org.elnix.aura.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.elnix.aura.database.entities.BirthdateEntity

@Dao
public interface BirthdateDao {
    @Insert
    public suspend fun insert(birthdate: BirthdateEntity): Long

    @Query("SELECT * FROM birthdates ORDER BY birthdate ASC")
    public fun getAll(): Flow<List<BirthdateEntity>>

    @Query("SELECT * FROM birthdates WHERE birthdate = :birthdate LIMIT 1")
    public suspend fun getByValue(birthdate: String): BirthdateEntity?

    @Query("SELECT * FROM birthdates WHERE id = :id LIMIT 1")
    public suspend fun getById(id: Long): BirthdateEntity?

    @Query("DELETE FROM birthdates WHERE id = :id")
    public suspend fun deleteById(id: Long)
}
