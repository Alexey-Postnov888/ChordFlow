package ru.alexeypostnov.chordflow.presentation.authorsList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import org.koin.androidx.compose.koinViewModel
import ru.alexeypostnov.chordflow.data.model.ResponseAuthorDetailsModel
import ru.alexeypostnov.chordflow.navigation.SongsListNavigationScreen
import ru.alexeypostnov.chordflow.presentation.ErrorComponent

@Composable
fun AuthorsListScreen() {
    val viewModel: AuthorsListViewModel = koinViewModel()
    val navigator = LocalNavigator.current

    LaunchedEffect(Unit) {
        viewModel.loadAuthorsList()
    }

    val authors by viewModel.authors.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    AuthorsListScreenContent(
        authors = authors,
        onAuthorClick = { author ->
            navigator?.push(
                SongsListNavigationScreen(author = author.author)
            )
        },
        isLoading = isLoading,
        error = error
    )
}

@Composable
fun AuthorsListScreenContent(
    authors: List<ResponseAuthorDetailsModel>,
    onAuthorClick: (ResponseAuthorDetailsModel) -> Unit,
    isLoading: Boolean,
    error: String?
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (authorsList, errorComponent) = createRefs()

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

            !isLoading && authors.isEmpty() -> {
                ErrorComponent(error = "Авторы не найдены...", modifier = Modifier
                    .constrainAs(errorComponent) {
                        centerHorizontallyTo(parent)
                        centerVerticallyTo(parent)
                    })
            }

            authors.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(10.dp)
                        .constrainAs(authorsList) {
                            top.linkTo(parent.top)
                            centerHorizontallyTo(parent)
                        }
                ) {
                    items(authors, key = { it.author}) { author ->
                        AuthorItem(
                            author,
                            modifier = Modifier
                                .clickable(onClick = {
                                    onAuthorClick(author)
                                })
                        )
                    }
                }
            }

            else -> {  }
        }
    }
}

@Composable
fun AuthorItem(author: ResponseAuthorDetailsModel, modifier: Modifier) {
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
            val (authorTitle, songsCount) = createRefs()
            Text(author.author,
                fontSize = 20.sp,
                color = colorScheme.primary,
                modifier = Modifier.constrainAs(authorTitle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                })

            Text("Всего песен: ${author.songsCount}",
                fontSize = 18.sp,
                color = colorScheme.secondary,
                modifier = Modifier.constrainAs(songsCount) {
                    top.linkTo(authorTitle.bottom, margin = 5.dp)
                    start.linkTo(authorTitle.start)
                })
        }
    }
}