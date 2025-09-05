package com.example.aiassistant.domain.usecase

import com.example.aiassistant.domain.model.ChatMessage
import com.example.aiassistant.domain.repository.AIRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetChatHistoryUseCase @Inject constructor(
    private val repository: AIRepository
) {
    operator fun invoke(): Flow<List<ChatMessage>> {
        return flow {
            while (true) {
                emit(repository.getChatHistory())
                kotlinx.coroutines.delay(1000)
            }
        }
    }
}
