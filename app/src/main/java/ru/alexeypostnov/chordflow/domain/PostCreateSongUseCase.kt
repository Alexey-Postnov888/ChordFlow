package ru.alexeypostnov.chordflow.domain

import kotlinx.coroutines.withContext
import ru.alexeypostnov.chordflow.data.model.SongDetailsModel
import ru.alexeypostnov.chordflow.data.repository.ChordRepository
import ru.alexeypostnov.chordflow.data.utils.DispatchersProvider

interface PostCreateSongUseCase {
    suspend operator fun invoke(song: SongDetailsModel)
}

class PostCreateSongUseCaseImpl(
    private val repository: ChordRepository,
    private val dispatchersProvider: DispatchersProvider
): PostCreateSongUseCase {
    override suspend fun invoke(song: SongDetailsModel) =
        withContext(dispatchersProvider.io) {
            repository.createSong(song)
        }
}