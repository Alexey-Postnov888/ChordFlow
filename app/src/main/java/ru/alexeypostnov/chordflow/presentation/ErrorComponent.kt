package ru.alexeypostnov.chordflow.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun ErrorComponent(error: String, modifier: Modifier) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
    ) {
        val (errorIcon, label) = createRefs()
        Icon(
            Icons.Rounded.Warning,
            contentDescription = "Ошибка",
            modifier = Modifier
                .constrainAs(errorIcon) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                },
        )
        Text(
            text = error,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(top = 5.dp)
                .constrainAs(label) {
                    top.linkTo(errorIcon.bottom)
                    centerHorizontallyTo(parent)
                }
        )
    }
}