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

//        private val MIGRATION_32_33: Migration = object : Migration(32, 33) {
//            override fun migrate(db: SupportSQLiteDatabase) {
//                db.execSQL("CREATE TABLE IF NOT EXISTS `emails` (`email` TEXT NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
//                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_emails_email` ON `emails` (`email`)")
//
//                db.execSQL("CREATE TABLE IF NOT EXISTS `names` (`name` TEXT NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
//                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_names_name` ON `names` (`name`)")
//
//                db.execSQL("CREATE TABLE IF NOT EXISTS `surnames` (`surname` TEXT NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
//                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_surnames_surname` ON `surnames` (`surname`)")
//
//                db.execSQL("CREATE TABLE IF NOT EXISTS `birthdates` (`birthdate` TEXT NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
//                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_birthdates_birthdate` ON `birthdates` (`birthdate`)")
//
//                db.execSQL("CREATE TABLE IF NOT EXISTS `phones` (`phone` TEXT NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
//                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_phones_phone` ON `phones` (`phone`)")
//
//                db.execSQL("CREATE TABLE IF NOT EXISTS `custom_note_details` (`note` TEXT NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
//                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_custom_note_details_note` ON `custom_note_details` (`note`)")
//
//                db.execSQL("CREATE TABLE IF NOT EXISTS `addresses` (`street` TEXT, `houseNumber` TEXT, `city` TEXT, `postalCode` TEXT, `state` TEXT, `country` TEXT, `additionalInfo` TEXT, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
//
//                db.execSQL(
//                    """
//                    CREATE TABLE IF NOT EXISTS `identities` (
//                      `label` TEXT,
//                      `emailId` INTEGER,
//                      `nameId` INTEGER,
//                      `surnameId` INTEGER,
//                      `birthdateId` INTEGER,
//                      `addressId` INTEGER,
//                      `phoneId` INTEGER,
//                      `customNoteDetailId` INTEGER,
//                      `createdAt` INTEGER NOT NULL,
//                      `updatedAt` INTEGER NOT NULL,
//                      `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
//                      FOREIGN KEY(`emailId`) REFERENCES `emails`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
//                      FOREIGN KEY(`nameId`) REFERENCES `names`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
//                      FOREIGN KEY(`surnameId`) REFERENCES `surnames`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
//                      FOREIGN KEY(`birthdateId`) REFERENCES `birthdates`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
//                      FOREIGN KEY(`addressId`) REFERENCES `addresses`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
//                      FOREIGN KEY(`phoneId`) REFERENCES `phones`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
//                      FOREIGN KEY(`customNoteDetailId`) REFERENCES `custom_note_details`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
//                    )
//                    """.trimIndent(),
//                )
//                db.execSQL("CREATE INDEX IF NOT EXISTS `index_identities_emailId` ON `identities` (`emailId`)")
//                db.execSQL("CREATE INDEX IF NOT EXISTS `index_identities_nameId` ON `identities` (`nameId`)")
//                db.execSQL("CREATE INDEX IF NOT EXISTS `index_identities_surnameId` ON `identities` (`surnameId`)")
//                db.execSQL("CREATE INDEX IF NOT EXISTS `index_identities_birthdateId` ON `identities` (`birthdateId`)")
//                db.execSQL("CREATE INDEX IF NOT EXISTS `index_identities_addressId` ON `identities` (`addressId`)")
//                db.execSQL("CREATE INDEX IF NOT EXISTS `index_identities_phoneId` ON `identities` (`phoneId`)")
//                db.execSQL("CREATE INDEX IF NOT EXISTS `index_identities_customNoteDetailId` ON `identities` (`customNoteDetailId`)")
//            }
//        }

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
