package moe.smoothie.androidide.themestore.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
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
import moe.smoothie.androidide.themestore.util.formatNumber

data class MicrosoftStoreCardState(
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
fun MicrosoftStoreCard(state: MicrosoftStoreCardState) {
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
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
                FadingEdgeMarqueeText(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    textAlign = TextAlign.Center
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

            RatingRow(
                modifier = Modifier.height(lineHeightDp),
                rating = state.rating
            )

            Row(modifier = Modifier.height(lineHeightDp)) {
                Icon(
                    painter = painterResource(R.drawable.baseline_download_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatNumber(state.downloads),
                    style = textStyle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            val descriptionTypography = MaterialTheme.typography.bodyMedium
            val descriptionHeight = with(LocalDensity.current) {
                descriptionTypography.lineHeight.toDp() * 3
            }
            Text(
                modifier = Modifier.height(descriptionHeight),
                text = state.description,
                maxLines = 3,
                style = descriptionTypography,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
internal fun Preview(
    name: String = "Material Icon Theme",
    description: String = "Material Design icons for Visual Studio Code, and here is some other description because why not"
) {
    AndroidIDEThemesTheme {
        Box(modifier = Modifier.width(150.dp)) {
            MicrosoftStoreCard(
                MicrosoftStoreCardState(
                    iconUrl = "https://example.com/exampple.png",
                    name = name,
                    developerName = "Microsoft",
                    developerWebsite = "microsoft.com",
                    developerWebsiteVerified = true,
                    downloads = 19_300_000,
                    description = description,
                    rating = 4.5f
                )
            )
        }
    }
}

@Preview
@Composable
internal fun ShortValuesPreview() = Preview(
    name = "Darcula",
    description = "A pretty short description"
)
