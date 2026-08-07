package org.elnix.aura.database.models

public data class AddressData(
    public val street: String? = null,
    public val houseNumber: String? = null,
    public val city: String? = null,
    public val postalCode: String? = null,
    public val state: String? = null,
    public val country: String? = null,
    public val additionalInfo: String? = null,
)
