package ru.alexeypostnov.chordflow.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.alexeypostnov.chordflow.data.model.AuthorEntity
import ru.alexeypostnov.chordflow.data.model.SongEntity

@Database(
    entities = [
        AuthorEntity::class,
        SongEntity::class
    ],
    version = 1,
    autoMigrations = []
)
abstract class ChordFlowDatabase: RoomDatabase() {
    abstract val authorsDAO: AuthorsDAO
    abstract val songsDAO: SongsDAO

    companion object {
        val MIGRATION_0_1 = object : Migration(0, 1) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXIST authors (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    author TEXT NOT NULL,
                    songsCount INTEGER NOT NULL
                    )
                """.trimIndent())

                db.execSQL("""
                    CREATE TABLE IF NOT EXIST songs (
                    id TEXT PRIMARY KEY NOT NULL,
                    title TEXT NOT NULL,
                    author TEXT NOT NULL,
                    text TEXT NOT NULL,
                    )
                """.trimIndent())
            }
        }
    }
}