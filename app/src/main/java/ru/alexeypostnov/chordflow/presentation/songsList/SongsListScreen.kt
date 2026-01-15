package ru.alexeypostnov.chordflow.presentation.songsList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import org.koin.androidx.compose.koinViewModel
import ru.alexeypostnov.chordflow.data.model.ResponseSongModel
import ru.alexeypostnov.chordflow.navigation.SongDetailsNavigationScreen
import ru.alexeypostnov.chordflow.presentation.ErrorComponent
import ru.alexeypostnov.chordflow.presentation.LoadingComponent

@Composable
fun SongsListScreen(author: String) {
    val viewModel: SongsListViewModel = koinViewModel()
    val navigator = LocalNavigator.current

    LaunchedEffect(Unit) {
        viewModel.loadSongsList(author)
    }

    val songs by viewModel.songs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

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
        isLoading = isLoading,
        error = error
    )
}

@Composable
fun SongsListScreenContent(
    songs: List<ResponseSongModel>,
    onSongClick: (ResponseSongModel) -> Unit,
    isLoading: Boolean,
    error: String?,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (songsList, loadingComponent, errorComponent) = createRefs()

        if (isLoading) {
            LoadingComponent(
                modifier = Modifier
                    .constrainAs(loadingComponent) {
                        centerHorizontallyTo(parent)
                        centerVerticallyTo(parent)
                    })
        } else if (!error.isNullOrEmpty()) {
            ErrorComponent(
                error, modifier = Modifier
                    .constrainAs(errorComponent) {
                        centerHorizontallyTo(parent)
                        centerVerticallyTo(parent)
                    })
        } else {
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
                            .clickable(onClick = {
                                onSongClick(song)
                            })
                    )
                }
            }
        }
    }
}

@Composable
fun SongItem(song: ResponseSongModel, modifier: Modifier) {
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
            Text(song.title, modifier = Modifier
                .constrainAs(songTitle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                })
        }
    }
}