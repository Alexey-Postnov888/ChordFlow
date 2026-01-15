package ru.alexeypostnov.chordflow.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.alexeypostnov.chordflow.data.model.AuthorEntity

@Dao
interface AuthorsDAO {
    @Upsert
    suspend fun upsertAuthor(author: AuthorEntity)

    @Query("SELECT * FROM ${AuthorEntity.TABLE} ORDER BY author ASC")
    fun getAuthors(): Flow<List<AuthorEntity>>

    @Query("SELECT * FROM ${AuthorEntity.TABLE} WHERE author = :name LIMIT 1")
    suspend fun getAuthorByName(name: String): AuthorEntity?

    @Query("DELETE FROM ${AuthorEntity.TABLE} WHERE author = :name")
    suspend fun deleteAuthorByName(name: String)
}