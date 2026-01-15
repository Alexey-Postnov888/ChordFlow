package ru.alexeypostnov.chordflow.data.repository

import kotlinx.coroutines.flow.Flow
import ru.alexeypostnov.chordflow.data.db.AuthorsDAO
import ru.alexeypostnov.chordflow.data.db.SongsDAO
import ru.alexeypostnov.chordflow.data.model.AuthorEntity
import ru.alexeypostnov.chordflow.data.model.SongEntity

interface ChordDatabaseRepository {
    fun getAuthors(): Flow<List<AuthorEntity>>
    suspend fun getAuthorByName(name: String): AuthorEntity?
    suspend fun deleteAuthorByName(name: String)
    suspend fun upsertAuthor(author: AuthorEntity)

    fun getSongsByAuthor(author: String): Flow<List<SongEntity>>
    fun getSongById(songId: String): Flow<SongEntity?>
    suspend fun upsertSong(song: SongEntity)
    suspend fun deleteSongById(songId: String)
}

class ChordDatabaseRepositoryImpl(
    private val authorsDAO: AuthorsDAO,
    private val songsDAO: SongsDAO
): ChordDatabaseRepository {
    override fun getAuthors(): Flow<List<AuthorEntity>> =
        authorsDAO.getAuthors()

    override suspend fun getAuthorByName(name: String): AuthorEntity? =
        authorsDAO.getAuthorByName(name)

    override suspend fun deleteAuthorByName(name: String) =
        authorsDAO.deleteAuthorByName(name)


    override suspend fun upsertAuthor(author: AuthorEntity) =
        authorsDAO.upsertAuthor(author)


    override fun getSongsByAuthor(author: String): Flow<List<SongEntity>> =
        songsDAO.getSongsByAuthor(author)


    override fun getSongById(songId: String): Flow<SongEntity?> =
        songsDAO.getSongById(songId)


    override suspend fun upsertSong(song: SongEntity) =
        songsDAO.upsertSong(song)

    override suspend fun deleteSongById(songId: String) =
        songsDAO.deleteSongById(songId)
}