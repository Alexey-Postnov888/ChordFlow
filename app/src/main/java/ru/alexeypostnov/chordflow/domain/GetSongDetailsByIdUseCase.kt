package ru.alexeypostnov.chordflow.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import ru.alexeypostnov.chordflow.data.model.SongDetailsModel
import ru.alexeypostnov.chordflow.data.model.SongEntity
import ru.alexeypostnov.chordflow.data.repository.ChordDatabaseRepository
import ru.alexeypostnov.chordflow.data.repository.ChordRepository

interface GetSongDetailsByIdUseCase {
    suspend operator fun invoke(songId: String): SongDetailsModel?
    fun getSongDetailsWithCache(songId: String): Flow<SongEntity?>
}

class GetSongDetailsByIdUseCaseImpl(
    val repository: ChordRepository,
    val databaseRepository: ChordDatabaseRepository
): GetSongDetailsByIdUseCase {
    override suspend fun invoke(songId: String): SongDetailsModel? {
        return try {
            val remoteSongDetails = repository.getSongDetailsById(songId)
                ?: throw Exception("Ошибка загрузки текста")

            val localSongDetails = databaseRepository.getSongById(remoteSongDetails.id).firstOrNull()
            val songEntity = localSongDetails?.copy(
                title = remoteSongDetails.title,
                author = remoteSongDetails.author,
                text = remoteSongDetails.text
            )
                ?: SongEntity(
                    id = remoteSongDetails.id,
                    title = remoteSongDetails.title,
                    author = remoteSongDetails.author,
                    text = remoteSongDetails.text
                )

            databaseRepository.upsertSong(songEntity)

            remoteSongDetails
        } catch (e: Exception) {
            null
        }
    }

    override fun getSongDetailsWithCache(songId: String) = databaseRepository.getSongById(songId)
}