package moe.smoothie.androidide.themestore.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import moe.smoothie.androidide.themestore.ThemeState
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltViewModel
class ThemeActivityViewModel @Inject constructor(
    private val httpClient: OkHttpClient
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _themeState = MutableStateFlow<ThemeState?>(null)
    val themeState: StateFlow<ThemeState?> = _themeState

    fun loadInfo() {

    }
}
