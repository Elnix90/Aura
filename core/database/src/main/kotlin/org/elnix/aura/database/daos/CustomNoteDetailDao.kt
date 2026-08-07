package org.elnix.aura.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.elnix.aura.database.entities.CustomNoteDetailEntity

@Dao
public interface CustomNoteDetailDao {
    @Insert
    public suspend fun insert(note: CustomNoteDetailEntity): Long

    @Query("SELECT * FROM custom_note_details ORDER BY note ASC")
    public fun getAll(): Flow<List<CustomNoteDetailEntity>>

    @Query("SELECT * FROM custom_note_details WHERE note = :note LIMIT 1")
    public suspend fun getByValue(note: String): CustomNoteDetailEntity?

    @Query("SELECT * FROM custom_note_details WHERE id = :id LIMIT 1")
    public suspend fun getById(id: Long): CustomNoteDetailEntity?

    @Query("DELETE FROM custom_note_details WHERE id = :id")
    public suspend fun deleteById(id: Long)
}
