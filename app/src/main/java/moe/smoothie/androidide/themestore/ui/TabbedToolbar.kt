package moe.smoothie.androidide.themestore.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import moe.smoothie.androidide.themestore.R

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun TabbedToolbar(tabNames: List<String>, tabIcons: List<Painter>, pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    Column {
        TopAppBar(
            title = {
                Text(stringResource(R.string.app_name))
            },
            actions = {
                IconButton(
                    onClick = { println("Imagine you opened settings") }
                ) {
                    Icon(
                        Icons.Rounded.Settings,
                        stringResource(R.string.destination_settings)
                    )
                }
            }
        )

        TabRow(pagerState.currentPage) {
            tabNames.forEachIndexed { index, title ->
                LeadingIconTab(
                    pagerState.currentPage == index,
                    text = { Text(title) },
                    icon = { Icon(tabIcons[index], null, Modifier.size(24.dp)) },
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }
    }
}
