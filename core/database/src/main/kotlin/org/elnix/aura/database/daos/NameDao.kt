package org.elnix.aura.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.elnix.aura.database.entities.NameEntity

@Dao
public interface NameDao {
    @Insert
    public suspend fun insert(name: NameEntity): Long

    @Query("SELECT * FROM names ORDER BY name ASC")
    public fun getAll(): Flow<List<NameEntity>>

    @Query("SELECT * FROM names WHERE name = :name LIMIT 1")
    public suspend fun getByValue(name: String): NameEntity?

    @Query("SELECT * FROM names WHERE id = :id LIMIT 1")
    public suspend fun getById(id: Long): NameEntity?

    @Query("DELETE FROM names WHERE id = :id")
    public suspend fun deleteById(id: Long)
}
