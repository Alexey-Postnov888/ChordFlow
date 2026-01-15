package ru.alexeypostnov.chordflow.presentation.createSong

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun CreateSongFloatingActionButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick
    ) {
        Icon(
            Icons.Rounded.Done,
            contentDescription = "Сохранить новую песню"
        )
    }
}