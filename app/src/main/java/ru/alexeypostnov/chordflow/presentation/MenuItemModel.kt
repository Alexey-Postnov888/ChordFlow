package ru.alexeypostnov.chordflow.presentation

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItemModel (
    val text: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)