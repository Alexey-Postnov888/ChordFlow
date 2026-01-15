package ru.alexeypostnov.chordflow.presentation.authorsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.alexeypostnov.chordflow.data.model.ResponseAuthorDetailsModel
import ru.alexeypostnov.chordflow.domain.GetAuthorsListUseCase

class AuthorsListViewModel(
    private val getAuthorsListUseCase: GetAuthorsListUseCase
): ViewModel() {
    private val _authors = MutableStateFlow<List<ResponseAuthorDetailsModel>>(emptyList())
    val authors: StateFlow<List<ResponseAuthorDetailsModel>> get() = _authors.asStateFlow()

    private val _authorsWithCache = MutableStateFlow<List<ResponseAuthorDetailsModel>>(emptyList())
    val authorsWithCache: StateFlow<List<ResponseAuthorDetailsModel>> get() = _authorsWithCache.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error.asStateFlow()

    init {
        loadAuthorsWithCache()
    }

    private fun loadAuthorsWithCache() {
        viewModelScope.launch {
            getAuthorsListUseCase.getAuthorsWithCache().collect { authors ->
                _authors.value = authors.map { authorEntity ->
                    ResponseAuthorDetailsModel(
                        author = authorEntity.author,
                        songsCount = authorEntity.songsCount
                    )
                }
            }
        }
    }

    fun loadAuthorsList() {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                getAuthorsListUseCase().apply {
                    this?.let { _authors.value = it }
                }
            } catch (e: Exception) {
                _authors.value = emptyList()
                _error.value = "Ошибка загрузки списка исполнителей"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun retry() {
        loadAuthorsList()
    }
}