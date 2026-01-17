package ru.alexeypostnov.chordflow.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.alexeypostnov.chordflow.data.model.AuthorEntity
import ru.alexeypostnov.chordflow.data.model.ResponseAuthorDetailsModel
import ru.alexeypostnov.chordflow.data.repository.ChordDatabaseRepository
import ru.alexeypostnov.chordflow.data.repository.ChordRepository
import ru.alexeypostnov.chordflow.data.utils.DispatchersProvider

interface GetAuthorsListUseCase {
    suspend operator fun invoke(): List<ResponseAuthorDetailsModel>?
    fun getAuthorsWithCache(): Flow<List<AuthorEntity>>
}

class GetAuthorsListUseCaseImpl(
    private val repository: ChordRepository,
    private val databaseRepository: ChordDatabaseRepository,
    private val dispatchersProvider: DispatchersProvider
): GetAuthorsListUseCase {
    override suspend fun invoke(): List<ResponseAuthorDetailsModel>? =
        withContext(dispatchersProvider.io) {
            try {
                val authors = repository.getAuthorsList()
                    ?: throw Exception("Ошибка загрузки исполнителей")

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

                authors
            } catch (e: Exception) {
                null
            }
        }

    override fun getAuthorsWithCache() = databaseRepository.getAuthors()
}