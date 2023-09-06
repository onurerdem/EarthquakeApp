package com.onurerdem.earthquakeapp.data.di

import com.onurerdem.earthquakeapp.data.remote.EarthquakeAPI
import com.onurerdem.earthquakeapp.data.repository.EarthquakeRepositoryImpl
import com.onurerdem.earthquakeapp.domain.repository.EarthquakeRepository
import com.onurerdem.earthquakeapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideEarthquakeApi(): EarthquakeAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EarthquakeAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideEarthquakeRepository(api : EarthquakeAPI) : EarthquakeRepository {
        return EarthquakeRepositoryImpl(api = api)
    }
}