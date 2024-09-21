package moe.smoothie.androidide.themestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.tokens.PrimaryNavigationTabTokens
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@Composable
fun MainActivityView() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TabbedToolbar(selectedTabIndex) },
        content = { innerPadding ->
            Text("Hello Android!")
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
@OptIn(ExperimentalMaterial3Api::class)
fun TabbedToolbar(selectedTabIndex: Int) {
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

        TabRow(selectedTabIndex) {
            tabNames.forEachIndexed { index, title ->
                LeadingIconTab(
                    selectedTabIndex == index,
                    text = { Text(title) },
                    icon = { Icon(tabIcons[index], null, Modifier.size(24.dp)) },
                    onClick = { println("Switch to fragment $index") }
                )
            }
        }
    }
}
