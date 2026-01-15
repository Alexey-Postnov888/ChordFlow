package ru.alexeypostnov.chordflow.data.model

data class SongDetailsModel(
    val id: String = "",
    val title: String,
    val author: String,
    val text: String
) {
    companion object {
        fun empty() = SongDetailsModel(
            id = "",
            title = "",
            author = "",
            text = ""
        )
    }
}
