package org.elnix.aura.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "birthdates",
    indices = [Index(value = ["birthdate"], unique = true)],
)
public data class BirthdateEntity(
    public val birthdate: String,
    @PrimaryKey(autoGenerate = true) public val id: Long = 0,
)
