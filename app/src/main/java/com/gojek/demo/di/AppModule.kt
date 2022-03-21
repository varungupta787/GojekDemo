package com.gojek.demo.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.gojek.demo.data.NetworkUtils
import com.gojek.demo.data.local.database.DatabaseUtil
import com.gojek.demo.data.local.database.RepositoryDatabase
import com.gojek.demo.data.remote.ApiService
import com.gojek.demo.data.repositories.RepositoryRepoDataImpl
import com.gojek.demo.domain.RepositoryDataRepo
import com.gojek.demo.domain.usercase.BaseUseCase
import com.gojek.demo.domain.usercase.RepoDataUsecase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(NetworkUtils.BASE_URL)
            .build()
            .create(ApiService::class.java)
    }

}


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @Singleton
    @Provides
    fun provideRepositoryDatabase(@ApplicationContext context: Context): RepositoryDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RepositoryDatabase::class.java,
            DatabaseUtil.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideRepoDataUsecase(
        dispatcher: CoroutineDispatcher,
        repositoryDataRepo: RepositoryDataRepo,
        repositoryDatabase: RepositoryDatabase,
    ): RepoDataUsecase {
        return RepoDataUsecase(dispatcher, repositoryDataRepo, repositoryDatabase)
    }

    @Singleton
    @Provides
    fun provideRepositoryRepoDataImpl(
        apiService: ApiService,
        dispatcher: CoroutineDispatcher
    ): RepositoryDataRepo {
        return RepositoryRepoDataImpl(apiService, dispatcher)
    }
}

/*
@InstallIn(ActivityComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(impl: RepositoryRepoDataImpl): RepositoryDataRepo

}*/
