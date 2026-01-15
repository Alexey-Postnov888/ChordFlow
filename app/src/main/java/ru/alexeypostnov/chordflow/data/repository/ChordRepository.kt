package ru.alexeypostnov.chordflow.data.repository

import ru.alexeypostnov.chordflow.data.model.ResponseAuthorDetailsModel
import ru.alexeypostnov.chordflow.data.model.ResponseSongDetailsModel
import ru.alexeypostnov.chordflow.data.model.ResponseSongModel
import ru.alexeypostnov.chordflow.data.model.SongDetailsModel
import ru.alexeypostnov.chordflow.data.network.ChordService

interface ChordRepository {
    suspend fun getSongsList(): List<ResponseSongModel>?
    suspend fun getSongDetailsById(songId: String): ResponseSongDetailsModel?
    suspend fun createSong(song: SongDetailsModel)
    suspend fun deleteSongById(songId: String)
    suspend fun editSongById(songId: String, song: SongDetailsModel)
    suspend fun getAuthorsList(): List<ResponseAuthorDetailsModel>?
    suspend fun getSongsListByAuthor(author: String): List<ResponseSongModel>?
}

class ChordRepositoryImpl(
    private val service: ChordService
): ChordRepository {
    override suspend fun getSongsList(): List<ResponseSongModel>? {
        val response = service.getSongsList()
        return if (response.isSuccessful)
            return response.body()
        else null
    }

    override suspend fun getSongDetailsById(songId: String): ResponseSongDetailsModel? {
        val response = service.getSongDetailsById(songId)
        return if (response.isSuccessful)
            return response.body()
        else null
    }

    override suspend fun createSong(song: SongDetailsModel) {
        service.createSong(song)
    }

    override suspend fun deleteSongById(songId: String) {
        service.deleteSongById(songId)
    }

    override suspend fun editSongById(
        songId: String,
        song: SongDetailsModel
    ) {
        service.editSongById(songId, song)
    }

    override suspend fun getAuthorsList(): List<ResponseAuthorDetailsModel>? {
        val response = service.getAuthorsList()
        return if (response.isSuccessful)
            return response.body()
        else null
    }

    override suspend fun getSongsListByAuthor(author: String): List<ResponseSongModel>? {
        val response = service.getSongsListByAuthor(author)
        return if (response.isSuccessful)
            return response.body()
        else null
    }
}