package moe.smoothie.androidide.themestore.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import moe.smoothie.androidide.themestore.data.JetbrainsStorefrontResponse
import moe.smoothie.androidide.themestore.ui.JetbrainsThemeCardState
import moe.smoothie.androidide.themestore.ui.LoadingStatus
import moe.smoothie.androidide.themestore.util.hasNetwork
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.coroutines.executeAsync
import javax.inject.Inject

@HiltViewModel
class JetbrainsStoreViewModel @Inject constructor(
    private val httpClient: OkHttpClient
) : StoreFrontViewModel<JetbrainsThemeCardState>() {
    override val itemsPerPage: Int = 10

    private val tag = "JetbrainsStoreViewModel"
    private val basePreviewUrl = "https://downloads.marketplace.jetbrains.com"

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun loadItems(context: Context) {
        if (!hasNetwork(context)) {
            mutableLoadingStatus.update { LoadingStatus.NO_NETWORK }
            return
        }

        mutableLoadingStatus.update { LoadingStatus.LOADING }

        viewModelScope.launch(Dispatchers.IO) {
            if (mutableAllItemsLoaded.value) {
                return@launch
            }

            if (mutableIsLoading.value) {
                return@launch
            }

            mutableIsLoading.update { true }

            val request = Request.Builder()
                .url(getPageUrl(mutableItems.value.size, itemsPerPage))
                .build()

            try {
                httpClient.newCall(request).executeAsync().use { response ->
                    if (!response.isSuccessful) {
                        Log.d(tag, "Request was not successful.\nUrl: ${request.url}")
                        mutableLoadingStatus.update { LoadingStatus.ERROR_RECEIVING }
                        return@use
                    }

                    var data: JetbrainsStorefrontResponse? = null
                    var responseBody = ""
                    try {
                        responseBody = response.body.string()
                        data = Json.decodeFromString<JetbrainsStorefrontResponse>(responseBody)
                    } catch (exception: Exception) {
                        Log.e(tag, "Error serializing the response")
                        Log.e(tag, "Response body:\n${responseBody.split(",").joinToString("\n")}")
                        Log.e(tag, exception.stackTraceToString())
                        mutableLoadingStatus.update { LoadingStatus.ERROR_PARSING }
                        return@use
                    }

                    mutableItems.update { list ->
                        list + data.plugins.map { plugin ->
                            JetbrainsThemeCardState(
                                previewUrl = basePreviewUrl + plugin.previewImage,
                                name = plugin.name,
                                rating = plugin.rating,
                                downloads = plugin.downloads,
                                trimmedDescription = plugin.preview
                            )
                        }
                    }

                    if (data.total <= items.value.size) {
                        mutableAllItemsLoaded.update { true }
                    }
                }
            } catch (exception: Exception) {
                val url = getPageUrl(items.value.size, itemsPerPage)
                Log.e(tag, "Exception loading ne items for $url")
                exception.printStackTrace()
                mutableLoadingStatus.update { LoadingStatus.ERROR_RECEIVING }
            }

            mutableIsLoading.update { false }
        }
    }

    private fun getPageUrl(offset: Int, pageSize: Int) =
        "https://plugins.jetbrains.com/api/searchPlugins?excludeTags=internal&includeTags=theme&max=${pageSize}&offset=${offset}&tags=Theme&search=${searchQuery.value}"
}
