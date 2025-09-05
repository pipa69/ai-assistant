package com.example.aiassistant.data.remote.websocket

import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.streamadapter.coroutines.CoroutineStreamAdapter
import com.tinder.scarlet.websocket.okhttp.OkHttpWebSocketFactory
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import javax.inject.Inject

class WebSocketService @Inject constructor(
    private val okHttpClient: OkHttpClient
) {
    private val scarlet = Scarlet.Builder()
        .webSocketFactory(OkHttpWebSocketFactory(okHttpClient))
        .addMessageAdapterFactory(GsonMessageAdapter.Factory())
        .addStreamAdapterFactory(CoroutineStreamAdapter.Factory())
        .build()

    private val aiService = scarlet.create(AIWebSocketService::class.java)

    val connectionState: Flow<WebSocket.Event> = aiService.observeWebSocketEvent()
    val incomingMessages: Flow<String> = aiService.observeMessages()

    suspend fun sendMessage(message: String) {
        aiService.sendMessage(message)
    }
}

interface AIWebSocketService {
    @Receive
    fun observeWebSocketEvent(): Flow<WebSocket.Event>

    @Receive
    fun observeMessages(): Flow<String>

    @Send
    fun sendMessage(message: String)
}
