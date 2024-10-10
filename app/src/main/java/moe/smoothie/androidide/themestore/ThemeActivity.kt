package moe.smoothie.androidide.themestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import moe.smoothie.androidide.themestore.ui.ThemeAppBar
import moe.smoothie.androidide.themestore.ui.ThemeAppBarState
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme
import okhttp3.OkHttpClient
import javax.inject.Inject

@AndroidEntryPoint
class ThemeActivity : ComponentActivity() {
    @Inject
    lateinit var httpClient: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidIDEThemesTheme {
                ThemeActivityView()
            }
        }
    }
}

@Composable
fun ThemeActivityView() {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { ThemeAppBar(
            state = ThemeAppBarState(
                iconUrl = "https://plugins.jetbrains.com/files/8006/607095/icon/pluginIcon.svg",
                themeName =  "Material Icons Theme, but it has a longer name",
                rating = 3.5f,
                downloads = 3_456_789,
                author = "John Doe",
                description = "A mock theme description for preview purposes",
                domain = "https://microsoft.com",
                isVerified = true
            ),
            scrollOffset = scrollState.value
        ) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            repeat(30) {
                Text("Something $it", Modifier.padding(10.dp))
            }
        }
    }
}

@Preview
@Composable
internal fun ThemeActivityPreview() {
    AndroidIDEThemesTheme {
        ThemeActivityView()
    }
}
