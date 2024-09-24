package moe.smoothie.androidide.themestore.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import moe.smoothie.androidide.themestore.data.JetbrainsStorefrontResponse
import moe.smoothie.androidide.themestore.ui.JetbrainsThemeCardState
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.coroutines.executeAsync
import javax.inject.Inject

@HiltViewModel
class JetbrainsStoreViewModel @Inject constructor(
    private val httpClient: OkHttpClient
) : ViewModel() {
    private val tag = "JetbrainsStoreViewModel";

    private val _items = MutableStateFlow<List<JetbrainsThemeCardState>>(emptyList())
    val items: StateFlow<List<JetbrainsThemeCardState>> = _items

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _allItemsLoaded = MutableStateFlow(false)
    val allItemsLoaded: StateFlow<Boolean> = _allItemsLoaded

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadItems(pageSize: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_allItemsLoaded.value) {
                return@launch
            }

            if (_isLoading.value) {
                return@launch
            }

            _isLoading.update { true }

            val request = Request.Builder()
                .url(getPageUrl(_items.value.size, pageSize))
                .build()

            httpClient.newCall(request).executeAsync().use { response ->
                if (!response.isSuccessful) {
                    Log.d(tag, "Request was not successful.\nUrl: ${request.url}")
                    return@use
                }

                var data: JetbrainsStorefrontResponse? = null
                var responseBody = ""
                try {
                    responseBody = response.body.string()
                    data = Json.decodeFromString<JetbrainsStorefrontResponse>(responseBody)
                }
                catch (exception: Exception) {
                    Log.e(tag, "Error serializing the response")
                    Log.e(tag, "Response body:\n${responseBody.split(",").joinToString("\n")}")
                    Log.e(tag, exception.stackTraceToString())
                    return@use
                }

                val baseUrl = "https://downloads.marketplace.jetbrains.com"
                val deferredIcons = data.plugins.map {
                    async { loadBitmap(baseUrl + it.icon) }
                }
                val deferredPreviews = data.plugins.map {
                    async { loadBitmap(/* baseUrl + it.previewImage*/ "https://picsum.photos/250?image=9") }
                }

                val icons = deferredIcons.awaitAll()
                val previews = deferredPreviews.awaitAll()

                _items.update { list ->
                    list + data.plugins.mapIndexed { index, plugin ->
                        val preview = previews[index]
                        Log.d(tag, "Preview for index $index is null? ${preview == null} ${preview?.asImageBitmap() == null}")
                        JetbrainsThemeCardState(
                            plugin,
                            icons[index],
                            previews[index]
                        )
                    }
                }

                if (data.total <= items.value.size) {
                    _allItemsLoaded.update { true }
                }
            }

            _isLoading.update { false }
        }
    }

    private fun getPageUrl(offset: Int, pageSize: Int) =
        "https://plugins.jetbrains.com/api/searchPlugins?excludeTags=internal&includeTags=theme&max=${pageSize}&offset=${offset}&tags=Theme"

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun loadBitmap(url: String): Bitmap? {
        if (url.isEmpty()) {
            return null
        }

        Log.d(tag, "Loading bitmap from url: $url")
        val request = Request.Builder().url(url).build()

        httpClient.newCall(request).executeAsync().use { response ->
            if (!response.isSuccessful) {
                Log.d(tag, "Failed to load bitmap: $url")
                return null
            }

            try {
                val bitmap = BitmapFactory.decodeStream(response.body.byteStream())
                Log.d(tag, "Returning the bitmap for url $url")
                return bitmap
            } catch (exception: Exception) {
                Log.d(tag, "Failed to decode bitmap from stream.")
                Log.d(tag, exception.stackTrace.toString())
                return null
            }
        }
    }
}
