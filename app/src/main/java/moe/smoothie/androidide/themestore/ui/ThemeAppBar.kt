package moe.smoothie.androidide.themestore.ui

import android.animation.ArgbEvaluator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import moe.smoothie.androidide.themestore.R
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme
import moe.smoothie.androidide.themestore.util.formatNumber
import moe.smoothie.androidide.themestore.util.toDp
import moe.smoothie.androidide.themestore.util.toPx

data class ThemeAppBarState(
    val iconUrl: String,
    val themeName: String,
    val rating: Float,
    val downloads: Long,
    val author: String,
    val description: String,
    val domain: String? = null,
    val isVerified: Boolean = false,
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ThemeAppBar(
    state: ThemeAppBarState,
    scrollOffset: Int,
    bakButtonCallback: () -> Unit = { },
    downloadButtonCallback: () -> Unit = { },
    openStoreButtonCallback: () -> Unit = { }
) {
    var contentHeight by remember { mutableIntStateOf(0) }
    val expandedHeightPx by remember { mutableIntStateOf(0) }

    val downloadsTextStyle = MaterialTheme.typography.bodyMedium

    val collapsedHeightPx = TopAppBarDefaults.MediumAppBarCollapsedHeight.toPx()

    val progress = (actualHeightPx - collapsedHeightPx) / (expandedHeightPx - collapsedHeightPx)

    val backgroundColor = Color(
        ArgbEvaluator().evaluate(
            progress,
            MaterialTheme.colorScheme.surfaceContainer.toArgb(),
            MaterialTheme.colorScheme.surface.toArgb()
        ) as Int
    )

    Box(
        modifier = Modifier
            .background(color = backgroundColor)
            .clip(RectangleShape)
    ) {
        Column(modifier = Modifier.statusBarsPadding()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                IconButton(onClick = { bakButtonCallback() }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_24),
                        contentDescription = stringResource(R.string.content_description_go_back)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.jetbrains_marketplace_icon),
                        contentDescription = null
                    )
                    Text(
                        text = "JetBrains Marketplace", // TODO: Pass store name into the composable
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(Modifier.weight(1f))
                AnimatedVisibility(
                    visible = progress == 0f,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(onClick = { openStoreButtonCallback() } ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.baseline_open_in_new_24),
                            contentDescription = stringResource(R.string.action_download_theme)
                        )
                    }
                }
            } // Top part

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(actualHeightDp)
            ) {
                val iconSizeExpanded = 64.dp
                val iconSizeCollapsed = 24.dp

                // Expanded content
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer(clip = false)
                        .onGloballyPositioned { contentHeight = it.size.height }
                ) {
                    Spacer(Modifier.width(16.dp))
                    AsyncImage(
                        model = if (state.iconUrl.lowercase().endsWith(".svg"))
                            ImageRequest.Builder(LocalContext.current)
                                .data(state.iconUrl)
                                .decoderFactory(SvgDecoder.Factory())
                                .build()
                        else
                            state.iconUrl,
                        modifier = Modifier.size(iconSizeExpanded),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val downloadCounterTextStyle = MaterialTheme.typography.bodySmall
                        val downloadCounterHeight = downloadCounterTextStyle.lineHeight.toDp()

                        Text(
                            text = state.themeName,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RatingRow(
                                modifier = Modifier.height(downloadCounterHeight),
                                rating = state.rating
                            )
                            Spacer(Modifier.weight(1f))
                            Icon(
                                modifier = Modifier.size(downloadCounterHeight),
                                painter = painterResource(R.drawable.baseline_download_24),
                                contentDescription = null // TODO: Set content description as "Downloads"
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = formatNumber(state.downloads),
                                style = downloadsTextStyle
                            )
                        }

                        Text(
                            text = state.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

private val mockState = ThemeAppBarState(
    iconUrl = "https://example.com",
    themeName = "Material Icons Theme, but it has a longer name",
    rating = 3.5f,
    downloads = 3_456_789,
    author = "John Doe",
    description = "A mock theme description for preview purposes",
    domain = "https://microsoft.com",
    isVerified = true
)

@Preview
@Composable
private fun CollapsedPreview() {
    AndroidIDEThemesTheme {
        ThemeAppBar(
            state = mockState,
            scrollOffset = 1000000
        )
    }
}

@Preview
@Composable
private fun HalfwayPreview() {
    AndroidIDEThemesTheme {
        ThemeAppBar(
            state = mockState,
            scrollOffset = 100
        )
    }
}

@Preview
@Composable
private fun ExpandedPreview() {
    AndroidIDEThemesTheme {
        ThemeAppBar(
            state = mockState,
            scrollOffset = 0
        )
    }
}
