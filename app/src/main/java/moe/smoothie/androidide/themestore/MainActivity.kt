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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import moe.smoothie.androidide.themestore.ui.JetbrainsStoreScroller
import moe.smoothie.androidide.themestore.ui.NavigationBarRoute
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
    val routeJetbrainsMarketplace = NavigationBarRoute(
        R.string.source_jetbrains,
        R.drawable.icons8_jetbrains,
        "jetbrains-marketplace"
    )
    val routeVSCodeMarketplace = NavigationBarRoute(
        R.string.source_vscode,
        R.drawable.icons8_visual_studio,
        "vscode-marketplace"
    )
    val routeSettings = NavigationBarRoute(
        R.string.destination_settings,
        R.drawable.baseline_settings_24,
        "settings"
    )
    val routes = listOf(routeJetbrainsMarketplace, routeVSCodeMarketplace, routeSettings)

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = routeJetbrainsMarketplace.route,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable(routeJetbrainsMarketplace.route) { JetbrainsStoreScroller(it) }
                composable(routeVSCodeMarketplace.route) { PageContent("VSCode themes will be here") }
                composable(routeSettings.route) { PageContent("Settings will be here") }
            }
        },
        bottomBar = { BottomNavigationBar(navController, routes) }
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

@Composable
fun BottomNavigationBar(navController: NavController, routes: List<NavigationBarRoute>) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        routes.forEach { route ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(route.iconResource),
                        contentDescription = stringResource(route.nameResource),
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(stringResource(route.nameResource)) },
                // TODO: Fix route selection
                selected = currentDestination?.route == route.route,
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
