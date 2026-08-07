package org.elnix.aura.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.elnix.aura.database.entities.AddressEntity

@Dao
public interface AddressDao {
    @Insert
    public suspend fun insert(address: AddressEntity): Long

    @Query("SELECT * FROM addresses ORDER BY city ASC")
    public fun getAll(): Flow<List<AddressEntity>>

    @Query(
        """
        SELECT * FROM addresses
        WHERE street IS :street
          AND houseNumber IS :houseNumber
          AND city IS :city
          AND postalCode IS :postalCode
          AND state IS :state
          AND country IS :country
          AND additionalInfo IS :additionalInfo
        LIMIT 1
        """,
    )
    public suspend fun getByMatch(
        street: String?,
        houseNumber: String?,
        city: String?,
        postalCode: String?,
        state: String?,
        country: String?,
        additionalInfo: String?,
    ): AddressEntity?

    @Query("SELECT * FROM addresses WHERE id = :id LIMIT 1")
    public suspend fun getById(id: Long): AddressEntity?

    @Query("DELETE FROM addresses WHERE id = :id")
    public suspend fun deleteById(id: Long)
}
