package org.elnix.aura.ui.components.identity.phone

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.elnix.aura.database.random.PhoneCountry
import org.elnix.aura.database.random.formatNationalNumber
import org.elnix.aura.database.random.parsePhoneCountry
import org.elnix.aura.database.random.phoneDigits
import org.elnix.aura.i18n.R
import org.elnix.aura.ktx.showToast
import org.elnix.aura.theme.AppObjectsColors
import org.elnix.aura.ui.base.animation.Icon
import org.elnix.aura.ui.base.animation.rememberAnimatedIcon


/**
 * Detects the country from the typed dialing code.
 *
 * A leading plus forces a dialing-code search; otherwise the device locale's
 * country is used as the default prefix. Returns the country and the national
 * digits, or null while nothing is typed yet.
 */
private fun resolvePhoneParts(
    original: String,
    defaultCountry: PhoneCountry?,
): Pair<PhoneCountry, String>? {
    if (original.isEmpty()) return null
    val explicitPlus = original.startsWith("+")
    val digits = if (explicitPlus) original.drop(1) else original
    return if (explicitPlus) {
        digits.parsePhoneCountry()
    } else {
        defaultCountry?.let { it to digits } ?: digits.parsePhoneCountry()
    }
}

/**
 * Formats a phone as "+code <spaced national>" using the country detected from
 * the typed dialing code (or the locale country when no code is typed).
 *
 * Unlike a hardcoded North American mask, every country applies its own digit
 * grouping from [PhoneCountry.numberFormat]. Cursor offsets are remapped to
 * match the reflowing mask so typing stays stable and never crashes.
 */
class PhoneVisualTransformation(
    private val defaultCountry: PhoneCountry?,
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val original = text.text.phoneDigits()
        if (original.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        val transformed = formatRaw(original)
        return TransformedText(
            AnnotatedString(transformed),
            PhoneOffsetMapping(original, transformed),
        )
    }

    private fun formatRaw(original: String): String {
        val parts = resolvePhoneParts(original, defaultCountry) ?: return original
        val national = parts.first.formatNationalNumber(parts.second)
        return if (national.isEmpty()) {
            "+${parts.first.dialingCode}"
        } else {
            "+${parts.first.dialingCode} $national"
        }
    }
}

/**
 * Maps cursor offsets between the raw digits and the formatted text. Spaces
 * inserted by the mask are skipped and every lookup is clamped so the IME
 * never receives an out of range offset.
 */
private class PhoneOffsetMapping(
    private val original: String,
    private val transformed: String,
) : OffsetMapping {

    private val originalToTransformed: IntArray = IntArray(original.length + 1) { transformed.length }
    private val transformedToOriginal: IntArray = IntArray(transformed.length + 1)

    init {
        var origIndex = 0
        var inserted = 0
        for (transformedIndex in transformed.indices) {
            transformedToOriginal[transformedIndex] = transformedIndex - inserted
            if (origIndex < original.length && transformed[transformedIndex] == original[origIndex]) {
                originalToTransformed[origIndex] = transformedIndex
                origIndex++
            } else {
                inserted++
            }
        }
        transformedToOriginal[transformed.length] = transformed.length - inserted
        originalToTransformed[original.length] = transformed.length
    }

    override fun originalToTransformed(offset: Int): Int =
        originalToTransformed[offset.coerceIn(0, original.length)]

    override fun transformedToOriginal(offset: Int): Int =
        transformedToOriginal[offset.coerceIn(0, transformed.length)]
}


/**
 * Single phone field that merges the country code and the local number.
 *
 * The country is detected live from the typed dialing code, the national
 * number is spaced automatically using the country format, and the flag
 * animates in as soon as a country matches. Tapping the trailing icon opens
 * a searchable country picker dialog.
 */
@Composable
internal fun PhoneField(
    value: String,
    onValueChange: (String) -> Unit,
    onShuffle: () -> Unit,
) {
    val ctx = LocalContext.current
//    val locale = LocalLocale.current
//    var showPicker by remember { mutableStateOf(false) }
    val digits = value.phoneDigits()
    val defaultCountry = PhoneCountry.defaultFrancePhoneCountry
    val detectedCountry = remember(value, defaultCountry) {
        resolvePhoneParts(digits, defaultCountry)?.first
            ?: if (digits.isEmpty()) defaultCountry else null
    }

    val animatedIcon = rememberAnimatedIcon()

    TextField(
        value = value,
        onValueChange = {
            // Take 9 digits at most, for some reason even though I'm forcing french numbers, 10 doesn't work and takes a number mor that what's needed?
            val digits = it.phoneDigits().take(9)
            onValueChange(digits)
        },
        visualTransformation = PhoneVisualTransformation(defaultCountry),
        label = { Text(stringResource(R.string.identity_phone)) },
        placeholder = { Text(stringResource(R.string.phone_number)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        singleLine = true,
        leadingIcon = {
            AnimatedFlag(detectedCountry) {
//                    showPicker = true
                ctx.showToast("If you ain't french, fuck you")
            }
        },
        trailingIcon = {
            animatedIcon.Icon(R.drawable.shuffle) {
                animatedIcon.setSuccess()
                onShuffle()
            }
        },
        colors = AppObjectsColors.outlinedTextFieldColors(),
        shape = CircleShape,
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(10.dp)
    )

//    if (showPicker) {
//        PhoneCountryPickerDialog(
//            onDismiss = { showPicker = false },
//            onSelect = { country ->
//                val national = resolvePhoneParts(digits, defaultCountry)
//                    ?.takeIf { it.first.iso == country.iso }
//                    ?.second
//                    .orEmpty()
//                onValueChange("+${country.dialingCode}$national")
//                showPicker = false
//            },
//        )
//    }
}

/**
 * Animated flag emoji shown as the phone field leading icon. Falls back to a
 * phone emoji until a country is detected.
 */
@Composable
private fun AnimatedFlag(
    country: PhoneCountry?,
    onClick: () -> Unit,
) {
    AnimatedContent(
        targetState = country?.emoji.orEmpty(),
        transitionSpec = {
            (
                    fadeIn(animationSpec = tween(180)) +
                            scaleIn(initialScale = 0.6f, animationSpec = tween(180))
                    ) togetherWith (
                    fadeOut(animationSpec = tween(120)) +
                            scaleOut(targetScale = 0.6f, animationSpec = tween(120))
                    )
        },
        label = "phoneFlag",
    ) { emoji ->
        Row(Modifier.padding(start = 5.dp)) {
            Text(
                text = emoji.ifEmpty { "\uD83C\uDF10" },
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = onClick)
                    .padding(10.dp),
            )
        }
    }
}
//
///**
// * Searchable dialog listing every country with its flag, name and dialing code.
// */
//@Composable
//private fun PhoneCountryPickerDialog(
//    onDismiss: () -> Unit,
//    onSelect: (PhoneCountry) -> Unit,
//) {
//    var query by remember { mutableStateOf("") }
//    val countries = RandomData.phoneCountries.map { it to stringResource(it.name) }
//    val filtered = remember(query, countries) {
//        val q = query.trim().lowercase()
//        if (q.isEmpty()) {
//            countries
//        } else {
//            countries.filter { (country, name) ->
//                name.lowercase().contains(q) ||
//                        country.iso.lowercase().contains(q) ||
//                        country.dialingCode.contains(q)
//            }
//        }
//    }
//
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        confirmButton = {
//            TextButton(onClick = onDismiss) {
//                Text(stringResource(R.string.cancel))
//            }
//        },
//        title = { Text(stringResource(R.string.phone_country_picker_title)) },
//        text = {
//            Column {
//                IdentityTextField(
//                    value = query,
//                    onValueChange = { query = it },
//                    label = stringResource(R.string.phone_country_search),
//                )
//                LazyColumn(
//                    modifier = Modifier.heightIn(max = 360.dp),
//                ) {
//                    items(filtered, key = { it.first.iso }) { (country, name) ->
//                        DragonRow(
//                            onClick = { onSelect(country) },
//                            modifier = Modifier.fillMaxWidth(),
//                        ) {
//                            Text(
//                                text = country.emoji,
//                                style = MaterialTheme.typography.titleLarge,
//                            )
//                            Text(
//                                text = name,
//                                style = MaterialTheme.typography.bodyLarge,
//                                modifier = Modifier
//                                    .weight(1f)
//                                    .padding(horizontal = 12.dp),
//                            )
//                            Text(
//                                text = "+${country.dialingCode}",
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                            )
//                        }
//                    }
//                }
//            }
//        },
//        containerColor = MaterialTheme.colorScheme.surface,
//        tonalElevation = 6.dp,
//        shape = MaterialTheme.shapes.large,
//    )
//}
