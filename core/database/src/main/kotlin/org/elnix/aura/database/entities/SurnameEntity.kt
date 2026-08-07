package org.elnix.aura.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "surnames",
    indices = [Index(value = ["surname"], unique = true)],
)
public data class SurnameEntity(
    public val surname: String,
    @PrimaryKey(autoGenerate = true) public val id: Long = 0,
)
