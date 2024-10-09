package moe.smoothie.androidide.themestore.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import moe.smoothie.androidide.themestore.viewmodels.JetbrainsStoreViewModel

@Composable
fun JetbrainsStoreScroller(
    backStackEntry: NavBackStackEntry,
    viewModel: JetbrainsStoreViewModel = hiltViewModel(backStackEntry)
) {
    StoreFrontScroller(
        cardComposable = { state -> JetbrainsThemeCard(state) },
        viewModel = viewModel
    )
}
