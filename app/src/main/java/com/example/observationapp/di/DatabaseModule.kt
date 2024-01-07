package com.example.observationapp.di

import android.app.Application
import androidx.room.Room
import com.example.observationapp.repository.database.ProjectDao
import com.example.observationapp.util.ApplicationDBTables
import com.example.observationapp.util.ObservationDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(application: Application): ObservationDB {
        return Room
            .databaseBuilder(
                application,
                ObservationDB::class.java,
                ApplicationDBTables.DATABASE_NAME
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideProjectDao(database: ObservationDB): ProjectDao {
        return database.projectDao()
    }

    /* @Provides
     fun provideEntity() = ProjectModelItem()*/
}