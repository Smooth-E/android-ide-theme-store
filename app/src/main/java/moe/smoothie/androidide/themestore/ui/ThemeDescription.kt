package moe.smoothie.androidide.themestore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import moe.smoothie.androidide.themestore.R
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme
import moe.smoothie.androidide.themestore.util.formatDomain
import moe.smoothie.androidide.themestore.util.formatNumber
import moe.smoothie.androidide.themestore.util.toDp

@Composable
fun ThemeDescription(
    iconUrl: String,
    name: String,
    description: String,
    publisherName: String,
    downloads: Long,
    rating: Float,
    publisherDomain: String? = null,
    publisherVerified: Boolean = false,
    installButtonCallback: () -> Unit = { }
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                model = if (iconUrl.endsWith(".svg"))
                    ImageRequest.Builder(LocalContext.current)
                        .data(iconUrl)
                        .decoderFactory(SvgDecoder.Factory())
                        .build()
                else
                    iconUrl,
                contentDescription = null
            ) { }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge
                )
                Row {
                    val textStyle = MaterialTheme.typography.bodySmall
                        .copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    val lineHeightDp = textStyle.lineHeight.toDp()

                    Icon(
                        modifier = Modifier.size(lineHeightDp),
                        painter = painterResource(R.drawable.baseline_person_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = publisherName,
                        style = textStyle
                    )
                    if (publisherDomain?.isNotEmpty() == true) {
                        Spacer(Modifier.width(16.dp))
                        Icon(
                            modifier = Modifier.size(lineHeightDp),
                            painter = painterResource(R.drawable.baseline_public_24),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = formatDomain(publisherDomain),
                            style = textStyle
                        )

                        if (publisherVerified) {
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                modifier = Modifier.size(lineHeightDp),
                                painter = painterResource(R.drawable.baseline_verified_24),
                                contentDescription = stringResource(R.string.content_description_publisher_verified),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                Row {
                    val textStyle = MaterialTheme.typography.bodySmall

                    RatingRow(
                        modifier = Modifier.height(textStyle.lineHeight.toDp()),
                        rating = rating
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(
                        modifier = Modifier.size(textStyle.lineHeight.toDp()),
                        painter = painterResource(R.drawable.baseline_download_24),
                        contentDescription = null
                    )
                    Text(
                        text = formatNumber(downloads),
                        style = textStyle
                    )
                }
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Row {
                    Spacer(Modifier.weight(1f))
                    FilledTonalButton(
                        onClick = { installButtonCallback() },
                        content = {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(R.drawable.phosphor_download_simple),
                                contentDescription = null
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.action_download_theme))
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ThemeDescriptionPreview() {
    AndroidIDEThemesTheme {
        ThemeDescription(
            iconUrl = "https://plugins.jetbrains.com/files/11938/605820/icon/pluginIcon.svg",
            name = "Material Icon Theme",
            description = "UI Themes for C/C++ extension.",
            publisherName = "Microsoft",
            publisherDomain = "https://microsoft.com/",
            publisherVerified = true,
            rating = 3.5f,
            downloads = 1_234_567
        )
    }
}
