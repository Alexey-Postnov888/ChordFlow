package ru.alexeypostnov.chordflow.presentation.songsList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.alexeypostnov.chordflow.data.model.SongDetailsModel
import ru.alexeypostnov.chordflow.navigation.EditSongNavigationScreen
import ru.alexeypostnov.chordflow.navigation.SongDetailsNavigationScreen
import ru.alexeypostnov.chordflow.presentation.ErrorComponent

@Composable
fun SongsListScreen(author: String) {
    val viewModel: SongsListViewModel = koinViewModel(parameters = { parametersOf(author) })
    val navigator = LocalNavigator.current

    LaunchedEffect(Unit) {
        viewModel.loadSongsList()
    }

    val songs by viewModel.songs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    var showMenuDialog  by remember { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<SongDetailsModel>(SongDetailsModel.empty()) }

    fun showSongMenu(song: SongDetailsModel) {
        selectedSong = song
        showMenuDialog = true
    }

    if (showMenuDialog && selectedSong != SongDetailsModel.empty()) {
        SongMenuAlertDialog(
            song = selectedSong,
            onEditClick = {
                navigator?.push(
                    EditSongNavigationScreen(songId = selectedSong.id)
                )
                showMenuDialog = false
            },
            onDeleteClick = {
                viewModel.deleteSongById(selectedSong.id)
                showMenuDialog = false
            },
            onDismiss = {
                showMenuDialog = false
                selectedSong = SongDetailsModel.empty()
            }
        )
    }

    SongsListScreenContent(
        songs = songs,
        onSongClick = { song ->
            navigator?.push(
                SongDetailsNavigationScreen(
                    songId = song.id,
                    songTitle = "${song.title} - ${song.author}"
                )
            )
        },
        onSongLongClick = { song ->
            showSongMenu(song)
        },
        isLoading = isLoading,
        error = error
    )
}

@Composable
fun SongsListScreenContent(
    songs: List<SongDetailsModel>,
    onSongClick: (SongDetailsModel) -> Unit,
    onSongLongClick: (SongDetailsModel) -> Unit,
    isLoading: Boolean,
    error: String?,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (songsList, errorComponent) = createRefs()

        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        when {
            !error.isNullOrEmpty() -> {
                ErrorComponent(error, modifier = Modifier
                    .constrainAs(errorComponent) {
                        centerHorizontallyTo(parent)
                        centerVerticallyTo(parent)
                    })
            }

            !isLoading && songs.isEmpty() -> {
                ErrorComponent(error = "Нет песен у этого исполнителя...", modifier = Modifier
                    .constrainAs(errorComponent) {
                        centerHorizontallyTo(parent)
                        centerVerticallyTo(parent)
                    })
            }

            songs.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(10.dp)
                        .constrainAs(songsList) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    items(songs, key = { it.id }) { song ->
                        SongItem(
                            song,
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = { onSongClick(song) },
                                    onLongClick = { onSongLongClick(song) }
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SongItem(song: SongDetailsModel, modifier: Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = modifier
            .padding(bottom = 5.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(10.dp)

    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(12.dp)
        ) {
            val (songTitle) = createRefs()
            Text(
                text = song.title,
                fontSize = 18.sp,
                modifier = Modifier
                .constrainAs(songTitle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                })
        }
    }
}

@Composable
fun SongMenuAlertDialog(
    song: SongDetailsModel,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(song.title) },
        text = {
            ConstraintLayout {
                val (editContainer, deleteContainer) = createRefs()

                ConstraintLayout(
                    modifier = Modifier
                        .constrainAs(editContainer) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                ) {
                    val (editIcon, editBtn) = createRefs()

                    Icon(
                        Icons.Rounded.Edit,
                        contentDescription = "Редактировать",
                        tint = colorScheme.primary,
                        modifier = Modifier
                            .constrainAs(editIcon) {
                                centerVerticallyTo(parent)
                                start.linkTo(parent.start)
                            }
                    )

                    Text(
                        text = "Редактировать",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .clickable(onClick = onEditClick)
                            .constrainAs(editBtn) {
                                centerVerticallyTo(parent)
                                start.linkTo(editIcon.end, margin = 10.dp)
                            }
                    )
                }

                ConstraintLayout(
                    modifier = Modifier
                        .constrainAs(deleteContainer) {
                            top.linkTo(editContainer.bottom, margin = 15.dp)
                            start.linkTo(parent.start)
                        }
                ) {
                    val (deleteIcon, deleteBtn) = createRefs()

                    Icon(
                        Icons.Rounded.Delete,
                        contentDescription = "Удалить",
                        tint = colorScheme.primary,
                        modifier = Modifier
                            .constrainAs(deleteIcon) {
                                centerVerticallyTo(parent)
                                start.linkTo(parent.start)
                            }
                    )

                    Text(
                        text = "Удалить",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .clickable(onClick = onDeleteClick)
                            .constrainAs(deleteBtn) {
                                centerVerticallyTo(parent)
                                start.linkTo(deleteIcon.end, margin = 10.dp)
                            }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть")
            }
        }
    )
}