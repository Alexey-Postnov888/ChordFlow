package ru.alexeypostnov.chordflow.presentation.songDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.alexeypostnov.chordflow.data.model.ChordLineModel
import ru.alexeypostnov.chordflow.data.model.SongDetailsModel
import ru.alexeypostnov.chordflow.data.utils.ChordParser
import ru.alexeypostnov.chordflow.domain.DeleteSongByIdUseCase
import ru.alexeypostnov.chordflow.domain.GetSongDetailsByIdUseCase

class SongDetailsViewModel(
    private val songId: String,
    private val getSongDetailsByIdUseCase: GetSongDetailsByIdUseCase,
    private val deleteSongByIdUseCase: DeleteSongByIdUseCase,
    private val chordParser: ChordParser
): ViewModel() {
    private val _songDetails = MutableStateFlow<SongDetailsModel>(SongDetailsModel.empty())
    val songDetails: StateFlow<SongDetailsModel> get() = _songDetails

    private val _parsedText = MutableStateFlow<List<ChordLineModel>>(emptyList())
    val parsedText: StateFlow<List<ChordLineModel>> get() = _parsedText

    private val _isDeleted = MutableStateFlow<Boolean>(false)
    val isDeleted: StateFlow<Boolean> get() = _isDeleted

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error.asStateFlow()

    init {
        loadSongDetailsWithCache()
    }

    fun loadSongDetailsWithCache() {
        viewModelScope.launch {
            getSongDetailsByIdUseCase.getSongDetailsWithCache(songId).collect { song ->
                song?.let {
                    _songDetails.value = SongDetailsModel(
                        id = song.id,
                        title = song.title,
                        author = song.author,
                        text = song.text
                    )
                }
            }
        }
    }

    fun loadSongDetails() {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                getSongDetailsByIdUseCase(songId)?.let {
                    _songDetails.value = it
                }

                chordParser.parseText(songDetails.value.text).apply {
                    this.let { _parsedText.value = it }
                }
            } catch (e: Exception) {
                if (_parsedText.value.isEmpty()) {
                    _error.value = "Ошибка загрузки списка песен"
                }
            } finally {
                _isLoading.value = false
            }

        }
    }

    fun deleteSongById() {
        viewModelScope.launch {
            try {
                deleteSongByIdUseCase(songId)
                _isDeleted.value = true
            } catch (e: Exception) {

            }
        }
    }
}