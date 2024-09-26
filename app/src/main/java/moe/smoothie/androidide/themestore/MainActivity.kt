package moe.smoothie.androidide.themestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import moe.smoothie.androidide.themestore.data.JetbrainsStorefrontResponse
import moe.smoothie.androidide.themestore.ui.JetbrainsStoreScroller
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

data class TopLevelRoute<T : Any>(
    val nameResource: Int,
    val icon: Painter,
    val route: T
)

@Serializable
data object JetbrainsMarketplace

@Serializable
data object VSCodeMarketplace

@Serializable
data object Settings

@Composable
fun MainActivityView() {
    val routes = listOf(
        TopLevelRoute(
            R.string.source_jetbrains,
            painterResource(R.drawable.icons8_jetbrains),
            JetbrainsMarketplace
        ),
        TopLevelRoute(
            R.string.source_vscode,
            painterResource(R.drawable.icons8_visual_studio),
            VSCodeMarketplace
        ),
        TopLevelRoute(
            R.string.destination_settings,
            painterResource(R.drawable.baseline_settings_24),
            Settings
        )
    )

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = JetbrainsMarketplace,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable<JetbrainsMarketplace> { JetbrainsStoreScroller(it) }
                composable<VSCodeMarketplace> { PageContent("VSCode themes will be here") }
                composable<Settings> { PageContent("Settings will be here") }
            }
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                routes.forEach { route ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = route.icon,
                                contentDescription = stringResource(route.nameResource),
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = { Text(stringResource(route.nameResource)) },
                        // TODO: Fix route selection
                        selected = currentDestination != null && currentDestination.route!!::class == route.route::class,
                        onClick = {
                            navController.navigate(route.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
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
