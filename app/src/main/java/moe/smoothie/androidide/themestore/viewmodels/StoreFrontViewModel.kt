package moe.smoothie.androidide.themestore.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class StoreFrontViewModel<T> : ViewModel() {
    abstract val itemsPerPage: Int

    protected val mutableItems = MutableStateFlow(emptyList<T>())
    val items: StateFlow<List<T>> = mutableItems

    protected val mutableIsLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = mutableIsLoading

    protected val mutableAllItemsLoaded = MutableStateFlow(false)
    val allItemsLoaded: StateFlow<Boolean> = mutableAllItemsLoaded

    protected val mutableErrorReceiving = MutableStateFlow(false)
    val errorReceiving: StateFlow<Boolean> = mutableErrorReceiving

    protected val mutableDeviceHasNetwork = MutableStateFlow(false)
    val deviceHasNetwork: StateFlow<Boolean> = mutableDeviceHasNetwork

    protected val mutableErrorParsingResponse = MutableStateFlow(false)
    val errorParsingResponse: StateFlow<Boolean> = mutableErrorParsingResponse

    protected val mutableSearchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = mutableSearchQuery

    abstract fun loadItems(context: Context)

    open fun reload(context: Context) {
        mutableItems.update { emptyList() }
        mutableAllItemsLoaded.update { false }
        loadItems(context)
    }

    open fun setSearchQuery(query: String, context: Context) {
        mutableSearchQuery.update { query }
        reload(context)
    }
}
