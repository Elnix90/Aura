@file:Suppress("ClassName")

package org.elnix.aura.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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

@Database(
    entities = [
        EmailEntity::class,
        NameEntity::class,
        SurnameEntity::class,
        BirthdateEntity::class,
        AddressEntity::class,
        PhoneEntity::class,
        CustomNoteDetailEntity::class,
        IdentityEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
public abstract class AppDatabase : RoomDatabase() {
    public abstract fun emailDao(): EmailDao
    public abstract fun nameDao(): NameDao
    public abstract fun surnameDao(): SurnameDao
    public abstract fun birthdateDao(): BirthdateDao
    public abstract fun addressDao(): AddressDao
    public abstract fun phoneDao(): PhoneDao
    public abstract fun customNoteDetailDao(): CustomNoteDetailDao
    public abstract fun identityDao(): IdentityDao

    public companion object {
        @Volatile
        private var _instance: AppDatabase? = null

        public fun getInstance(context: Context): AppDatabase {
            val instance = _instance
                ?: Room
                    .databaseBuilder(context.applicationContext, AppDatabase::class.java, "room")
                    .build()
            if (_instance == null) _instance = instance
            return instance
        }
    }
}
