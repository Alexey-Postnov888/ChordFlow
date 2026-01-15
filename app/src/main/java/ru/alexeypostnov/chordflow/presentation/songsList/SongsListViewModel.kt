package ru.alexeypostnov.chordflow.presentation.songsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.alexeypostnov.chordflow.data.model.SongDetailsModel
import ru.alexeypostnov.chordflow.domain.DeleteSongByIdUseCase
import ru.alexeypostnov.chordflow.domain.GetSongsListByAuthorUseCase

class SongsListViewModel(
    private val author: String,
    private val getSongsListByAuthorUseCase: GetSongsListByAuthorUseCase,
    private val deleteSongByIdUseCase: DeleteSongByIdUseCase
) : ViewModel() {
    private val _songs = MutableStateFlow<List<SongDetailsModel>>(emptyList())
    val songs: StateFlow<List<SongDetailsModel>> get() = _songs.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error.asStateFlow()

    init {
        loadSongsWithCache()
    }

    fun loadSongsWithCache() {
        viewModelScope.launch {
            getSongsListByAuthorUseCase.getSongsWithCache(author).collect { songs ->
                _songs.value = songs.map { songEntity ->
                    SongDetailsModel(
                        id = songEntity.id,
                        title = songEntity.title,
                        author = songEntity.author,
                        text = songEntity.text
                    )
                }
            }
        }
    }

    fun loadSongsList() {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                getSongsListByAuthorUseCase(author)?.let {
                    _songs.value = it
                }
            } catch (e: Exception) {
                if (_songs.value.isEmpty()) {
                    _error.value = "Ошибка загрузки списка песен"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteSongById(songId: String) {
        viewModelScope.launch {
            try {
                deleteSongByIdUseCase(songId)
            } catch (e: Exception) {

            }
        }
    }
}