package org.elnix.aura.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "phones",
    indices = [Index(value = ["phone"], unique = true)],
)
public data class PhoneEntity(
    public val phone: String,
    @PrimaryKey(autoGenerate = true) public val id: Long = 0,
)
