package ru.alexeypostnov.chordflow.domain

import kotlinx.coroutines.flow.Flow
import ru.alexeypostnov.chordflow.data.model.AuthorEntity
import ru.alexeypostnov.chordflow.data.model.ResponseAuthorDetailsModel
import ru.alexeypostnov.chordflow.data.repository.ChordDatabaseRepository
import ru.alexeypostnov.chordflow.data.repository.ChordRepository

interface GetAuthorsListUseCase {
    suspend operator fun invoke(): List<ResponseAuthorDetailsModel>?
    fun getAuthorsWithCache(): Flow<List<AuthorEntity>>
}

class GetAuthorsListUseCaseImpl(
    val repository: ChordRepository,
    val databaseRepository: ChordDatabaseRepository
): GetAuthorsListUseCase {
    override suspend fun invoke(): List<ResponseAuthorDetailsModel>? {
        val authors = repository.getAuthorsList() ?: emptyList()

        authors.forEach { remoteAuthor ->
            val localAuthor = databaseRepository.getAuthorByName(remoteAuthor.author)

            val authorEntity = localAuthor?.copy(
                author = remoteAuthor.author,
                songsCount = remoteAuthor.songsCount
            )
                ?: AuthorEntity(
                    author = remoteAuthor.author,
                    songsCount = remoteAuthor.songsCount
                )

            databaseRepository.upsertAuthor(authorEntity)
        }

        return authors
    }

    override fun getAuthorsWithCache() = databaseRepository.getAuthors()
}