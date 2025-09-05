package com.example.aiassistant.di

import android.content.Context
import com.example.aiassistant.data.local.database.AppDatabase
import com.example.aiassistant.data.remote.websocket.WebSocketService
import com.example.aiassistant.domain.repository.AIRepository
import com.example.aiassistant.data.repository.AIRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideWebSocketService(okHttpClient: OkHttpClient): WebSocketService {
        return WebSocketService(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideAIRepository(
        webSocketService: WebSocketService,
        database: AppDatabase,
        gson: Gson
    ): AIRepository {
        return AIRepositoryImpl(webSocketService, database.chatDao(), gson)
    }
}
