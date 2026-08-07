package org.elnix.aura.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "names",
    indices = [Index(value = ["name"], unique = true)],
)
public data class NameEntity(
    public val name: String,
    @PrimaryKey(autoGenerate = true) public val id: Long = 0,
)
