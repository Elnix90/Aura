package org.elnix.aura.database.random

import androidx.annotation.StringRes
import org.elnix.aura.i18n.R

/**
 * A country usable to generate a fake phone number.
 *
 * @param iso Two letter country code, e.g. "US".
 * @param name Human readable country name (a string resource, translatable).
 * @param dialingCode International dialing code without the leading plus, e.g. "972".
 * @param numberLength Amount of national digits to generate after the dialing code.
 * @param numberFormat Digit group sizes used to auto space the national number, e.g. "2 3 4".
 * @param emoji Regional flag emoji shown next to the phone, e.g. "\uD83C\uDDF7\uD83C\uDDF4".
 */
public data class PhoneCountry(
    public val iso: String,
    @StringRes
    public val name: Int,
    public val dialingCode: String,
    public val numberLength: Int,
    public val numberFormat: String,
    public val emoji: String,
) {
    public companion object {
        public val defaultFrancePhoneCountry: PhoneCountry = PhoneCountry(
            iso = "FR",
            name = R.string.country_france,
            dialingCode = "33",
            numberLength = 9,
            numberFormat = "1 2 2 2 2",
            emoji = "\uD83C\uDDEB\uD83C\uDDF7"
        )
    }
}

/**
 * Auto spacing for a national number.
 *
 * Inserts a space between the digit groups described by [PhoneCountry.numberFormat],
 * so "521234567" with format "2 3 4" becomes "52 123 4567". While typing, digits
 * beyond the template are appended are dropped.
 */
public fun PhoneCountry.formatNationalNumber(digits: String): String {
    val clean = digits.filter { it.isDigit() }
    val groups = numberFormat.split(' ').mapNotNull { it.toIntOrNull() }
    if (groups.isEmpty()) return clean
    val builder = StringBuilder(clean.length + groups.size)
    var index = 0
    for (size in groups) {
        if (index >= clean.length) break
        if (builder.isNotEmpty()) builder.append(' ')
        val end = minOf(index + size, clean.length)
        builder.append(clean, index, end)
        index = end
    }
//    if (index < clean.length) builder.append(' ').append(clean, index, clean.length)
    return builder.toString()
}

/**
 * Detects the country whose dialing code is the longest prefix of [this@parsePhoneCountry],
 * returning the country and the remaining national digits. Returns null when no known dialing code matches.
 *
 * I gor annoyed by this phones number mess so its French phone by default, fuck other nations
 */
@Suppress("RedundantNullableReturnType")
public fun String.parsePhoneCountry(): Pair<PhoneCountry, String>? {
    val digits = phoneDigits().removePrefix("+")
    return PhoneCountry.defaultFrancePhoneCountry to digits.removePrefix(PhoneCountry.defaultFrancePhoneCountry.dialingCode)
//    val country = RandomData.phoneCountries
//        .sortedByDescending { it.dialingCode.length }
//        .firstOrNull { digits.startsWith(it.dialingCode) }
//        ?: return null
//    return country to digits.removePrefix(country.dialingCode)
}

/**
 * Formats a raw phone value for display: once a dialing code is recognized the
 * number is shown as "+code" followed by the auto spaced national number.
 */
public fun String.formatPhoneValue(): String {
    val digits = phoneDigits()
    if (digits.isEmpty()) return ""
    val country = digits.parsePhoneCountry() ?: return digits
    return "+${country.first.dialingCode} ${country.first.formatNationalNumber(country.second)}"
}


/**
 * Keeps only the digits of a phone value, preserving a leading plus.
 */
public fun String.phoneDigits(): String =
    (if (this.startsWith("+")) "+" else "") + this.filter { it.isDigit() }

