package moe.smoothie.androidide.themestore.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import moe.smoothie.androidide.themestore.R
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme
import moe.smoothie.androidide.themestore.util.formatNumber

data class JetbrainsThemeCardState(
    val previewUrl: String,
    val name: String,
    val rating: Float,
    val downloads: Long,
    val trimmedDescription: String
)

@Composable
fun JetbrainsThemeCard(state: JetbrainsThemeCardState) {
    val spacing = 8.dp
    val outlinedStar = painterResource(R.drawable.outline_grade_24)
    val filledStar = painterResource(R.drawable.baseline_grade_24)

    OutlinedCard(
        onClick = { },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {
                SubcomposeAsyncImage(
                    model = state.previewUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .height(maxWidth * 480f / 768f)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier.padding(
                    top = spacing,
                    start = spacing * 2,
                    end = spacing * 2,
                    bottom = spacing * 2
                )
            ) {
                Text(
                    text = state.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing / 2),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(modifier = Modifier.align(Alignment.CenterVertically)) {
                        for (i in 0..5) {
                            Icon(
                                painter = if (state.rating >= i) filledStar else outlinedStar,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Row {
                        Icon(
                            painter = painterResource(R.drawable.baseline_download_24),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp).align(Alignment.CenterVertically),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formatNumber(state.downloads),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .wrapContentHeight(Alignment.CenterVertically)
                                .align(Alignment.CenterVertically)
                                .padding(start = spacing / 2)
                        )
                    }
                }
                Text(
                    text = state.trimmedDescription,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing / 2),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
fun JetbrainsThemeCardPreview(themeName: String = "One Dark Pro Theme") {
    AndroidIDEThemesTheme {
        Box(modifier = Modifier.width(300.dp)) {
            JetbrainsThemeCard(
                JetbrainsThemeCardState(
                previewUrl = "https://example.com", // Images do not load in previews
                name = themeName,
                rating = 4.3f,
                downloads = 1_234_567,
                trimmedDescription = "One Dark theme for JetBrains. Do you need help? Please check the docs FAQs to see if we can solve your problem. If that does not fix your problem, please submit an..."
            )
            )
        }
    }
}

@Preview
@Composable
fun LongThemeNamePreview() =
    JetbrainsThemeCardPreview("A very very very long name of a theme, yeah")
