package ru.alexeypostnov.chordflow.presentation.songsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.alexeypostnov.chordflow.data.model.ResponseSongModel
import ru.alexeypostnov.chordflow.domain.GetSongsListByAuthorUseCase

class SongsListViewModel(
    private val getSongsListByAuthorUseCase: GetSongsListByAuthorUseCase
) : ViewModel() {
    private val _songs = MutableStateFlow<List<ResponseSongModel>>(emptyList())
    val songs: StateFlow<List<ResponseSongModel>> get() = _songs.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error.asStateFlow()

    fun loadSongsList(author: String) {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                getSongsListByAuthorUseCase(author).apply {
                    this?.let { _songs.value = it }
                }
            } catch (e: Exception) {
                _songs.value = emptyList()
                _error.value = "Ошибка загрузки списка песен"
            } finally {
                _isLoading.value = false
            }
        }
    }
}