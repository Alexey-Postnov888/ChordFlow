package ru.alexeypostnov.chordflow.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.alexeypostnov.chordflow.presentation.ChordFlowScaffold
import ru.alexeypostnov.chordflow.presentation.MenuItemModel
import ru.alexeypostnov.chordflow.presentation.authorsList.AuthorsListScreen
import ru.alexeypostnov.chordflow.presentation.createSong.CreateSongFloatingActionButton
import ru.alexeypostnov.chordflow.presentation.createSong.CreateSongScreen
import ru.alexeypostnov.chordflow.presentation.createSong.CreateSongViewModel
import ru.alexeypostnov.chordflow.presentation.editSong.EditSongScreen
import ru.alexeypostnov.chordflow.presentation.editSong.EditSongViewModel
import ru.alexeypostnov.chordflow.presentation.songDetails.SongDetailsScreen
import ru.alexeypostnov.chordflow.presentation.songDetails.SongDetailsViewModel
import ru.alexeypostnov.chordflow.presentation.songsList.SongsListScreen


class AuthorsListNavigationScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        ChordFlowScaffold(
            title = "ChordFlow",
            showBackButton = false,
            navigator = navigator,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navigator.push(CreateSongNavigationScreen())
                    }
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "Добавить"
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                AuthorsListScreen()
            }
        }
    }
}

class SongsListNavigationScreen(
    private val author: String
): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        ChordFlowScaffold(
            title = author,
            navigator = navigator,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navigator.push(CreateSongNavigationScreen(author))
                    }
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "Добавить"
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                SongsListScreen(author)
            }
        }
    }
}

class SongDetailsNavigationScreen(
    private val songId: String,
    private val songTitle: String,
): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: SongDetailsViewModel = koinViewModel(parameters = { parametersOf(songId) })

        val isDeleted by viewModel.isDeleted.collectAsStateWithLifecycle()

        LaunchedEffect(isDeleted) {
            if (isDeleted) {
                navigator.pop()
            }
        }

        ChordFlowScaffold(
            title = songTitle,
            navigator = navigator,
            menuItems = listOf(
                MenuItemModel(
                    text = "Изменить",
                    icon = Icons.Rounded.Edit,
                    onClick = {
                        navigator.push(EditSongNavigationScreen(songId))
                    }
                ),
                MenuItemModel(
                    text = "Удалить",
                    icon = Icons.Rounded.Delete,
                    onClick = {
                        viewModel.deleteSongById()
                    }
                )
            )
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                SongDetailsScreen(songId)
            }
        }
    }
}

class CreateSongNavigationScreen(
    val author: String? = null
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel: CreateSongViewModel = koinViewModel()

        val songIsSaved by viewModel.songIsSaved.collectAsStateWithLifecycle()

        LaunchedEffect(songIsSaved) {
            if (songIsSaved) {
                navigator.pop()
            }
        }

        LaunchedEffect(Unit) {
            if (!author.isNullOrEmpty()) {
                viewModel.updateSongAuthor(author)
            }
        }

        ChordFlowScaffold(
            title = "Добавление новой песни",
            navigator = navigator,
            floatingActionButton = {
                CreateSongFloatingActionButton { viewModel.saveSong() }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                CreateSongScreen()
            }
        }
    }
}

class EditSongNavigationScreen(
    private val songId: String
): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: EditSongViewModel = koinViewModel()

        val isEdited by viewModel.isEdited.collectAsStateWithLifecycle()

        LaunchedEffect(isEdited) {
            if (isEdited) {
                navigator.pop()
            }
        }

        ChordFlowScaffold(
            title = "Редактирование песни",
            navigator = navigator,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.editSongById(songId)
                    }
                ) {
                    Icon(
                        Icons.Rounded.Done,
                        contentDescription = "Сохранить изменения"
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                EditSongScreen(songId)
            }
        }
    }
}