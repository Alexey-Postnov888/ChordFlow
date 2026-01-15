package ru.alexeypostnov.chordflow.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = SongEntity.TABLE)
data class SongEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val author: String,
    val text: String
) {
    companion object {
        const val TABLE = "songs"
    }
}
