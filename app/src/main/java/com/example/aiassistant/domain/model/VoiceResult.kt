package com.example.aiassistant.domain.model

sealed class VoiceResult {
    data class Partial(val text: String) : VoiceResult()
    data class Final(val text: String) : VoiceResult()
    data class Error(val message: String) : VoiceResult()
}
