package org.elnix.aura.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.elnix.aura.database.entities.IdentityEntity
import org.elnix.aura.database.models.Identity

@Dao
public interface IdentityDao {
    @Insert
    public suspend fun insert(identity: IdentityEntity)

    @Update
    public suspend fun update(identity: IdentityEntity)

    @Delete
    public suspend fun delete(identity: IdentityEntity)

    @Query("DELETE FROM identities WHERE id = :id")
    public suspend fun deleteById(id: Long)

    @Query("SELECT * FROM identities WHERE id = :id LIMIT 1")
    public suspend fun getEntity(id: Long): IdentityEntity?

    @Transaction
    @Query("SELECT * FROM identities ORDER BY updatedAt DESC")
    public fun getAllIdentities(): Flow<List<Identity>>

    @Transaction
    @Query("SELECT * FROM identities WHERE id = :id")
    public fun getIdentity(id: Long): Flow<Identity?>

    @Transaction
    @Query("SELECT * FROM identities WHERE id = :id")
    public suspend fun getIdentityById(id: Long): Identity?

    @Query("SELECT COUNT(*) FROM identities")
    public fun observeCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM identities WHERE emailId = :id")
    public suspend fun countIdentitiesUsingEmailId(id: Long): Int

    @Query("SELECT COUNT(*) FROM identities WHERE nameId = :id")
    public suspend fun countIdentitiesUsingNameId(id: Long): Int

    @Query("SELECT COUNT(*) FROM identities WHERE surnameId = :id")
    public suspend fun countIdentitiesUsingSurnameId(id: Long): Int

    @Query("SELECT COUNT(*) FROM identities WHERE birthdateId = :id")
    public suspend fun countIdentitiesUsingBirthdateId(id: Long): Int

    @Query("SELECT COUNT(*) FROM identities WHERE addressId = :id")
    public suspend fun countIdentitiesUsingAddressId(id: Long): Int

    @Query("SELECT COUNT(*) FROM identities WHERE phoneId = :id")
    public suspend fun countIdentitiesUsingPhoneId(id: Long): Int

    @Query("SELECT COUNT(*) FROM identities WHERE customNoteDetailId = :id")
    public suspend fun countIdentitiesUsingCustomNoteDetailId(id: Long): Int
}
