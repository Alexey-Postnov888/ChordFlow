package ru.alexeypostnov.chordflow.domain

import ru.alexeypostnov.chordflow.data.model.ResponseSongModel
import ru.alexeypostnov.chordflow.data.repository.ChordDatabaseRepository
import ru.alexeypostnov.chordflow.data.repository.ChordRepository

interface GetSongsListByAuthorUseCase {
    suspend operator fun invoke(author: String): List<ResponseSongModel>?
}

class GetSongsListByAuthorUseCaseImpl(
    val repository: ChordRepository,
    val databaseRepository: ChordDatabaseRepository
): GetSongsListByAuthorUseCase {
    override suspend fun invoke(author: String): List<ResponseSongModel>? =
        repository.getSongsListByAuthor(author)
}