package moe.smoothie.androidide.themestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import moe.smoothie.androidide.themestore.ui.JetbrainsStoreScroller
import moe.smoothie.androidide.themestore.ui.TabbedToolbar
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme
import okhttp3.OkHttpClient
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var httpClient: OkHttpClient

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

@Composable
fun MainActivityView() {
    val pagerState = rememberPagerState(pageCount = { 2 })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TabbedToolbar(
                tabNames = listOf(
                    stringResource(R.string.source_jetbrains),
                    stringResource(R.string.source_vscode)
                ),
                tabIcons = listOf(
                    painterResource(R.drawable.icons8_jetbrains),
                    painterResource(R.drawable.icons8_visual_studio)
                ),
                pagerState = pagerState
            )
        },
        content = { innerPadding ->
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                contentPadding = innerPadding
            ) { pageIndex ->
                when (pageIndex) {
                    0 -> JetbrainsStoreScroller()
                    1 -> PageContent("Something 1")
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
fun PageContent(text: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text)
    }
}
