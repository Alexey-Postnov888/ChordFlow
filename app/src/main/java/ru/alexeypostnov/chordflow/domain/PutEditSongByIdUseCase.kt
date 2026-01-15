package ru.alexeypostnov.chordflow.domain

import ru.alexeypostnov.chordflow.data.model.SongDetailsModel
import ru.alexeypostnov.chordflow.data.repository.ChordRepository

interface PutEditSongByIdUseCase {
    suspend operator fun invoke(songId: String, song: SongDetailsModel)
}

class PutEditSongByIdUseCaseImpl(
    val repository: ChordRepository
): PutEditSongByIdUseCase {
    override suspend fun invoke(
        songId: String,
        song: SongDetailsModel
    ) {
        repository.editSongById(songId, song)
    }
}