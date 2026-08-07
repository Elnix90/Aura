package org.elnix.aura.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "emails",
    indices = [Index(value = ["email"], unique = true)],
)
public data class EmailEntity(
    public val email: String,
    @PrimaryKey(autoGenerate = true) public val id: Long = 0,
)
