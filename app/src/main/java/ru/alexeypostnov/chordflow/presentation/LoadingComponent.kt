package ru.alexeypostnov.chordflow.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun LoadingComponent(modifier: Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
    ) {
        val (progress, label) = createRefs()
        CircularProgressIndicator(
            color = colorScheme.secondary,
            modifier = Modifier
                .constrainAs(progress) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                }
        )
        Text(
            text = "Загрузка...",
            fontSize = 16.sp,
            modifier = Modifier
                .padding(top = 5.dp)
                .constrainAs(label) {
                    top.linkTo(progress.bottom)
                    centerHorizontallyTo(parent)
                }
        )
    }
}