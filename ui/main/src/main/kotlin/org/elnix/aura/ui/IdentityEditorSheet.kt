package org.elnix.aura.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.elnix.aura.database.models.AddressData
import org.elnix.aura.database.models.IdentityValues
import org.elnix.aura.i18n.R
import org.elnix.aura.theme.AppObjectsColors
import org.elnix.aura.ui.base.components.Spacer
import org.elnix.aura.ui.dragon.components.DragonModalBottomSheet
import org.elnix.aura.ui.dragon.components.DragonSettingsGroup
import org.elnix.aura.ui.dragon.components.ValidateCancelButtons
import org.elnix.aura.ui.dragon.components.rememberBottomSheetState
import org.elnix.aura.ui.dragon.expandable.ExpandableSection
import org.elnix.aura.ui.dragon.expandable.rememberExpandableSection
import org.elnix.aura.ui.dragon.model.ExpandableSectionMode
import org.elnix.aura.ui.dragon.text.DialogTitle

/**
 * Bottom sheet used both to create a new identity and to edit an existing one.
 *
 * The sheet holds local state for every editable value, initialized from
 * [initialValues]. On save, it collects the current state into an [IdentityValues]
 * and forwards it through [onSave]. Values left blank are dropped, so the
 * repository can reuse existing ones or create new ones on demand.
 *
 * @param initialValues The values to prefill the form with. Use [IdentityValues] defaults for creation.
 * @param onDismiss Requested when the user cancels or dismisses the sheet.
 * @param onSave Called with the collected values when the user confirms.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdentityEditorSheet(
    initialValues: IdentityValues,
    onDismiss: () -> Unit,
    onSave: (IdentityValues) -> Unit,
) {
    var label by rememberSaveable { mutableStateOf(initialValues.label.orEmpty()) }
    var email by rememberSaveable { mutableStateOf(initialValues.email.orEmpty()) }
    var name by rememberSaveable { mutableStateOf(initialValues.name.orEmpty()) }
    var surname by rememberSaveable { mutableStateOf(initialValues.surname.orEmpty()) }
    var birthdate by rememberSaveable { mutableStateOf(initialValues.birthdate.orEmpty()) }
    var phone by rememberSaveable { mutableStateOf(initialValues.phone.orEmpty()) }
    var customNote by rememberSaveable { mutableStateOf(initialValues.customNoteDetail.orEmpty()) }

    var street by rememberSaveable { mutableStateOf(initialValues.address?.street.orEmpty()) }
    var houseNumber by rememberSaveable { mutableStateOf(initialValues.address?.houseNumber.orEmpty()) }
    var city by rememberSaveable { mutableStateOf(initialValues.address?.city.orEmpty()) }
    var postalCode by rememberSaveable { mutableStateOf(initialValues.address?.postalCode.orEmpty()) }
    var state by rememberSaveable { mutableStateOf(initialValues.address?.state.orEmpty()) }
    var country by rememberSaveable { mutableStateOf(initialValues.address?.country.orEmpty()) }
    var additionalInfo by rememberSaveable { mutableStateOf(initialValues.address?.additionalInfo.orEmpty()) }

    val addressSection = rememberExpandableSection(
        title = stringResource(R.string.identity_address),
        mode = ExpandableSectionMode.Expandable,
    )

    DragonModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberBottomSheetState(true)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            DialogTitle(
                text = stringResource(R.string.edit_identity),
                modifier = Modifier.fillMaxWidth()
            )

            IdentityField(
                value = label,
                onValueChange = { label = it },
                label = stringResource(R.string.identity_label),
                placeholder = stringResource(R.string.identity_label_placeholder),
            )

            IdentityField(
                value = email,
                onValueChange = { email = it },
                label = stringResource(R.string.identity_email),
                keyboardType = KeyboardType.Email,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                IdentityField(
                    value = name,
                    onValueChange = { name = it },
                    label = stringResource(R.string.identity_name),
                    modifier = Modifier.weight(1f),
                )

                IdentityField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = stringResource(R.string.identity_surname),
                    modifier = Modifier.weight(1f),
                )
            }

            IdentityField(
                value = birthdate,
                onValueChange = { birthdate = it },
                label = stringResource(R.string.identity_birthdate),
                keyboardType = KeyboardType.DateTime,
            )

            IdentityField(
                value = phone,
                onValueChange = { phone = it },
                label = stringResource(R.string.identity_phone),
                keyboardType = KeyboardType.Phone,
            )

            DragonSettingsGroup(R.string.identity_address) {
                ExpandableSection(addressSection) {
                    IdentityField(
                        value = street,
                        onValueChange = { street = it },
                        label = stringResource(R.string.identity_address_street),
                    )

                    IdentityField(
                        value = houseNumber,
                        onValueChange = { houseNumber = it },
                        label = stringResource(R.string.identity_address_house_number),
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        IdentityField(
                            value = postalCode,
                            onValueChange = { postalCode = it },
                            label = stringResource(R.string.identity_address_postal_code),
                            modifier = Modifier.weight(1f),
                        )

                        IdentityField(
                            value = city,
                            onValueChange = { city = it },
                            label = stringResource(R.string.identity_address_city),
                            modifier = Modifier.weight(1f),
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        IdentityField(
                            value = state,
                            onValueChange = { state = it },
                            label = stringResource(R.string.identity_address_state),
                            modifier = Modifier.weight(1f),
                        )

                        IdentityField(
                            value = country,
                            onValueChange = { country = it },
                            label = stringResource(R.string.identity_address_country),
                            modifier = Modifier.weight(1f),
                        )
                    }

                    IdentityField(
                        value = additionalInfo,
                        onValueChange = { additionalInfo = it },
                        label = stringResource(R.string.identity_address_additional_info),
                    )
                }
            }

            IdentityField(
                value = customNote,
                onValueChange = { customNote = it },
                label = stringResource(R.string.identity_custom_note),
                singleLine = false,
            )

            Spacer(5.dp)

            ValidateCancelButtons(
                validateText = stringResource(R.string.save),
                cancelText = stringResource(R.string.cancel),
                onCancel = onDismiss,
                onConfirm = {
                    onSave(
                        IdentityValues(
                            label = label.blankToNull(),
                            email = email.blankToNull(),
                            name = name.blankToNull(),
                            surname = surname.blankToNull(),
                            birthdate = birthdate.blankToNull(),
                            phone = phone.blankToNull(),
                            customNoteDetail = customNote.blankToNull(),
                            address = if (addressSection.isExpanded()) {
                                AddressData(
                                    street = street.blankToNull(),
                                    houseNumber = houseNumber.blankToNull(),
                                    city = city.blankToNull(),
                                    postalCode = postalCode.blankToNull(),
                                    state = state.blankToNull(),
                                    country = country.blankToNull(),
                                    additionalInfo = additionalInfo.blankToNull(),
                                ).takeIf { it.isNotEmpty() }
                            } else null,
                        )
                    )
                },
            )
        }
    }
}

@Composable
private fun IdentityField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    shape: Shape = MaterialTheme.shapes.extraLarge,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = if (placeholder.isNotEmpty()) {
            { Text(placeholder) }
        } else null,
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = shape,
        colors = AppObjectsColors.outlinedTextFieldColors(
            backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            onBackgroundColor = MaterialTheme.colorScheme.onSurface,
            removeBorder = false,
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 2.dp),
    )
}

private fun String.blankToNull(): String? = takeIf { it.isNotBlank() }

private fun AddressData.isNotEmpty(): Boolean =
    listOfNotNull(street, houseNumber, city, postalCode, state, country, additionalInfo)
        .any { it.isNotBlank() }
