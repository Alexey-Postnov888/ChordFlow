package ru.alexeypostnov.chordflow.data.model

data class ChordLineModel(
    val chords: List<ChordPosition>,
    val lyrics: String,
    val isComment: Boolean = false
)

data class ChordPosition(
    val chord: String,
    val position: Int
)
