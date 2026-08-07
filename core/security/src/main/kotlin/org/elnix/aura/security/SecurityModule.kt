package org.elnix.aura.security

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object SecurityModule {
    @Provides
    @Singleton
    fun provideSecurityService(): SecurityService = SecurityServiceImpl()
}