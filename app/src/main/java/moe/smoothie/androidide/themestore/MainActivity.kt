package moe.smoothie.androidide.themestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidIDEThemesTheme {
                MainActivityView()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainActivityView() {
    val pagerState = rememberPagerState(pageCount = { 2 })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TabbedToolbar(pagerState) },
        content = { innerPadding ->
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false
            ) { pageIndex ->
                when (pageIndex) {
                    0 -> PageContent("Something 1")
                    1 -> PageContent("Something 2")
                }
            }
        }
    )
}

@Preview
@Composable
fun MainActivityPreview() {
    AndroidIDEThemesTheme {
        MainActivityView()
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun TabbedToolbar(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    val tabNames = listOf(
        stringResource(R.string.source_jetbrains),
        stringResource(R.string.source_vscode)
    )

    val tabIcons = listOf(
        painterResource(R.drawable.icons8_jetbrains),
        painterResource(R.drawable.icons8_visual_studio)
    )

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
                        stringResource(R.string.content_description_settings)
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

@Composable
fun PageContent(text: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text)
    }
}
