package com.world.edvora_assignment.di

import com.world.edvora_assignment.Networking.Destination
import com.world.edvora_assignment.repository.RidesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RideRepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        destination : Destination,
    ) : RidesRepository {
        return RidesRepository(destination )
    }


}