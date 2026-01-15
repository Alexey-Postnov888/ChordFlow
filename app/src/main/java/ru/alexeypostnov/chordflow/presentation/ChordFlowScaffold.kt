package ru.alexeypostnov.chordflow.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChordFlowScaffold(
    title: String,
    showBackButton: Boolean = true,
    navigator: Navigator,
    onBackButtonClick: () -> Unit = { navigator.pop() },
    floatingActionButton: @Composable (() -> Unit)? = null,
    menuItems: List<MenuItemModel>? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(
                            onClick = { onBackButtonClick() }
                        ) {
                            Icon(
                                Icons.Rounded.ArrowBack,
                                contentDescription = "Назад"
                            )
                        }
                    }
                },
                actions = {
                    if (menuItems != null) {
                        IconButton(
                            onClick = { expanded = true }
                        ) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Меню"
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            shape = RoundedCornerShape(12.dp),
                            offset = DpOffset(x = (-8).dp, y = 8.dp)
                        ) {
                            menuItems.forEach { menuItem ->
                                DropdownMenuItem(
                                    text = { Text(menuItem.text) },
                                    onClick = {
                                        expanded = false
                                        menuItem.onClick()
                                    },
                                    leadingIcon = {
                                        Icon(
                                            menuItem.icon,
                                            contentDescription = null,
                                            tint = colorScheme.primary
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = { floatingActionButton?.invoke() },
        content = content
    )
}