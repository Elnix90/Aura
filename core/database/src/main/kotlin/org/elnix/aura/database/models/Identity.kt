package org.elnix.aura.database.models

import androidx.room.Embedded
import androidx.room.Relation
import org.elnix.aura.database.entities.AddressEntity
import org.elnix.aura.database.entities.BirthdateEntity
import org.elnix.aura.database.entities.CustomNoteDetailEntity
import org.elnix.aura.database.entities.EmailEntity
import org.elnix.aura.database.entities.IdentityEntity
import org.elnix.aura.database.entities.NameEntity
import org.elnix.aura.database.entities.PhoneEntity
import org.elnix.aura.database.entities.SurnameEntity

public data class Identity(
    @Embedded public val entity: IdentityEntity,
    @Relation(parentColumn = "emailId", entityColumn = "id") public val email: EmailEntity? = null,
    @Relation(parentColumn = "nameId", entityColumn = "id") public val name: NameEntity? = null,
    @Relation(parentColumn = "surnameId", entityColumn = "id") public val surname: SurnameEntity? = null,
    @Relation(parentColumn = "birthdateId", entityColumn = "id") public val birthdate: BirthdateEntity? = null,
    @Relation(parentColumn = "addressId", entityColumn = "id") public val address: AddressEntity? = null,
    @Relation(parentColumn = "phoneId", entityColumn = "id") public val phone: PhoneEntity? = null,
    @Relation(parentColumn = "customNoteDetailId", entityColumn = "id") public val customNoteDetail: CustomNoteDetailEntity? = null,
) {
    public fun toValues(): IdentityValues = IdentityValues(
        label = entity.label,
        email = email?.email,
        name = name?.name,
        surname = surname?.surname,
        birthdate = birthdate?.birthdate,
        phone = phone?.phone,
        customNoteDetail = customNoteDetail?.note,
        address = address?.let {
            AddressData(
                street = it.street,
                houseNumber = it.houseNumber,
                city = it.city,
                postalCode = it.postalCode,
                state = it.state,
                country = it.country,
                additionalInfo = it.additionalInfo,
            )
        },
    )
}
