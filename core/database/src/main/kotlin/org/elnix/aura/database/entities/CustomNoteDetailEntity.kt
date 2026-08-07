package org.elnix.aura.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "custom_note_details",
    indices = [Index(value = ["note"], unique = true)],
)
public data class CustomNoteDetailEntity(
    public val note: String,
    @PrimaryKey(autoGenerate = true) public val id: Long = 0,
)
