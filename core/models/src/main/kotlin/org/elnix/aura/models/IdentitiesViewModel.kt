package org.elnix.aura.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.elnix.aura.database.entities.AddressEntity
import org.elnix.aura.database.entities.BirthdateEntity
import org.elnix.aura.database.entities.CustomNoteDetailEntity
import org.elnix.aura.database.entities.EmailEntity
import org.elnix.aura.database.entities.NameEntity
import org.elnix.aura.database.entities.PhoneEntity
import org.elnix.aura.database.entities.SurnameEntity
import org.elnix.aura.database.models.Identity
import org.elnix.aura.database.models.IdentityValues
import org.elnix.aura.database.repository.IdentityRepository
import org.elnix.aura.models.utils.viewModelInitialized
import javax.inject.Inject

@HiltViewModel
public class IdentitiesViewModel @Inject constructor(
    application: Application,
    private val repository: IdentityRepository,
) : AndroidViewModel(application) {

    public val identities: StateFlow<List<Identity>> = repository.observeAllIdentities()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    public val identitiesCount: StateFlow<Int> = repository.observeIdentitiesCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    public val emails: StateFlow<List<EmailEntity>> = repository.observeAllEmails()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    public val names: StateFlow<List<NameEntity>> = repository.observeAllNames()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    public val surnames: StateFlow<List<SurnameEntity>> = repository.observeAllSurnames()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    public val birthdates: StateFlow<List<BirthdateEntity>> = repository.observeAllBirthdates()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    public val addresses: StateFlow<List<AddressEntity>> = repository.observeAllAddresses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    public val phones: StateFlow<List<PhoneEntity>> = repository.observeAllPhones()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    public val customNoteDetails: StateFlow<List<CustomNoteDetailEntity>> = repository.observeAllCustomNoteDetails()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelInitialized()
    }

    public fun observeIdentity(id: Long): Flow<Identity?> = repository.observeIdentity(id)

    public fun createIdentity(
        values: IdentityValues,
        onResult: (Long) -> Unit = {},
    ) {
        viewModelScope.launch {
            onResult(repository.createIdentity(values))
        }
    }

    public fun updateIdentity(
        id: Long,
        values: IdentityValues,
        onResult: (Boolean) -> Unit = {},
    ) {
        viewModelScope.launch {
            onResult(repository.updateIdentity(id, values))
        }
    }

    public fun deleteIdentity(
        id: Long,
        onResult: () -> Unit = {},
    ) {
        viewModelScope.launch {
            repository.deleteIdentity(id)
            onResult()
        }
    }
}
