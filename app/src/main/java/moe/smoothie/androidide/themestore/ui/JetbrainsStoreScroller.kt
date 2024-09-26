package moe.smoothie.androidide.themestore.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import moe.smoothie.androidide.themestore.viewmodels.JetbrainsStoreViewModel

@Composable
fun JetbrainsStoreScroller(
    navBackStackEntry: NavBackStackEntry,
    pageViewModel: JetbrainsStoreViewModel = hiltViewModel(navBackStackEntry)
) {
    val tag = "JetbrainsStoreScroller"
    val itemsPerPage = 20

    val cards by pageViewModel.items.collectAsState()
    val isLoading by pageViewModel.isLoading.collectAsState()
    val allItemsLoaded by pageViewModel.allItemsLoaded.collectAsState()

    Log.d(tag, "Composing the scroller with ${cards.size} items")

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 300.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cards.size) { index ->
            JetbrainsThemeCard(cards[index])

            if (index == cards.size - itemsPerPage / 2) {
                SideEffect {
                    if (!isLoading) {
                        pageViewModel.loadItems(itemsPerPage)
                    }
                }
            }
        }

        item {
            SideEffect {
                if (cards.isEmpty()) {
                    pageViewModel.loadItems(itemsPerPage)
                }
            }
        }

        item {
            if (!allItemsLoaded) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp)

                    )
                    Text("Someting", modifier = Modifier.fillMaxWidth())
                }
            } else {
                Text(text = "All items loaded", modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
