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
    val itemsPerPage = 40
    val cards = viewModel.items.collectAsState()
    val allLoaded = viewModel.allItemsLoaded.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()

    StoreFrontScroller(
        cards = cards.value,
        allItemsLoaded = allLoaded.value,
        isLoading = isLoading.value,
        itemsPerPage = itemsPerPage,
        cardComposable = { state -> JetbrainsThemeCard(state) },
        viewModel = viewModel
    )
}
