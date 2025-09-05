package com.example.aiassistant.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val content: String,
    val sender: SenderType,
    val timestamp: Long = System.currentTimeMillis(),
    val type: MessageType = MessageType.TEXT,
    val status: MessageStatus = MessageStatus.SENT,
    val metadata: Map<String, String> = emptyMap()
) : Parcelable {
    enum class SenderType { USER, AI, SYSTEM }
    enum class MessageType { TEXT, VOICE, IMAGE, COMMAND }
    enum class MessageStatus { SENDING, SENT, ERROR, DELIVERED }
}
