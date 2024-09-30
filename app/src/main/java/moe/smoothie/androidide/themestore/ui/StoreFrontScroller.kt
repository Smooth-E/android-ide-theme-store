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

@Composable
fun <State> StoreFrontScroller(
    cards: List<State>,
    isLoading: Boolean,
    allItemsLoaded: Boolean,
    itemsPerPage: Int,
    cardComposable: @Composable (State) -> Unit,
    loadItems: () -> Unit
) {
    val tag = "StoreFrontScroller"

    Log.d(tag, "Composing the scroller with ${cards.size} items")

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 300.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cards.size) { index ->
            cardComposable(cards[index])

            if (index == cards.size - itemsPerPage / 2) {
                SideEffect {
                    if (!isLoading) {
                        loadItems()
                    }
                }
            }
        }

        item {
            SideEffect {
                if (cards.isEmpty()) {
                    loadItems()
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
                    Text("Something", modifier = Modifier.fillMaxWidth())
                }
            } else {
                Text(text = "All items loaded", modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
