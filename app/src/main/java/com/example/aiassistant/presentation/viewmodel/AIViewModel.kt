package com.example.aiassistant.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiassistant.domain.model.ChatMessage
import com.example.aiassistant.domain.model.ConnectionState
import com.example.aiassistant.domain.repository.AIRepository
import com.example.aiassistant.domain.usecase.GetChatHistoryUseCase
import com.example.aiassistant.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AIViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getChatHistoryUseCase: GetChatHistoryUseCase,
    private val aiRepository: AIRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AIViewState())
    val uiState: StateFlow<AIViewState> = _uiState.asStateFlow()

    init {
        loadChatHistory()
        observeConnectionState()
        observeIncomingMessages()
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            sendMessageUseCase(message).onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message
                )
            }
        }
    }

    private fun loadChatHistory() {
        viewModelScope.launch {
            getChatHistoryUseCase().collect { messages ->
                _uiState.value = _uiState.value.copy(messages = messages)
            }
        }
    }

    private fun observeConnectionState() {
        aiRepository.connectionState
            .onEach { connectionState ->
                _uiState.value = _uiState.value.copy(connectionState = connectionState)
            }
            .launchIn(viewModelScope)
    }

    private fun observeIncomingMessages() {
        aiRepository.incomingMessages
            .onEach { message ->
                val currentMessages = _uiState.value.messages
                _uiState.value = _uiState.value.copy(
                    messages = currentMessages + message
                )
            }
            .launchIn(viewModelScope)
    }
}

data class AIViewState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.Disconnected(),
    val error: String? = null
)
