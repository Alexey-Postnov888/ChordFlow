package ru.alexeypostnov.chordflow.presentation.createSong

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.alexeypostnov.chordflow.data.model.SongDetailsModel
import ru.alexeypostnov.chordflow.domain.PostCreateSongUseCase

class CreateSongViewModel(
    private val postCreateSongUseCase: PostCreateSongUseCase
): ViewModel() {
    private val _songTitle = MutableStateFlow<String>("")
    val songTitle: StateFlow<String> get() = _songTitle.asStateFlow()

    private val _songAuthor = MutableStateFlow<String>("")
    val songAuthor: StateFlow<String> get() = _songAuthor.asStateFlow()

    private val _songText = MutableStateFlow<String>("")
    val songText: StateFlow<String> get() = _songText.asStateFlow()

    private val _songIsSaved = MutableStateFlow<Boolean>(false)
    val songIsSaved: StateFlow<Boolean> get() = _songIsSaved.asStateFlow()

    private var _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error.asStateFlow()

    fun updateSongTitle(newTitle: String) {
        _songTitle.value = newTitle
    }

    fun updateSongAuthor(newAuthor: String) {
        _songAuthor.value = newAuthor
    }

    fun updateSongText(newText: String) {
        _songText.value = newText
    }

    fun saveSong() {
        viewModelScope.launch {
            resetErrorMessage()

            if (songTitle.value.isBlank() || songAuthor.value.isBlank() || _songText.value.isBlank()) {
                _error.value = "Все поля должны быть заполнены"
                return@launch
            }

            val song = SongDetailsModel(
                title = songTitle.value,
                author = songAuthor.value,
                text = songText.value
            )

            postCreateSongUseCase(song)
            _songIsSaved.value = true
        }
    }

    fun resetErrorMessage() {
        _error.value = null
    }
}