package ru.alexeypostnov.chordflow.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import ru.alexeypostnov.chordflow.data.model.SongDetailsModel
import ru.alexeypostnov.chordflow.data.model.SongEntity
import ru.alexeypostnov.chordflow.data.repository.ChordDatabaseRepository
import ru.alexeypostnov.chordflow.data.repository.ChordRepository

interface GetSongsListByAuthorUseCase {
    suspend operator fun invoke(author: String): List<SongDetailsModel>?
    fun getSongsWithCache(author: String): Flow<List<SongEntity>>

}

class GetSongsListByAuthorUseCaseImpl(
    val repository: ChordRepository,
    val databaseRepository: ChordDatabaseRepository
): GetSongsListByAuthorUseCase {
    override suspend fun invoke(author: String): List<SongDetailsModel>? {
        return try {
            val songs = repository.getSongsListByAuthor(author)
                ?: throw Exception("Ошибка загрузки песен")

            songs.forEach { remoteSong ->
                val localSong = databaseRepository.getSongById(remoteSong.id).firstOrNull()

                val songEntity = localSong?.copy(
                    title = remoteSong.title,
                    author = remoteSong.author,
                    text = remoteSong.text
                )
                    ?: SongEntity(
                        id = remoteSong.id,
                        title = remoteSong.title,
                        author = remoteSong.author,
                        text = remoteSong.text
                    )

                databaseRepository.upsertSong(songEntity)
            }

            songs
        } catch (e: Exception) {
            null
        }
    }

    override fun getSongsWithCache(author: String): Flow<List<SongEntity>> =
        databaseRepository.getSongsByAuthor(author)
}