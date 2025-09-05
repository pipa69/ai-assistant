package com.example.aiassistant.domain.usecase

import com.example.aiassistant.domain.model.ChatMessage
import com.example.aiassistant.domain.repository.AIRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: AIRepository
) {
    suspend operator fun invoke(message: String): Result<Unit> {
        val chatMessage = ChatMessage(
            content = message,
            sender = ChatMessage.SenderType.USER
        )
        return repository.sendMessage(chatMessage)
    }
}
