package org.elnix.aura.database.models

public data class IdentityValues(
    public val label: String? = null,
    public val color: String? = null,
    public val email: String? = null,
    public val name: String? = null,
    public val surname: String? = null,
    public val birthdate: String? = null,
    public val phone: String? = null,
    public val customNoteDetail: String? = null,
    public val address: AddressData? = null,
)
