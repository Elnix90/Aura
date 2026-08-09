package org.elnix.aura.database.random

import org.elnix.aura.database.models.AddressData
import org.elnix.aura.database.models.IdentityValues
import org.elnix.aura.database.random.PhoneCountry.Companion.defaultFrancePhoneCountry
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Generates realistic looking fake values for a disposable identity.
 *
 * Every value is picked from the bundled lists in [RandomData], so no external
 * service is needed for the plain shuffle buttons. The [randomizeAll] helper
 * builds a complete [IdentityValues] in one call.
 */
@Singleton
public class RandomIdentityProvider @Inject constructor() {

    public fun randomLabel(): String = RandomData.labels.random()

    public fun randomName(): String = RandomData.firstNames.random()

    public fun randomSurname(): String = RandomData.surnames.random()

    public fun randomEmail(): String {
        val localPart = buildString {
            append(randomName().lowercase())
            if (Random.nextBoolean()) {
                append('.').append(randomSurname().lowercase())
            }
            if (Random.nextInt(100) < 30) {
                append(Random.nextInt(10, 999))
            }
        }.sanitizeEmailLocalPart()
        return "$localPart@${RandomData.emailDomains.random()}"
    }

    public fun randomBirthdate(): String {
        val today = LocalDate.now()
        val daysBack = Random.nextLong(18 * 365L, 75 * 365L)
        return today.minusDays(daysBack).format(DateTimeFormatter.ISO_LOCAL_DATE)
    }


    public fun randomPhoneCountry(): PhoneCountry = defaultFrancePhoneCountry

    public fun randomPhoneNumber(country: PhoneCountry): String =
        (1..country.numberLength).map { Random.nextInt(10) }.joinToString(separator = "")

    public fun randomPhone(): String {
        val country = randomPhoneCountry()
        return "+${country.dialingCode}${randomPhoneNumber(country)}"
    }

    public fun randomStreet(): String = RandomData.streets.random()

    public fun randomCity(): String = RandomData.cities.random()

    public fun randomCountry(): String = RandomData.countries.random()

    public fun randomHouseNumber(): String = Random.nextInt(1, 400).toString()

    public fun randomPostalCode(): String =
        if (Random.nextBoolean()) {
            (1..4).map { Random.nextInt(10) }.joinToString(separator = "")
        } else {
            buildString {
                append('A' + Random.nextInt(26))
                append(Random.nextInt(10))
                append('A' + Random.nextInt(26))
                append(Random.nextInt(10))
            }
        }

    public fun randomAddress(): AddressData = AddressData(
        street = randomStreet(),
        houseNumber = randomHouseNumber(),
        city = randomCity(),
        postalCode = randomPostalCode(),
        state = null,
        country = randomCountry(),
        additionalInfo = null,
    )

    public fun randomColorHex(): String {
        val argb = 0xFF000000 or Random.nextLong(0x1000000)
        return "#%08X".format(argb)
    }

    public fun randomizeAll(): IdentityValues = IdentityValues(
        label = randomLabel(),
        color = randomColorHex(),
        email = randomEmail(),
        name = randomName(),
        surname = randomSurname(),
        birthdate = randomBirthdate(),
        phone = randomPhone(),
        address = randomAddress(),
    )
}

private fun String.sanitizeEmailLocalPart(): String =
    lowercase().filter { it.isLetterOrDigit() || it == '.' || it == '-' }
