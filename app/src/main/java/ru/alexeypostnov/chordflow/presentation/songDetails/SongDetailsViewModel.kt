package ru.alexeypostnov.chordflow.presentation.songDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.alexeypostnov.chordflow.data.model.ChordLineModel
import ru.alexeypostnov.chordflow.data.model.ResponseSongDetailsModel
import ru.alexeypostnov.chordflow.data.utils.ChordParser
import ru.alexeypostnov.chordflow.domain.DeleteSongByIdUseCase
import ru.alexeypostnov.chordflow.domain.GetSongDetailsByIdUseCase

class SongDetailsViewModel(
    private val getSongDetailsByIdUseCase: GetSongDetailsByIdUseCase,
    private val deleteSongByIdUseCase: DeleteSongByIdUseCase,
    private val chordParser: ChordParser
): ViewModel() {
    private val _songDetails = MutableStateFlow<ResponseSongDetailsModel>(ResponseSongDetailsModel.empty())
    val songDetails: StateFlow<ResponseSongDetailsModel> get() = _songDetails

    private val _parsedText = MutableStateFlow<List<ChordLineModel>>(emptyList())
    val parsedText: StateFlow<List<ChordLineModel>> get() = _parsedText

    private val _isDeleted = MutableStateFlow<Boolean>(false)
    val isDeleted: StateFlow<Boolean> get() = _isDeleted

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error.asStateFlow()

    fun loadSongDetailsWithCache(songId: String) {
        viewModelScope.launch {
            getSongDetailsByIdUseCase.getSongDetailsWithCache(songId).collect { song ->
                song?.let {
                    _songDetails.value = ResponseSongDetailsModel(
                        id = song.id,
                        title = song.title,
                        author = song.author,
                        text = song.text
                    )
                }
            }
        }
    }

    fun loadSongDetails(songId: String) {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                getSongDetailsByIdUseCase(songId).apply {
                    this?.let { _songDetails.value = it }
                }

                chordParser.parseText(songDetails.value.text).apply {
                    this.let { _parsedText.value = it }
                }
            } catch (e: Exception) {
                _songDetails.value = ResponseSongDetailsModel.empty()
                _error.value = "Ошибка загрузки текста песни"
            } finally {
                _isLoading.value = false
            }

        }
    }

    fun deleteSongById(songId: String) {
        viewModelScope.launch {
            try {
                deleteSongByIdUseCase(songId)
                _isDeleted.value = true
            } catch (e: Exception) {

            }
        }
    }
}