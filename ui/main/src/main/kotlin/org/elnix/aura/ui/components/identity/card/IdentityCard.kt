package org.elnix.aura.ui.components.identity.card

import android.annotation.SuppressLint
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.elnix.aura.database.entities.AddressEntity
import org.elnix.aura.database.models.Identity
import org.elnix.aura.i18n.R
import org.elnix.aura.ktx.toColor
import org.elnix.aura.theme.AppObjectsColors
import org.elnix.aura.ui.base.components.Spacer
import org.elnix.aura.ui.base.compositionlocals.LocalCardColorLerpAmount
import org.elnix.aura.ui.dragon.components.DragonIconButton

/**
 * A single identity preview shown in the identities list.
 *
 * The card title resolves to the full name when available, and falls back to
 * the identity label. The primary info row (name, label) is always shown,
 * while email, phone, birthdate and the address are rendered as secondary lines.
 *
 * @param identity The identity to display.
 * @param onClick Invoked when the card is tapped (typically opens the editor).
 * @param onLongClick Invoked when the card is long-pressed (typically opens the delete confirmation).
 */
@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun IdentityCard(
    identity: Identity,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null
) {
    val linearInterpolationForCardsColor = LocalCardColorLerpAmount.current
    val defaultContainerColor = CardDefaults.cardColors().containerColor

    val backgroundColor = retain(identity.entity.color, linearInterpolationForCardsColor) {
        try {
            val idColor: Color = identity.entity.color?.toColor() ?: return@retain null
            lerp(idColor, defaultContainerColor, linearInterpolationForCardsColor)
        } catch (_: Exception) {
            null
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor ?: Color.Unspecified
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            IdentityCardTitle(identity)

            if (identity.entity.label != null) {
                Spacer(4.dp)
                Text(
                    text = identity.entity.label!!,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            identity.email?.email?.let { email ->
                Spacer(8.dp)
                IdentityCardLine(icon = R.drawable.alternate_email, text = email)
            }

            identity.phone?.phone?.let { phone ->
                IdentityCardLine(icon = R.drawable.android_cell_5, text = phone)
            }

            identity.birthdate?.birthdate?.let { birthdate ->
                IdentityCardLine(icon = R.drawable.schedule, text = birthdate)
            }

            identity.address?.let { address ->
                val summary = address.summary()
                if (summary != null) {
                    IdentityCardLine(icon = R.drawable.home, text = summary)
                }
            }

            Spacer(4.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                DragonIconButton(
                    icon = R.drawable.delete_forever,
                    contentDescription = R.string.delete_identity,
                    colors = AppObjectsColors.cancelIconButtonColors(),
                    onClick = onDelete
                )
            }
        }
    }
}

@Composable
private fun IdentityCardTitle(identity: Identity) {
    val fullName = listOfNotNull(identity.name?.name, identity.surname?.surname)
        .joinToString(separator = " ")

    Text(
        text = fullName.ifBlank { identity.entity.label ?: stringResource(R.string.identity_label) },
        style = MaterialTheme.typography.titleMediumEmphasized,
        maxLines = 1,
    )
}

@Composable
private fun IdentityCardLine(
    icon: Int,
    text: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.sizeIn(maxHeight = 16.dp, maxWidth = 16.dp),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
        )
    }
}

/**
 * Builds a single line address summary, e.g. "street houseNumber, postalCode city".
 * Returns null when every field is blank.
 */
internal fun AddressEntity.summary(): String? =
    listOfNotNull(
        listOfNotNull(street, houseNumber).joinToString(separator = " ").ifBlank { null },
        listOfNotNull(postalCode, city).joinToString(separator = " ").ifBlank { null },
        country?.takeIf { it.isNotBlank() },
    ).filter { it.isNotBlank() }
        .joinToString(separator = ", ")
        .takeIf { it.isNotEmpty() }

