package ru.alexeypostnov.chordflow.presentation.songDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.alexeypostnov.chordflow.data.model.ChordLineModel
import ru.alexeypostnov.chordflow.presentation.ErrorComponent

@Composable
fun SongDetailsScreen(
    songId: String,
) {
    val viewModel: SongDetailsViewModel = koinViewModel()

    LaunchedEffect(songId) {
        viewModel.loadSongDetailsWithCache(songId)
        viewModel.loadSongDetails(songId)
    }

    val parsedText by viewModel.parsedText.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    SongDetailsScreenContent(
        parsedText = parsedText,
        isLoading = isLoading,
        error = error
    )
}

@Composable
fun SongDetailsScreenContent(
    parsedText: List<ChordLineModel>,
    isLoading: Boolean,
    error: String?,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        val (songText, errorComponent) = createRefs()

        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        if (!error.isNullOrEmpty()) {
            ErrorComponent(error, modifier = Modifier
                .constrainAs(errorComponent) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                })
        }

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .constrainAs(songText) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        ) {
            items(parsedText) { chordLine ->
                ChordLineItemSeparate(chordLine = chordLine)
            }
        }
    }
}

@Composable
fun ChordLineItemSeparate(chordLine: ChordLineModel) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        if (chordLine.isComment) {
            Text(
                text = chordLine.lyrics,
                color = colorScheme.secondary,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        } else {
            if (chordLine.chords.isNotEmpty()) {
                Text(
                    text = buildString {
                        var lastPos = 0
                        chordLine.chords.sortedBy { it.position }.forEach { chord ->
                            repeat(chord.position - lastPos) {
                                append(" ")
                            }
                            append(chord.chord)
                            lastPos = chord.position + chord.chord.length
                        }
                    },
                    color = colorScheme.onPrimaryContainer,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }

            Text(
                text = chordLine.lyrics,
                fontSize = 18.sp
            )
        }
    }
}