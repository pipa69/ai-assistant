package com.example.aiassistant.di

import com.example.aiassistant.domain.usecase.GetChatHistoryUseCase
import com.example.aiassistant.domain.usecase.SendMessageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideSendMessageUseCase(
        repository: com.example.aiassistant.domain.repository.AIRepository
    ): SendMessageUseCase {
        return SendMessageUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetChatHistoryUseCase(
        repository: com.example.aiassistant.domain.repository.AIRepository
    ): GetChatHistoryUseCase {
        return GetChatHistoryUseCase(repository)
    }
}
