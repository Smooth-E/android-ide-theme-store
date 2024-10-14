package moe.smoothie.androidide.themestore.ui

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import moe.smoothie.androidide.themestore.R
import moe.smoothie.androidide.themestore.util.isScrolled
import moe.smoothie.androidide.themestore.viewmodels.StoreFrontViewModel

@Composable
fun <State> StoreFrontScroller(
    viewModel: StoreFrontViewModel<State>,
    cardComposable: @Composable (State) -> Unit,
    minSize: Dp = 300.dp
) {
    val tag = "StoreFrontScroller"

    val cards by viewModel.items.collectAsState()
    Log.d(tag, "Composing the scroller with ${cards.size} items")

    val lazyGridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val isLoading by viewModel.isLoading.collectAsState()
    val allItemsLoaded by viewModel.allItemsLoaded.collectAsState()
    val errorParsingResponse by viewModel.errorParsingResponse.collectAsState()
    val errorReceiving by viewModel.errorReceiving.collectAsState()
    val deviceHasNetwork by viewModel.deviceHasNetwork.collectAsState()

    var searchBarHeight by remember { mutableIntStateOf(0) }

    Box(Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Adaptive(minSize = minSize),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp),
            state = lazyGridState
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                val height = with(LocalDensity.current) { searchBarHeight.toDp() }
                Box(Modifier.height(height + 16.dp))
            }
            items(
                count = cards.size,
                key = { index -> "${cards[index].hashCode()}-$index" }
            ) { index ->
                Box(Modifier.animateItem()) {
                    cardComposable(cards[index])
                }
                if (index == cards.size - viewModel.itemsPerPage / 2) {
                    SideEffect {
                        if (!isLoading) {
                            viewModel.loadItems(context)
                        }
                    }
                }
            }
            if (cards.isEmpty()) {
                item {
                    SideEffect {
                        viewModel.loadItems(context)
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
                        if (!deviceHasNetwork) {
                            LoadingFooterCard(
                                modifier = modifier,
                                lazyGridState = lazyGridState,
                                viewModel = viewModel,
                                coroutineScope = coroutineScope,
                                context = LocalContext.current,
                                loadingStatus = LoadingStatus.NO_NETWORK
                            )
                        } else if (errorReceiving) {
                            LoadingFooterCard(
                                modifier = modifier,
                                lazyGridState = lazyGridState,
                                viewModel = viewModel,
                                coroutineScope = coroutineScope,
                                context = LocalContext.current,
                                loadingStatus = LoadingStatus.ERROR_RECEIVING
                            )
                        } else if (errorParsingResponse) {
                            LoadingFooterCard(
                                modifier = modifier,
                                lazyGridState = lazyGridState,
                                viewModel = viewModel,
                                coroutineScope = coroutineScope,
                                context = LocalContext.current,
                                loadingStatus = LoadingStatus.ERROR_PARSING
                            )
                        }
                    } else {
                        FooterListEnd(
                            modifier = modifier,
                            lazyGridState = lazyGridState,
                            viewModel = viewModel,
                            coroutineScope = coroutineScope,
                            cardsSize = cards.size,
                            context = LocalContext.current
                        )
                    }
                }
            }
        }

        PillSearchField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter)
                .onGloballyPositioned { searchBarHeight = it.size.height }
                .shadow(
                    elevation = if (lazyGridState.isScrolled()) 3.dp else 0.dp,
                    shape = RoundedCornerShape(300.dp)
                ),
            onValueChanged = { viewModel.setSearchQuery(it, context) },
        )

        val isFabVisible by remember { derivedStateOf { lazyGridState.firstVisibleItemIndex } }
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            visible = isFabVisible > 0,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        lazyGridState.animateScrollToItem(0)
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_upward_24),
                    contentDescription = stringResource(R.string.content_description_scroll_to_top)
                )
            }
        }
    }
}

private fun <State> reloadCallback(
    coroutineScope: CoroutineScope,
    lazyGridState: LazyGridState,
    viewModel: StoreFrontViewModel<State>,
    context: Context
) {
    coroutineScope.launch {
        lazyGridState.animateScrollToItem(0)
        viewModel.reload(context)
    }
}

@Composable
internal fun <State> LoadingFooterCard(
    modifier: Modifier,
    loadingStatus: LoadingStatus,
    context: Context,
    lazyGridState: LazyGridState,
    viewModel: StoreFrontViewModel<State>,
    coroutineScope: CoroutineScope
) {
    ElevatedCard {
        StatusView(
            modifier = modifier,
            loadingStatus = loadingStatus,
            reloadCallback = { reloadCallback(coroutineScope, lazyGridState, viewModel, context) }
        )
    }
}

@Composable
internal fun <State> FooterListEnd(
    modifier: Modifier,
    lazyGridState: LazyGridState,
    viewModel: StoreFrontViewModel<State>,
    coroutineScope: CoroutineScope,
    context: Context,
    cardsSize: Int
) {
    ElevatedCard {
        StatusView(
            modifier = modifier,
            hero = {
                Icon(
                    painter = painterResource(R.drawable.round_auto_awesome_24),
                    contentDescription = null
                )
            },
            header = { Text(stringResource(R.string.header_all_loaded)) },
            description = {
                Text(
                    stringResource(R.string.description_all_loaded)
                        .format(cardsSize)
                )
            },
            reloadCallback = { reloadCallback(coroutineScope, lazyGridState, viewModel, context) }
        )
    }
}
