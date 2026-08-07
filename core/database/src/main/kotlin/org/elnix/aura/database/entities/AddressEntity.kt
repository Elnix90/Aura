package org.elnix.aura.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addresses")
public data class AddressEntity(
    public val street: String? = null,
    public val houseNumber: String? = null,
    public val city: String? = null,
    public val postalCode: String? = null,
    public val state: String? = null,
    public val country: String? = null,
    public val additionalInfo: String? = null,
    @PrimaryKey(autoGenerate = true) public val id: Long = 0,
)
