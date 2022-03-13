package com.world.edvora_assignment.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.world.edvora_assignment.Networking.Destination
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
/*

Retrofit Module
This Module puts our retrofit functionaliy in the objects graph
So that it can be used later

 */

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideGsonBuilder() : Gson {
        return GsonBuilder()
            .create()
    }


    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit.Builder{
        return Retrofit.Builder()
            .baseUrl("https://assessment.api.vweb.app/")
            .addConverterFactory(GsonConverterFactory.create(gson))


    }

    @Singleton
    @Provides
    fun provideRetofitService(retrofit : Retrofit.Builder) : Destination {
        return retrofit
            .build()
            .create(Destination::class.java)
    }


}