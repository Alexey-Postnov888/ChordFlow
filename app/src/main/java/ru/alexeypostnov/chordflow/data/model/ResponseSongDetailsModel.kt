package ru.alexeypostnov.chordflow.data.model

data class ResponseSongDetailsModel(
    val id: String,
    val title: String,
    val author: String,
    val text: String
) {
    companion object {
        fun empty() = ResponseSongDetailsModel(
            id = "",
            title = "",
            author = "",
            text = ""
        )
    }
}
