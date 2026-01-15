package ru.alexeypostnov.chordflow.domain

import ru.alexeypostnov.chordflow.data.model.SongDetailsModel
import ru.alexeypostnov.chordflow.data.repository.ChordRepository

interface PostCreateSongUseCase {
    suspend operator fun invoke(song: SongDetailsModel)
}

class PostCreateSongUseCaseImpl(
    val repository: ChordRepository
): PostCreateSongUseCase {
    override suspend fun invoke(song: SongDetailsModel) {
        repository.createSong(song)
    }
}