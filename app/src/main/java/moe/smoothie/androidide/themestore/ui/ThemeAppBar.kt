package moe.smoothie.androidide.themestore.ui

import android.animation.ArgbEvaluator
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
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
    scrollOffset: Int
) {
    var installButtonWidth by remember { mutableIntStateOf(0) }

    val headerTextStyle = MaterialTheme.typography.titleLarge
    val downloadsTextStyle = MaterialTheme.typography.bodyMedium
    val descriptionTextStyle = MaterialTheme.typography.bodyMedium
    val developerTextStyle = MaterialTheme.typography.bodyMedium
    val spacing = 8.dp

    val expandedHeightPx =
        headerTextStyle.lineHeight.toPx() +
                downloadsTextStyle.lineHeight.toPx() +
                descriptionTextStyle.lineHeight.toPx() +
                developerTextStyle.lineHeight.toPx() +
                spacing.toPx() * 5

    val expandedHeightDp = expandedHeightPx.toDp()
    val collapsedHeightPx = TopAppBarDefaults.MediumAppBarCollapsedHeight.toPx()
    val collapsedHeightDp = collapsedHeightPx.toDp()

    val actualHeightPx = (expandedHeightPx - scrollOffset)
        .coerceIn(collapsedHeightPx, expandedHeightPx)

    val actualHeightDp = actualHeightPx.toDp()

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
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .height(actualHeightDp),
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .offset(0.dp, (expandedHeightDp - actualHeightDp) / 2)
                    .requiredHeight(expandedHeightDp),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                val iconSize = lerp(collapsedHeightDp, expandedHeightDp, progress) - spacing * 2

                SubcomposeAsyncImage(
                    modifier = Modifier
                        .size(iconSize)
                        .offset(0.dp, (actualHeightDp - iconSize) / 2)
                        .clip(CircleShape),
                    contentDescription = null,
                    model = if (state.iconUrl.lowercase().endsWith(".svg"))
                        ImageRequest.Builder(LocalContext.current)
                            .data(state.iconUrl)
                            .decoderFactory(SvgDecoder.Factory())
                            .build()
                    else
                        state.iconUrl,
                    contentScale = ContentScale.Fit
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    val headerVerticalOffset =
                        ((1f - progress) / 2f * (collapsedHeightPx - headerTextStyle.lineHeight.toPx()))
                    FadingEdgeMarqueeText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                lerp(
                                    collapsedHeightDp,
                                    headerTextStyle.lineHeight.toDp(),
                                    progress
                                )
                            )
                            .padding(
                                PaddingValues(
                                    end = installButtonWidth.toDp() * (1f - progress) + spacing
                                )
                            )
                            .offset(0.dp, headerVerticalOffset.toDp()),
                        text = state.themeName,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        gradientColors = listOf(
                            backgroundColor,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            backgroundColor
                        )
                    )

                    val downloadCounterHeight = downloadsTextStyle.lineHeight.toDp()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(downloadCounterHeight)
                            .alpha(progress),
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween
                    ) {
                        RatingRow(
                            modifier = Modifier.height(downloadCounterHeight),
                            rating = 3.5f
                        )
                        Row(
                            modifier = Modifier.fillMaxHeight(),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_download_24),
                                contentDescription = null
                            )
                            Text(
                                text = formatNumber(state.downloads),
                                style = downloadsTextStyle
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .height(collapsedHeightDp)
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = progress == 0f,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Button(
                        modifier = Modifier
                            .onGloballyPositioned {
                                installButtonWidth = it.size.width
                            },
                        onClick = { },
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                painter = painterResource(R.drawable.phosphor_download_simple),
                                contentDescription = null
                            )
                            Text("Install")
                        }
                    }
                }
            }
        }
    }
}

private val mockState = ThemeAppBarState(
    iconUrl = "https://example.com",
    themeName = "Mtaerial Icons Theme, but it has a longer name",
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
