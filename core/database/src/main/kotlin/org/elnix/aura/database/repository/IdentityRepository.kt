package org.elnix.aura.database.repository

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import org.elnix.aura.database.AppDatabase
import org.elnix.aura.database.daos.AddressDao
import org.elnix.aura.database.daos.BirthdateDao
import org.elnix.aura.database.daos.CustomNoteDetailDao
import org.elnix.aura.database.daos.EmailDao
import org.elnix.aura.database.daos.IdentityDao
import org.elnix.aura.database.daos.NameDao
import org.elnix.aura.database.daos.PhoneDao
import org.elnix.aura.database.daos.SurnameDao
import org.elnix.aura.database.entities.AddressEntity
import org.elnix.aura.database.entities.BirthdateEntity
import org.elnix.aura.database.entities.CustomNoteDetailEntity
import org.elnix.aura.database.entities.EmailEntity
import org.elnix.aura.database.entities.IdentityEntity
import org.elnix.aura.database.entities.NameEntity
import org.elnix.aura.database.entities.PhoneEntity
import org.elnix.aura.database.entities.SurnameEntity
import org.elnix.aura.database.models.AddressData
import org.elnix.aura.database.models.Identity
import org.elnix.aura.database.models.IdentityValues
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class IdentityRepository @Inject constructor(
    private val database: AppDatabase,
    private val emailDao: EmailDao,
    private val nameDao: NameDao,
    private val surnameDao: SurnameDao,
    private val birthdateDao: BirthdateDao,
    private val addressDao: AddressDao,
    private val phoneDao: PhoneDao,
    private val customNoteDetailDao: CustomNoteDetailDao,
    private val identityDao: IdentityDao,
) {

    public fun observeAllIdentities(): Flow<List<Identity>> = identityDao.getAllIdentities()

    public fun observeIdentity(id: Long): Flow<Identity?> = identityDao.getIdentity(id)

    public fun observeIdentitiesCount(): Flow<Int> = identityDao.observeCount()

    public fun observeAllEmails(): Flow<List<EmailEntity>> = emailDao.getAll()

    public fun observeAllNames(): Flow<List<NameEntity>> = nameDao.getAll()

    public fun observeAllSurnames(): Flow<List<SurnameEntity>> = surnameDao.getAll()

    public fun observeAllBirthdates(): Flow<List<BirthdateEntity>> = birthdateDao.getAll()

    public fun observeAllAddresses(): Flow<List<AddressEntity>> = addressDao.getAll()

    public fun observeAllPhones(): Flow<List<PhoneEntity>> = phoneDao.getAll()

    public fun observeAllCustomNoteDetails(): Flow<List<CustomNoteDetailEntity>> = customNoteDetailDao.getAll()

    public suspend fun getIdentity(id: Long): Identity? = identityDao.getIdentityById(id)

    public suspend fun createIdentity(values: IdentityValues): Boolean = database.withTransaction {
        val references = resolveReferences(values)
        val now = System.currentTimeMillis()
        identityDao.insert(
            IdentityEntity(
                label = values.label?.trim()?.takeIf { it.isNotEmpty() },
                emailId = references.emailId,
                nameId = references.nameId,
                surnameId = references.surnameId,
                birthdateId = references.birthdateId,
                addressId = references.addressId,
                phoneId = references.phoneId,
                customNoteDetailId = references.customNoteDetailId,
                createdAt = now,
                updatedAt = now,
            ),
        )
        true
    }

    public suspend fun updateIdentity(id: Long, values: IdentityValues): Boolean = database.withTransaction {
        val current = identityDao.getEntity(id) ?: return@withTransaction false
        val references = resolveReferences(values)
        val updated = current.copy(
            label = values.label?.trim()?.takeIf { it.isNotEmpty() },
            emailId = references.emailId,
            nameId = references.nameId,
            surnameId = references.surnameId,
            birthdateId = references.birthdateId,
            addressId = references.addressId,
            phoneId = references.phoneId,
            customNoteDetailId = references.customNoteDetailId,
            updatedAt = System.currentTimeMillis(),
        )
        identityDao.update(updated)
        cleanupOrphans(current, updated)
        true
    }

    public suspend fun deleteIdentity(id: Long) {
        database.withTransaction {
            val current = identityDao.getEntity(id) ?: return@withTransaction
            identityDao.deleteById(id)
            cleanupOrphans(current, null)
        }
    }

    private suspend fun resolveReferences(values: IdentityValues): References = References(
        emailId = resolveEmail(values.email),
        nameId = resolveName(values.name),
        surnameId = resolveSurname(values.surname),
        birthdateId = resolveBirthdate(values.birthdate),
        addressId = resolveAddress(values.address),
        phoneId = resolvePhone(values.phone),
        customNoteDetailId = resolveCustomNoteDetail(values.customNoteDetail),
    )

    private suspend fun resolveEmail(email: String?): Long? {
        val value = email?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        val normalized = value.lowercase(Locale.ROOT)
        emailDao.getByValue(normalized)?.let { return it.id }
        return emailDao.insert(EmailEntity(email = normalized))
    }

    private suspend fun resolveName(name: String?): Long? {
        val value = name?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        nameDao.getByValue(value)?.let { return it.id }
        return nameDao.insert(NameEntity(name = value))
    }

    private suspend fun resolveSurname(surname: String?): Long? {
        val value = surname?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        surnameDao.getByValue(value)?.let { return it.id }
        return surnameDao.insert(SurnameEntity(surname = value))
    }

    private suspend fun resolveBirthdate(birthdate: String?): Long? {
        val value = birthdate?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        birthdateDao.getByValue(value)?.let { return it.id }
        return birthdateDao.insert(BirthdateEntity(birthdate = value))
    }

    private suspend fun resolvePhone(phone: String?): Long? {
        val value = phone?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        phoneDao.getByValue(value)?.let { return it.id }
        return phoneDao.insert(PhoneEntity(phone = value))
    }

    private suspend fun resolveCustomNoteDetail(note: String?): Long? {
        val value = note?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        customNoteDetailDao.getByValue(value)?.let { return it.id }
        return customNoteDetailDao.insert(CustomNoteDetailEntity(note = value))
    }

    private suspend fun resolveAddress(address: AddressData?): Long? {
        val street = address?.street?.trim()?.takeIf { it.isNotEmpty() }
        val houseNumber = address?.houseNumber?.trim()?.takeIf { it.isNotEmpty() }
        val city = address?.city?.trim()?.takeIf { it.isNotEmpty() }
        val postalCode = address?.postalCode?.trim()?.takeIf { it.isNotEmpty() }
        val state = address?.state?.trim()?.takeIf { it.isNotEmpty() }
        val country = address?.country?.trim()?.takeIf { it.isNotEmpty() }
        val additionalInfo = address?.additionalInfo?.trim()?.takeIf { it.isNotEmpty() }
        if (street == null && houseNumber == null && city == null && postalCode == null &&
            state == null && country == null && additionalInfo == null
        ) {
            return null
        }
        addressDao.getByMatch(street, houseNumber, city, postalCode, state, country, additionalInfo)
            ?.let { return it.id }
        return addressDao.insert(
            AddressEntity(
                street = street,
                houseNumber = houseNumber,
                city = city,
                postalCode = postalCode,
                state = state,
                country = country,
                additionalInfo = additionalInfo,
            ),
        )
    }

    private suspend fun cleanupOrphans(previous: IdentityEntity, current: IdentityEntity?) {
        cleanupValue(previous.emailId, current?.emailId, identityDao::countIdentitiesUsingEmailId, emailDao::deleteById)
        cleanupValue(previous.nameId, current?.nameId, identityDao::countIdentitiesUsingNameId, nameDao::deleteById)
        cleanupValue(previous.surnameId, current?.surnameId, identityDao::countIdentitiesUsingSurnameId, surnameDao::deleteById)
        cleanupValue(previous.birthdateId, current?.birthdateId, identityDao::countIdentitiesUsingBirthdateId, birthdateDao::deleteById)
        cleanupValue(previous.addressId, current?.addressId, identityDao::countIdentitiesUsingAddressId, addressDao::deleteById)
        cleanupValue(previous.phoneId, current?.phoneId, identityDao::countIdentitiesUsingPhoneId, phoneDao::deleteById)
        cleanupValue(previous.customNoteDetailId, current?.customNoteDetailId, identityDao::countIdentitiesUsingCustomNoteDetailId, customNoteDetailDao::deleteById)
    }

    private suspend fun cleanupValue(
        previousId: Long?,
        currentId: Long?,
        countUsage: suspend (Long) -> Int,
        deleteById: suspend (Long) -> Unit,
    ) {
        if (previousId != null && previousId != currentId && countUsage(previousId) == 0) {
            deleteById(previousId)
        }
    }

    private data class References(
        val emailId: Long? = null,
        val nameId: Long? = null,
        val surnameId: Long? = null,
        val birthdateId: Long? = null,
        val addressId: Long? = null,
        val phoneId: Long? = null,
        val customNoteDetailId: Long? = null,
    )
}
