package ru.alexeypostnov.chordflow.domain

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import ru.alexeypostnov.chordflow.data.repository.ChordDatabaseRepository
import ru.alexeypostnov.chordflow.data.repository.ChordRepository
import ru.alexeypostnov.chordflow.data.utils.DispatchersProvider

interface DeleteSongByIdUseCase {
    suspend operator fun invoke(songId: String)
}

class DeleteSongByIdUseCaseImpl(
    private val repository: ChordRepository,
    private val databaseRepository: ChordDatabaseRepository,
    private val dispatchersProvider: DispatchersProvider
): DeleteSongByIdUseCase {
    override suspend fun invoke(songId: String) =
        withContext(dispatchersProvider.io) {
            val authorName = databaseRepository.getSongById(songId).firstOrNull()?.author

            try {
                repository.deleteSongById(songId)
            } finally {
                databaseRepository.deleteSongById(songId)

                authorName?.let {
                    val songsCount =
                        databaseRepository.getSongsByAuthor(authorName).firstOrNull()?.size
                    if (songsCount == 0) {
                        databaseRepository.deleteAuthorByName(authorName)
                    }
                }
            }
        }
}