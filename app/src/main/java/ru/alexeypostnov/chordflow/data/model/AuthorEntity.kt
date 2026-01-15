package ru.alexeypostnov.chordflow.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = AuthorEntity.TABLE)
data class AuthorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val author: String,
    val songsCount: Int
) {
    companion object {
        const val TABLE = "authors"
    }
}
