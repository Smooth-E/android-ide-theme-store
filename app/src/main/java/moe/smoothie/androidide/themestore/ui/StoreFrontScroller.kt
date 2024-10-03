package moe.smoothie.androidide.themestore.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import kotlinx.coroutines.launch
import moe.smoothie.androidide.themestore.R
import moe.smoothie.androidide.themestore.viewmodels.StoreFrontViewModel

@Composable
fun <State> StoreFrontScroller(
    cards: List<State>,
    isLoading: Boolean,
    allItemsLoaded: Boolean,
    itemsPerPage: Int,
    cardComposable: @Composable (State) -> Unit,
    viewModel: StoreFrontViewModel
) {
    val tag = "StoreFrontScroller"

    Log.d(tag, "Composing the scroller with ${cards.size} items")

    val lazyGridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Adaptive(minSize = 300.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        state = lazyGridState
    ) {
        items(
            count = cards.size,
            key = { index -> cards[index].hashCode() }
        ) { index ->
            Box(Modifier.animateItem()) {
                cardComposable(cards[index])
            }
            if (index == cards.size - itemsPerPage / 2) {
                SideEffect {
                    if (!isLoading) {
                        viewModel.loadItems(itemsPerPage)
                    }
                }
            }
        }
        if (cards.isEmpty()) {
            item {
                SideEffect {
                    viewModel.loadItems(itemsPerPage)
                }
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val modifier = Modifier.width(min(450.dp, maxWidth))

                if (!allItemsLoaded) {
                    FooterCard(
                        modifier = modifier,
                        hero = {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        header = { Text(stringResource(R.string.header_fetching_items)) },
                        message = { Text(stringResource(R.string.message_fetching_items)) }
                    )
                } else {
                    FooterCard(
                        modifier = modifier,
                        hero = {
                            Icon(
                                painter = painterResource(R.drawable.round_auto_awesome_24),
                                contentDescription = null
                            )
                        },
                        header = { Text(stringResource(R.string.header_all_loaded)) },
                        message = {
                            Text(
                                stringResource(R.string.description_all_loaded)
                                    .format(cards.size)
                            )
                        },
                        button = {
                            FilledTonalButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    coroutineScope.launch {
                                        lazyGridState.animateScrollToItem(0)
                                        viewModel.reload(itemsPerPage)
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_refresh_24),
                                    contentDescription = null
                                )
                                Text(stringResource(R.string.button_reload))

                            }
                        },
                    )
                }
            }
        }
    }
}
