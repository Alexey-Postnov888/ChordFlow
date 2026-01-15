package ru.alexeypostnov.chordflow.presentation.editSong

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditSongScreen(songId: String) {
    val viewModel: EditSongViewModel = koinViewModel()

    val songTitle by viewModel.songTitle.collectAsStateWithLifecycle()
    val songAuthor by viewModel.songAuthor.collectAsStateWithLifecycle()
    val songText by viewModel.songText.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(error) {
        if (!error.isNullOrEmpty()) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            viewModel.resetErrorMessage()
        }
    }

    LaunchedEffect(songId) {
        viewModel.loadSongDetails(songId)
    }

    EditSongScreenContent(
        songTitle = songTitle,
        songAuthor = songAuthor,
        songText = songText,
        onTitleChanged = viewModel::updateSongTitle,
        onAuthorChanged = viewModel::updateSongAuthor,
        onTextChanged = viewModel::updateSongText,
    )
}

@Composable
fun EditSongScreenContent(
    songTitle: String,
    songAuthor: String,
    songText: String,
    onTitleChanged: (String) -> Unit,
    onAuthorChanged: (String) -> Unit,
    onTextChanged: (String) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        val (songTitleTextField,
            songAuthorTextField,
            songTextTextField) = createRefs()

        OutlinedTextField(
            value = songTitle,
            onValueChange = onTitleChanged,
            label = { Text("Название") },
            placeholder = { Text("Видели ночь") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .constrainAs(songTitleTextField) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )

        OutlinedTextField(
            value = songAuthor,
            onValueChange = onAuthorChanged,
            label = { Text("Исполнгитель") },
            placeholder = { Text("Кино") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(top = 5.dp)
                .constrainAs(songAuthorTextField) {
                    top.linkTo(songTitleTextField.bottom)
                    start.linkTo(songTitleTextField.start)
                }
        )

        OutlinedTextField(
            value = songText,
            onValueChange = onTextChanged,
            label = { Text("Текст") },
            placeholder = { Text("[C]Видели ночь, [G]гуляли всю ночь") },
            shape = RoundedCornerShape(12.dp),
            minLines = 8,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .imePadding()
                .constrainAs(songTextTextField) {
                    top.linkTo(songAuthorTextField.bottom)
                    start.linkTo(songAuthorTextField.start)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
        )
    }
}

@Composable
@Preview
fun EditSongScreenPreview() {

}