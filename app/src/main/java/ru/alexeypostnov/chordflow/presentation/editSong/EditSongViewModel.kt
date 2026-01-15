package ru.alexeypostnov.chordflow.presentation.editSong

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.alexeypostnov.chordflow.data.model.SongDetailsModel
import ru.alexeypostnov.chordflow.domain.GetSongDetailsByIdUseCase
import ru.alexeypostnov.chordflow.domain.PutEditSongByIdUseCase

class EditSongViewModel(
    private val getSongDetailsByIdUseCase: GetSongDetailsByIdUseCase,
    private val putEditSongByIdUseCase: PutEditSongByIdUseCase
): ViewModel() {
    private val _songTitle = MutableStateFlow<String>("")
    val songTitle: StateFlow<String> get() = _songTitle.asStateFlow()

    private val _songAuthor = MutableStateFlow<String>("")
    val songAuthor: StateFlow<String> get() = _songAuthor.asStateFlow()

    private val _songText = MutableStateFlow<String>("")
    val songText: StateFlow<String> get() = _songText.asStateFlow()

    private val _isEdited = MutableStateFlow<Boolean>(false)
    val isEdited: StateFlow<Boolean> get() = _isEdited

    private var _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error.asStateFlow()

    fun loadSongDetails(songId: String) {
        viewModelScope.launch {
            try {
                getSongDetailsByIdUseCase(songId)?.let { songDetails ->
                    _songTitle.value = songDetails.title
                    _songAuthor.value = songDetails.author
                    _songText.value = songDetails.text
                }
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки песни"
            }
        }
    }

    fun updateSongTitle(newTitle: String) {
        _songTitle.value = newTitle
    }

    fun updateSongAuthor(newAuthor: String) {
        _songAuthor.value = newAuthor
    }

    fun updateSongText(newText: String) {
        _songText.value = newText
    }

    fun editSongById(songId: String) {
        viewModelScope.launch {
            _error.value = null

            try {
                if (songTitle.value.isBlank() || songAuthor.value.isBlank() || _songText.value.isBlank()) {
                    _error.value = "Все поля должны быть заполнены"
                    return@launch
                }

                val song = SongDetailsModel(
                    title = songTitle.value,
                    author = songAuthor.value,
                    text = songText.value
                )

                putEditSongByIdUseCase(songId, song)
                _isEdited.value = true
            } catch (e: Exception) {
                _error.value = "Что-то пошло не так при редактировании песни"
            }
        }
    }

    fun resetErrorMessage() {
        _error.value = null
    }
}