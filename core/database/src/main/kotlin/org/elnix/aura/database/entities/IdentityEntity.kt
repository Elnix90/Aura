package org.elnix.aura.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "identities",
    foreignKeys = [
        ForeignKey(
            entity = EmailEntity::class,
            parentColumns = ["id"],
            childColumns = ["emailId"],
            onDelete = ForeignKey.NO_ACTION,
        ),
        ForeignKey(
            entity = NameEntity::class,
            parentColumns = ["id"],
            childColumns = ["nameId"],
            onDelete = ForeignKey.NO_ACTION,
        ),
        ForeignKey(
            entity = SurnameEntity::class,
            parentColumns = ["id"],
            childColumns = ["surnameId"],
            onDelete = ForeignKey.NO_ACTION,
        ),
        ForeignKey(
            entity = BirthdateEntity::class,
            parentColumns = ["id"],
            childColumns = ["birthdateId"],
            onDelete = ForeignKey.NO_ACTION,
        ),
        ForeignKey(
            entity = AddressEntity::class,
            parentColumns = ["id"],
            childColumns = ["addressId"],
            onDelete = ForeignKey.NO_ACTION,
        ),
        ForeignKey(
            entity = PhoneEntity::class,
            parentColumns = ["id"],
            childColumns = ["phoneId"],
            onDelete = ForeignKey.NO_ACTION,
        ),
        ForeignKey(
            entity = CustomNoteDetailEntity::class,
            parentColumns = ["id"],
            childColumns = ["customNoteDetailId"],
            onDelete = ForeignKey.NO_ACTION,
        ),
    ],
    indices = [
        Index(value = ["emailId"]),
        Index(value = ["nameId"]),
        Index(value = ["surnameId"]),
        Index(value = ["birthdateId"]),
        Index(value = ["addressId"]),
        Index(value = ["phoneId"]),
        Index(value = ["customNoteDetailId"]),
    ],
)

public data class IdentityEntity(
    public val label: String? = null,
    public val color: String? = null,
    public val emailId: Long? = null,
    public val nameId: Long? = null,
    public val surnameId: Long? = null,
    public val birthdateId: Long? = null,
    public val addressId: Long? = null,
    public val phoneId: Long? = null,
    public val customNoteDetailId: Long? = null,
    public val createdAt: Long = 0,
    public val updatedAt: Long = 0,
    @PrimaryKey(autoGenerate = true) public val id: Long = 0,
)
