package moe.smoothie.androidide.themestore.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import moe.smoothie.androidide.themestore.viewmodels.MicrosoftStoreViewModel

@Composable
fun MicrosoftStoreScroller(
    backStackEntry: NavBackStackEntry,
    viewModel: MicrosoftStoreViewModel = hiltViewModel(backStackEntry)
) {
    StoreFrontScroller(
        viewModel = viewModel,
        cardComposable = { MicrosoftStoreCard(it) },
        minSize = 150.dp
    )
}
