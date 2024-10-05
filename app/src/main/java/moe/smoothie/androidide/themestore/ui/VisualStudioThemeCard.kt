package moe.smoothie.androidide.themestore.ui

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import moe.smoothie.androidide.themestore.R
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme

data class VisualStudioThemeCardState(
    val iconUrl: String,
    val name: String,
    val developerName: String,
    val developerWebsite: String?,
    val developerWebsiteVerified: Boolean,
    val downloads: Long,
    val description: String,
    val rating: Float
)

@Composable
fun VisualStudioThemeCard(state: VisualStudioThemeCardState) {
    val spacing = 8.dp

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
                AsyncImage(
                    model = state.iconUrl,
                    modifier = Modifier.size(64.dp),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = state.name,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            val textStyle = MaterialTheme.typography.bodySmall
            val lineHeightDp = with(LocalDensity.current) { textStyle.lineHeight.toDp() }

            Column(
                modifier = Modifier.height(lineHeightDp * 2)
            ) {
                Text(
                    modifier = Modifier.basicMarquee(),
                    text = state.developerName,
                    style = textStyle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    state.developerWebsite?.let {
                        Text(
                            text = state.developerWebsite,
                            style = textStyle,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                        if (state.developerWebsiteVerified) {
                            Icon(
                                modifier = Modifier
                                    .size(lineHeightDp)
                                    .padding(2.dp),
                                painter = painterResource(R.drawable.baseline_verified_24),
                                contentDescription = stringResource(R.string.content_description_publisher_verified),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Text(
                text = state.description,
                maxLines = 3,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
            )
            Row {
                for (i in 1..5) {
                    val painter =
                        if (i <= state.rating)
                            painterResource(R.drawable.baseline_grade_24)
                        else painterResource(
                            R.drawable.outline_grade_24
                        )
                    Icon(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.size(lineHeightDp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
internal fun VisualStudioThemeCardPreview() {
    AndroidIDEThemesTheme {
        Box(modifier = Modifier.width(200.dp)) {
            VisualStudioThemeCard(
                VisualStudioThemeCardState(
                    iconUrl = "https://example.com/exampple.png",
                    name = "Material Icon Theme",
                    developerName = "Microsoft",
                    developerWebsite = "microsoft.com",
                    developerWebsiteVerified = true,
                    downloads = 19_300_000,
                    description = "Material Design icons for Visual Studio Code, and here is some other description because why not",
                    rating = 4.5f
                )
            )
        }
    }
}
