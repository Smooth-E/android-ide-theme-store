package moe.smoothie.androidide.themestore.viewmodels

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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.coroutines.executeAsync
import javax.inject.Inject

@HiltViewModel
class JetbrainsStoreViewModel @Inject constructor(
    private val httpClient: OkHttpClient
) : ViewModel() {
    private val tag = "JetbrainsStoreViewModel";

    private val _items = MutableStateFlow<List<JetbrainsStorefrontResponse.Plugin>>(emptyList())
    val items: StateFlow<List<JetbrainsStorefrontResponse.Plugin>> = _items

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

            try {
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
                    } catch (exception: Exception) {
                        Log.e(tag, "Error serializing the response")
                        Log.e(tag, "Response body:\n${responseBody.split(",").joinToString("\n")}")
                        Log.e(tag, exception.stackTraceToString())
                        return@use
                    }

                    _items.update { list -> list + data.plugins }

                    if (data.total <= items.value.size) {
                        _allItemsLoaded.update { true }
                    }
                }
            }
            catch (exception: Exception) {
                Log.e(tag, "Exception loading ne items for ${getPageUrl(items.value.size, pageSize)}")
                exception.printStackTrace()
            }

            _isLoading.update { false }
        }
    }

    private fun getPageUrl(offset: Int, pageSize: Int) =
        "https://plugins.jetbrains.com/api/searchPlugins?excludeTags=internal&includeTags=theme&max=${pageSize}&offset=${offset}&tags=Theme"
}
