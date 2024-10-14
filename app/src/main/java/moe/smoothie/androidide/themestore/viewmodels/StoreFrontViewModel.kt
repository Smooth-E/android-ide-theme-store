package moe.smoothie.androidide.themestore.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import moe.smoothie.androidide.themestore.ui.LoadingStatus

abstract class StoreFrontViewModel<T> : ViewModel() {
    abstract val itemsPerPage: Int

    protected val mutableItems = MutableStateFlow(emptyList<T>())
    val items: StateFlow<List<T>> = mutableItems

    protected val mutableIsLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = mutableIsLoading

    protected val mutableAllItemsLoaded = MutableStateFlow(false)
    val allItemsLoaded: StateFlow<Boolean> = mutableAllItemsLoaded

    protected val mutableLoadingStatus = MutableStateFlow(LoadingStatus.LOADING)
    val loadingStatus: StateFlow<LoadingStatus> = mutableLoadingStatus

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
