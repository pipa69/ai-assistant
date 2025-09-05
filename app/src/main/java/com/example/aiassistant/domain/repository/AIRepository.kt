package com.example.aiassistant.domain.repository

import com.example.aiassistant.domain.model.ChatMessage
import com.example.aiassistant.domain.model.ConnectionState
import com.example.aiassistant.domain.model.VoiceResult
import kotlinx.coroutines.flow.Flow

interface AIRepository {
    val connectionState: Flow<ConnectionState>
    val incomingMessages: Flow<ChatMessage>
    
    suspend fun connect()
    suspend fun disconnect()
    suspend fun sendMessage(message: ChatMessage): Result<Unit>
    suspend fun getChatHistory(): List<ChatMessage>
    suspend fun clearHistory()
    
    suspend fun startListening(): Flow<VoiceResult>
    suspend fun stopListening()
    suspend fun speak(text: String): Result<Unit>
}
