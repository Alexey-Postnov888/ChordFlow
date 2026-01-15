package ru.alexeypostnov.chordflow.data.model

import com.google.gson.annotations.SerializedName


data class ResponseAuthorDetailsModel(
    val author: String,
    @SerializedName("songs_count")
    val songsCount: Int
)
