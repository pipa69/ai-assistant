package com.example.aiassistant.domain.model

sealed class ConnectionState {
    object Connected : ConnectionState()
    object Connecting : ConnectionState()
    data class Disconnected(val reason: String? = null) : ConnectionState()
    data class Error(val throwable: Throwable) : ConnectionState()
}
