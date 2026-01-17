package ru.alexeypostnov.chordflow.domain

import kotlinx.coroutines.withContext
import ru.alexeypostnov.chordflow.data.model.SongDetailsModel
import ru.alexeypostnov.chordflow.data.repository.ChordRepository
import ru.alexeypostnov.chordflow.data.utils.DispatchersProvider

interface PutEditSongByIdUseCase {
    suspend operator fun invoke(songId: String, song: SongDetailsModel)
}

class PutEditSongByIdUseCaseImpl(
    private val repository: ChordRepository,
    private val dispatchersProvider: DispatchersProvider
): PutEditSongByIdUseCase {
    override suspend fun invoke(
        songId: String,
        song: SongDetailsModel
    ) =
        withContext(dispatchersProvider.io) {
            repository.editSongById(songId, song)
        }
}