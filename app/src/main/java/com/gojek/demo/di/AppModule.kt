package com.gojek.demo.di

import android.content.Context
import androidx.room.Room
import com.gojek.demo.data.NetworkUtils
import com.gojek.demo.data.local.database.DatabaseUtil
import com.gojek.demo.data.local.database.RepositoryDatabase
import com.gojek.demo.data.remote.ApiService
import com.gojek.demo.data.repositories.RepositoryRepoDataImpl
import com.gojek.demo.domain.RepositoryDataRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        val okHttpBuilder = OkHttpClient.Builder()
        val client = Retrofit.Builder().client(okHttpBuilder.build())

        return client.baseUrl(NetworkUtils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class MainDispatcher

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class IoDispatcher

    @Singleton
    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @MainDispatcher
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Singleton
    @Provides
    fun provideRepositoryDatabase(@ApplicationContext context: Context): RepositoryDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RepositoryDatabase::class.java,
            DatabaseUtil.DATABASE_NAME
        ).build()
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindRepository(impl: RepositoryRepoDataImpl): RepositoryDataRepo

}
