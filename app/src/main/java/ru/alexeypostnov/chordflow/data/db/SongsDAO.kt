package ru.alexeypostnov.chordflow.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.alexeypostnov.chordflow.data.model.SongEntity

@Dao
interface SongsDAO {
    @Upsert
    suspend fun upsertSong(song: SongEntity)

    @Query("SELECT * FROM ${SongEntity.TABLE} WHERE author = :author ORDER BY title ASC")
    fun getSongsByAuthor(author: String): Flow<List<SongEntity>>

    @Query("SELECT * FROM ${SongEntity.TABLE} WHERE id = :id")
    fun getSongById(id: String): Flow<SongEntity?>

    @Query("DELETE FROM ${SongEntity.TABLE} WHERE id = :id")
    suspend fun deleteSongById(id: String)
}