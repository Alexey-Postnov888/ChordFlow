package ru.alexeypostnov.chordflow.domain

import kotlinx.coroutines.flow.firstOrNull
import ru.alexeypostnov.chordflow.data.repository.ChordDatabaseRepository
import ru.alexeypostnov.chordflow.data.repository.ChordRepository

interface DeleteSongByIdUseCase {
    suspend operator fun invoke(songId: String)
}

class DeleteSongByIdUseCaseImpl(
    val repository: ChordRepository,
    val databaseRepository: ChordDatabaseRepository
): DeleteSongByIdUseCase {
    override suspend fun invoke(songId: String) {
        val authorName = databaseRepository.getSongById(songId).firstOrNull()?.author

        try {
            repository.deleteSongById(songId)
        } finally {
            databaseRepository.deleteSongById(songId)

            authorName?.let {
                val songsCount = databaseRepository.getSongsByAuthor(authorName).firstOrNull()?.size
                if (songsCount == 0) {
                    databaseRepository.deleteAuthorByName(authorName)
                }
            }
        }
    }
}