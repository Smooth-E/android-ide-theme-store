package moe.smoothie.androidide.themestore.ui

import androidx.collection.mutableIntIntMapOf
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.isTraceInProgress
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import coil.compose.SubcomposeAsyncImage
import moe.smoothie.androidide.themestore.R
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme
import moe.smoothie.androidide.themestore.util.formatNumber

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

    val expandedHeightPx = with(LocalDensity.current) {
        headerTextStyle.lineHeight.toPx() +
                downloadsTextStyle.lineHeight.toPx() +
                descriptionTextStyle.lineHeight.toPx() +
                developerTextStyle.lineHeight.toPx() +
                spacing.toPx() * 5
    }

    val expandedHeightDp = with(LocalDensity.current) { expandedHeightPx.toDp() }

    val collapsedHeightPx = with(LocalDensity.current) {
        TopAppBarDefaults.MediumAppBarCollapsedHeight.toPx()
    }

    val collapsedHeightDp = with(LocalDensity.current) { collapsedHeightPx.toDp() }

    val actualHeightPx =
        (expandedHeightPx - scrollOffset).coerceIn(collapsedHeightPx, expandedHeightPx)

    val actualHeightDp = with(LocalDensity.current) { actualHeightPx.toDp() }

    val progress = actualHeightPx / expandedHeightPx - collapsedHeightPx / expandedHeightPx

    Box(Modifier.background(color = MaterialTheme.colorScheme.surfaceContainer)) {
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .height(actualHeightDp)
                .background(color = MaterialTheme.colorScheme.surfaceContainer),
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .offset(0.dp, (expandedHeightDp - actualHeightDp) / 2)
                    .requiredHeight(expandedHeightDp),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                val iconSize = lerp(collapsedHeightDp, expandedHeightDp, progress) - 16.dp

                SubcomposeAsyncImage(
                    modifier = Modifier
                        .size(iconSize)
                        .offset(0.dp, (actualHeightDp - iconSize) / 2)
                        .clip(CircleShape),
                    contentDescription = null,
                    model = state.iconUrl,
                    contentScale = ContentScale.Fit
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val headerLineHeightDp = with(LocalDensity.current) {
                        headerTextStyle.lineHeight.toDp()
                    }

                    val headerVerticalOffset = with(LocalDensity.current) {
                        val headerLineHeightPx = headerTextStyle.lineHeight.toPx()
                        ((1f - progress) / 2f * (collapsedHeightPx - headerLineHeightPx)).toInt()
                    }

                    val installButtonWidthDp =
                        with(LocalDensity.current) { installButtonWidth.toDp() }

                    Box {
                        FadingEdgeMarqueeText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(lerp(collapsedHeightDp, headerLineHeightDp, progress))
                                .padding(PaddingValues(end = installButtonWidthDp + spacing))
                                .offset { IntOffset(0, headerVerticalOffset) },
                            text = state.themeName,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            gradientColors = listOf(
                                MaterialTheme.colorScheme.surfaceContainer,
                                Color.Transparent,
                                Color.Transparent,
                                Color.Transparent,
                                Color.Transparent,
                                MaterialTheme.colorScheme.surfaceContainer
                            )
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
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
                                    onClick = { }
                                ) {
                                    Text("Install")
                                }
                            }
                        }
                    }

                    val downloadCounterHeight = with(LocalDensity.current) {
                        downloadsTextStyle.lineHeight.toDp()
                    }

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
        }
    }
}

private val mockState = ThemeAppBarState(
    iconUrl = "https://example.com",
    themeName =  "Mtaerial Icons Theme, but it has a longer name",
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
