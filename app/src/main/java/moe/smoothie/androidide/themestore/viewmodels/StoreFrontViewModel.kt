package moe.smoothie.androidide.themestore.viewmodels

import android.content.Context
import kotlinx.coroutines.flow.StateFlow

interface StoreFrontViewModel<T> {
    val items: StateFlow<List<T>>
    val isLoading: StateFlow<Boolean>
    val allItemsLoaded: StateFlow<Boolean>
    val errorReceiving: StateFlow<Boolean>
    val deviceHasNetwork: StateFlow<Boolean>
    val errorParsingResponse: StateFlow<Boolean>

    fun loadItems(context: Context, pageSize: Int)

    fun reload(context: Context, pageSize: Int)
}
