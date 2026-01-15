package ru.alexeypostnov.chordflow.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.alexeypostnov.chordflow.data.model.ResponseAuthorDetailsModel
import ru.alexeypostnov.chordflow.data.model.ResponseSongDetailsModel
import ru.alexeypostnov.chordflow.data.model.ResponseSongModel
import ru.alexeypostnov.chordflow.data.model.SongDetailsModel

interface ChordService {
    @GET("/songs")
    suspend fun getSongsList(): Response<List<ResponseSongModel>>

    @GET("/songs/{songId}")
    suspend fun getSongDetailsById(
        @Path("songId") singId: String
    ): Response<ResponseSongDetailsModel>

    @POST("/songs")
    suspend fun createSong(
        @Body song: SongDetailsModel
    ): Response<Void>

    @DELETE("songs/{songId}")
    suspend fun deleteSongById(
        @Path("songId") singId: String
    ): Response<Void>

    @PUT("songs/{songId}")
    suspend fun editSongById(
        @Path("songId") singId: String,
        @Body song: SongDetailsModel
    ): Response<Void>

    @GET("songs/authors/list")
    suspend fun getAuthorsList(): Response<List<ResponseAuthorDetailsModel>>

    @GET("songs/by-author/{author}")
    suspend fun getSongsListByAuthor(
        @Path("author") author: String
    ): Response<List<ResponseSongModel>>
}