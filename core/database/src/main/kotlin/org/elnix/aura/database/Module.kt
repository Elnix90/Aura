package org.elnix.aura.database
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.elnix.aura.database.daos.AddressDao
import org.elnix.aura.database.daos.BirthdateDao
import org.elnix.aura.database.daos.CustomNoteDetailDao
import org.elnix.aura.database.daos.EmailDao
import org.elnix.aura.database.daos.IdentityDao
import org.elnix.aura.database.daos.NameDao
import org.elnix.aura.database.daos.PhoneDao
import org.elnix.aura.database.daos.SurnameDao
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext ctx: Context): AppDatabase =
        AppDatabase.getInstance(ctx)

    @Provides
    @Singleton
    fun provideEmailDao(db: AppDatabase): EmailDao = db.emailDao()

    @Provides
    @Singleton
    fun provideNameDao(db: AppDatabase): NameDao = db.nameDao()

    @Provides
    @Singleton
    fun provideSurnameDao(db: AppDatabase): SurnameDao = db.surnameDao()

    @Provides
    @Singleton
    fun provideBirthdateDao(db: AppDatabase): BirthdateDao = db.birthdateDao()

    @Provides
    @Singleton
    fun provideAddressDao(db: AppDatabase): AddressDao = db.addressDao()

    @Provides
    @Singleton
    fun providePhoneDao(db: AppDatabase): PhoneDao = db.phoneDao()

    @Provides
    @Singleton
    fun provideCustomNoteDetailDao(db: AppDatabase): CustomNoteDetailDao = db.customNoteDetailDao()

    @Provides
    @Singleton
    fun provideIdentityDao(db: AppDatabase): IdentityDao = db.identityDao()
}
