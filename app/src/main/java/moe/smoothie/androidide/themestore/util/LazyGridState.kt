package moe.smoothie.androidide.themestore.util

import androidx.compose.foundation.lazy.grid.LazyGridState

fun LazyGridState.isScrolled(): Boolean =
    (firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset != 0) || firstVisibleItemIndex != 0
