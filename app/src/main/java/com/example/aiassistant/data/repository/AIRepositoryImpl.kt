package com.example.aiassistant.data.repository

import com.example.aiassistant.data.local.database.ChatDao
import com.example.aiassistant.data.remote.websocket.WebSocketService
import com.example.aiassistant.domain.model.ChatMessage
import com.example.aiassistant.domain.model.ConnectionState
import com.example.aiassistant.domain.model.VoiceResult
import com.example.aiassistant.domain.repository.AIRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AIRepositoryImpl @Inject constructor(
    private val webSocketService: WebSocketService,
    private val chatDao: ChatDao,
    private val gson: Gson
) : AIRepository {

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected())
    override val connectionState: Flow<ConnectionState> = _connectionState

    override val incomingMessages: Flow<ChatMessage> = webSocketService.incomingMessages
        .map { gson.fromJson(it, ChatMessage::class.java) }
        .onEach { message ->
            chatDao.insert(message.toEntity())
        }

    override suspend fun connect() {
        // Implementacja połączenia WebSocket
        _connectionState.value = ConnectionState.Connecting
        try {
            // Tutaj logika połączenia
            _connectionState.value = ConnectionState.Connected
        } catch (e: Exception) {
            _connectionState.value = ConnectionState.Error(e)
        }
    }

    override suspend fun disconnect() {
        _connectionState.value = ConnectionState.Disconnected("User disconnected")
    }

    override suspend fun sendMessage(message: ChatMessage): Result<Unit> {
        return try {
            val jsonMessage = gson.toJson(message)
            webSocketService.sendMessage(jsonMessage)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getChatHistory(): List<ChatMessage> {
        return emptyList() // Tymczasowa implementacja
    }

    override suspend fun clearHistory() {
        chatDao.clearAll()
    }

    override suspend fun startListening(): Flow<VoiceResult> {
        TODO("Not yet implemented")
    }

    override suspend fun stopListening() {
        TODO("Not yet implemented")
    }

    override suspend fun speak(text: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    private fun ChatMessage.toEntity() = com.example.aiassistant.data.local.database.entities.ChatMessageEntity(
        id = id,
        content = content,
        sender = sender.name,
        timestamp = timestamp,
        type = type.name,
        status = status.name,
        metadata = gson.toJson(metadata)
    )

    private fun com.example.aiassistant.data.local.database.entities.ChatMessageEntity.toDomain() = ChatMessage(
        id = id,
        content = content,
        sender = ChatMessage.SenderType.valueOf(sender),
        timestamp = timestamp,
        type = ChatMessage.MessageType.valueOf(type),
        status = ChatMessage.MessageStatus.valueOf(status),
        metadata = gson.fromJson(metadata, Map::class.java) as Map<String, String>
    )
}
