package moe.smoothie.androidide.themestore

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import moe.smoothie.androidide.themestore.ui.ThemeActivityTopBar
import moe.smoothie.androidide.themestore.ui.ThemeDescription
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme
import moe.smoothie.androidide.themestore.util.getSerializableExtraApiDependent
import moe.smoothie.androidide.themestore.viewmodels.ThemeActivityViewModel
import okhttp3.OkHttpClient
import javax.inject.Inject

enum class StoreType(
    @StringRes val storeName: Int,
    @DrawableRes val storeIcon: Int
) {
    JETBRAINS(
        storeName = R.string.store_name_jetbrains,
        storeIcon = R.drawable.jetbrains_marketplace_icon
    ),
    MICROSOFT(
        storeName = R.string.store_name_microsoft,
        storeIcon = R.drawable.microsoft_store_icon
    )
}

data class ThemeState(
    val iconUrl: String,
    val name: String,
    val description: String,
    val publisherName: String,
    val publisherDomain: String? = null,
    val publisherVerified: Boolean = false
)

@AndroidEntryPoint
class ThemeActivity : ComponentActivity() {
    companion object {
        const val EXTRA_STORE_TYPE: String = "STORE_TYPE"
        const val EXTRA_ICON_URL: String = "ICON_URL"
        const val EXTRA_THEME_URL: String = "THEME_URL"
    }

    val tag = "ThemeActivity"
    val viewModel: ThemeActivityViewModel by viewModels()

    @Inject
    lateinit var httpClient: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val store = intent.getSerializableExtraApiDependent(
            name = EXTRA_STORE_TYPE,
            clazz = StoreType::class.java
        )

        if (store == null) {
            Log.e(tag, "No store type passed in the intent")
            finish()
            return
        }

        setContent {
            AndroidIDEThemesTheme {
                val scrollState = rememberScrollState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        ThemeActivityTopBar(
                            storeName = stringResource(store.storeName),
                            storeIcon = painterResource(store.storeIcon),
                            scrolled = scrollState.value != 0,
                            backButtonCallback = { this.finish() }
                        )
                    }
                ) { innerPadding ->

                }
            }
        }
    }
}

@Composable
private fun ThemeView(
    innerPadding: PaddingValues,
    scrollState: ScrollState
) {
    Box(Modifier.padding(innerPadding)) {
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            repeat(30) {
                Text("Something $it", Modifier.padding(10.dp))
            }
        }
    }
}
