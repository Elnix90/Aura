package org.elnix.aura.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import org.elnix.aura.database.models.AddressData
import org.elnix.aura.database.models.IdentityValues
import org.elnix.aura.database.remote.NearbyAddressResult
import org.elnix.aura.i18n.R
import org.elnix.aura.ktx.randomColor
import org.elnix.aura.ktx.showToast
import org.elnix.aura.ktx.toColor
import org.elnix.aura.ktx.toHexWithAlpha
import org.elnix.aura.models.IdentitiesViewModel
import org.elnix.aura.theme.AppObjectsColors
import org.elnix.aura.ui.base.activityViewModel
import org.elnix.aura.ui.base.components.AnimatedFab
import org.elnix.aura.ui.base.components.Spacer
import org.elnix.aura.ui.base.compositionlocals.LocalNavigator
import org.elnix.aura.ui.base.modifiers.conditional
import org.elnix.aura.ui.components.burger.MoreOptions
import org.elnix.aura.ui.components.identity.address.RandomAddressSection
import org.elnix.aura.ui.components.identity.common.IdentityTextField
import org.elnix.aura.ui.components.identity.date.BirthdateField
import org.elnix.aura.ui.components.identity.email.EmailField
import org.elnix.aura.ui.components.identity.note.NotesField
import org.elnix.aura.ui.components.identity.phone.PhoneField
import org.elnix.aura.ui.dialogs.ColorPickerDialog
import org.elnix.aura.ui.dragon.components.DragonButton
import org.elnix.aura.ui.dragon.components.DragonSettingsGroup
import org.elnix.aura.ui.dragon.components.ResetIcon
import org.elnix.aura.ui.dragon.expandable.ExpandableSection
import org.elnix.aura.ui.dragon.expandable.rememberExpandableSection
import org.elnix.aura.base.model.ExpandableSectionMode
import org.elnix.aura.ui.helpers.settings.BaseSettingsTitle
import org.elnix.aura.ui.helpers.settings.SettingsScaffold


@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdentityEditorScreen(
    isCreatingNew: Boolean,
    initialValues: IdentityValues,
    identitiesViewModel: IdentitiesViewModel = activityViewModel(),
    onSave: (IdentityValues) -> Unit,
) {
    val ctx = LocalContext.current
    val navigator = LocalNavigator.current
    val randomIdentityProvider = identitiesViewModel.randomIdentityProvider

    var editIdentity by retain(initialValues) { mutableStateOf(initialValues) }

    val editColor = retain(editIdentity.color) {
        try {
            editIdentity.color?.toColor() ?: return@retain null
        } catch (_: Exception) {
            null
        }
    }

    var isLoadingNearby by remember { mutableStateOf(false) }
    var shouldRequestPermission by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }

    val addressSection = rememberExpandableSection(
        title = stringResource(R.string.identity_address),
        mode = ExpandableSectionMode.Expandable,
    )

    @SuppressLint("LocalContextGetResourceValueCall")
    fun fetchNearbyAddress() {
        if (isLoadingNearby) return
        isLoadingNearby = true
        identitiesViewModel.randomNearbyAddress { result ->
            isLoadingNearby = false
            when (result) {
                is NearbyAddressResult.Success -> {
                    val newAddress = editIdentity.address?.copy(
                        street = result.address.street,
                        houseNumber = result.address.houseNumber,
                        city = result.address.city,
                        postalCode = result.address.postalCode,
                        state = result.address.state,
                        country = result.address.country,
                        additionalInfo = result.address.additionalInfo
                    )

                    editIdentity = editIdentity.copy(address = newAddress)
                    ctx.showToast(ctx.getString(R.string.address_generated))
                }

                NearbyAddressResult.LocationUnavailable ->
                    ctx.showToast(ctx.getString(R.string.location_unavailable))

                NearbyAddressResult.NoAddressFound ->
                    ctx.showToast(ctx.getString(R.string.no_address_found))

                NearbyAddressResult.ServiceUnreachable ->
                    ctx.showToast(ctx.getString(R.string.address_service_unreachable))
            }
        }
    }

    fun onRandomAddressClicked() {
        val hasPermission =
            ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            fetchNearbyAddress()
        } else {
            shouldRequestPermission = true
        }
    }

    val requestLocationPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        if (grants.values.any { it }) {
            fetchNearbyAddress()
        } else {
            ctx.showToast(ctx.getString(R.string.location_permission_required))
        }
    }

    LaunchedEffect(shouldRequestPermission) {
        if (shouldRequestPermission) {
            requestLocationPermission.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            )
            shouldRequestPermission = false
        }
    }

    fun randomizeAll() {
        with(randomIdentityProvider) {
            editIdentity = editIdentity.randomizeAll()
        }
    }

    SettingsScaffold(
        title = "",
        helpText = null,
        onReset = null,
        resetText = null,
        specialSettingsTitle = {
            BaseSettingsTitle(
                title = stringResource(if (isCreatingNew) R.string.create_new_identity else R.string.edit_identity),
                onBack = navigator::onBack,
                moreOptions = { dismiss ->
                    listOf(
                        MoreOptions(
                            text = { stringResource(R.string.randomize_all) },
                            onClick = {
                                randomizeAll()
                                dismiss()
                            },
                            icon = R.drawable.shuffle,
                        )
                    )
                }
            ) {
                AnimatedFab(
                    onClick = { onSave(editIdentity) },
                    icon = R.drawable.save,
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            val focusRequester = remember { FocusRequester() }

            LaunchedEffect(Unit) {
                if (isCreatingNew) {
                    focusRequester.requestFocus()
                }
            }

            OutlinedTextField(
                value = editIdentity.label.orEmpty(),
                onValueChange = { editIdentity = editIdentity.copy(label = it) },
                label = {
                    Text(stringResource(R.string.identity_label))
                },
                placeholder = {
                    Text(stringResource(R.string.what_is_this_identity_for))
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                singleLine = true,
                shape = CircleShape,
                colors = AppObjectsColors.outlinedTextFieldColors(
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    onBackgroundColor = MaterialTheme.colorScheme.onSurface,
                    removeBorder = true
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 2.dp)
                    .focusRequester(focusRequester)
            )


            DragonSettingsGroup(R.string.basic_infos) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    IdentityTextField(
                        value = editIdentity.name.orEmpty(),
                        onValueChange = { editIdentity = editIdentity.copy(name = it) },
                        onShuffle = { editIdentity = editIdentity.copy(name = randomIdentityProvider.randomName()) },
                        label = stringResource(R.string.identity_name),
                        modifier = Modifier.weight(1f),
                    )

                    IdentityTextField(
                        value = editIdentity.surname.orEmpty(),
                        onValueChange = { editIdentity = editIdentity.copy(surname = it) },
                        onShuffle = { editIdentity = editIdentity.copy(surname = randomIdentityProvider.randomSurname()) },
                        label = stringResource(R.string.identity_surname),
                        modifier = Modifier.weight(1f),
                    )
                }

                EmailField(
                    value = editIdentity.email.orEmpty(),
                    onValueChange = { editIdentity = editIdentity.copy(email = it) },
                    onShuffle = { editIdentity = editIdentity.copy(email = randomIdentityProvider.randomEmail()) }
                )

                PhoneField(
                    value = editIdentity.phone.orEmpty(),
                    onValueChange = { editIdentity = editIdentity.copy(phone = it) },
                    onShuffle = { editIdentity = editIdentity.copy(phone = randomIdentityProvider.randomPhone()) },
                )
            }

            DragonSettingsGroup(R.string.identity_birthdate) {
                BirthdateField(
                    value = editIdentity.birthdate.orEmpty(),
                    onValueChange = { editIdentity = editIdentity.copy(birthdate = it) },
                    onShuffle = { editIdentity = editIdentity.copy(birthdate = randomIdentityProvider.randomBirthdate()) },
                )
            }

            DragonSettingsGroup(R.string.color) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DragonButton(
                        onClick = { showColorPicker = true },
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.colorize_filled),
                            contentDescription = null
                        )
                        Spacer(5.dp)
                        Text(stringResource(R.string.color_selector))
                    }

                    ResetIcon {
                        editIdentity = editIdentity.copy(color = null)
                    }

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .conditional(editColor != null, fallback = {
                                background(
                                    Brush.sweepGradient(
                                        listOf(
                                            Color.Black,
                                            Color.Red,
                                            Color.White,
                                            Color.Green,
                                            Color(0xFF930093),
                                            Color.Blue,
                                            Color.Black
                                        )
                                    ),
                                    CircleShape
                                )
                            }) {
                                background(editColor!!, CircleShape)
                            }
                            .border(2.dp, Color.White, CircleShape)
                            .clickable {
                                editIdentity = editIdentity.copy(color = randomColor().toHexWithAlpha)
                            }
                            .padding(10.dp)
                    )
                }
            }

            DragonSettingsGroup(R.string.identity_address) {
                ExpandableSection(addressSection) {

                    val address by rememberUpdatedState(editIdentity.address)
                    fun editAddress(new: (old: AddressData) -> AddressData) {
                        val newAddress = new(editIdentity.address ?: AddressData())
                        editIdentity = editIdentity.copy(address = newAddress)
                    }

                    RandomAddressSection(
                        onGenerate = ::onRandomAddressClicked,
                        isLoading = isLoadingNearby,
                    )

                    IdentityTextField(
                        value = editIdentity.name.orEmpty(),
                        onValueChange = { editIdentity = editIdentity.copy(name = it) },
                        onShuffle = { editIdentity = editIdentity.copy(name = randomIdentityProvider.randomName()) },
                        label = stringResource(R.string.identity_address_street),
                        modifier = Modifier.weight(1f),
                    )

                    IdentityTextField(
                        value = address?.houseNumber.orEmpty(),
                        onValueChange = { editAddress { old -> old.copy(houseNumber = it) } },
                        onShuffle = { editAddress { old -> old.copy(houseNumber = randomIdentityProvider.randomHouseNumber()) } },
                        label = stringResource(R.string.identity_address_house_number),
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f),
                    )


                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        IdentityTextField(
                            value = address?.postalCode.orEmpty(),
                            onValueChange = { editAddress { old -> old.copy(postalCode = it) } },
                            onShuffle = { editAddress { old -> old.copy(postalCode = randomIdentityProvider.randomPostalCode()) } },
                            label = stringResource(R.string.identity_address_postal_code),
                            modifier = Modifier.weight(1f),
                        )


                        IdentityTextField(
                            value = address?.city.orEmpty(),
                            onValueChange = { editAddress { old -> old.copy(city = it) } },
                            onShuffle = { editAddress { old -> old.copy(city = randomIdentityProvider.randomCity()) } },
                            label = stringResource(R.string.identity_address_city),
                            modifier = Modifier.weight(1f),
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        IdentityTextField(
                            value = address?.state.orEmpty(),
                            onValueChange = { editAddress { old -> old.copy(state = it) } },
                            onShuffle = { editAddress { old -> old.copy(state = randomIdentityProvider.randomCountry()) } },
                            label = stringResource(R.string.identity_address_state),
                            modifier = Modifier.weight(1f),
                        )

                        IdentityTextField(
                            value = address?.country.orEmpty(),
                            onValueChange = { editAddress { old -> old.copy(country = it) } },
                            onShuffle = { editAddress { old -> old.copy(country = randomIdentityProvider.randomCountry()) } },
                            label = stringResource(R.string.identity_address_country),
                            modifier = Modifier.weight(1f),
                        )
                    }

                    IdentityTextField(
                        value = address?.additionalInfo.orEmpty(),
                        onValueChange = { editAddress { old -> old.copy(additionalInfo = it) } },
                        onShuffle = null,
                        label = stringResource(R.string.identity_address_additional_info),
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            NotesField(
                value = editIdentity.customNoteDetail.orEmpty(),
                onValueChange = { editIdentity = editIdentity.copy(customNoteDetail = it) }
            )
        }
    }

    if (showColorPicker) {
        ColorPickerDialog(
            initialColor = editColor,
            onDismissRequest = { showColorPicker = false }
        ) {
            editIdentity = editIdentity.copy(color = it.toHexWithAlpha)
        }
    }
}

//private fun buildEmail(localPart: String, domain: String): String? = when {
//    localPart.isNotBlank() && domain.isNotBlank() -> "$localPart@$domain"
//    localPart.isNotBlank() -> localPart
//    else -> domain.blankToNull()
//}
//
//private fun String.blankToNull(): String? = takeIf { it.isNotBlank() }
//
//private fun AddressData.isNotEmpty(): Boolean =
//    listOfNotNull(street, houseNumber, city, postalCode, state, country, additionalInfo)
//        .any { it.isNotBlank() }
