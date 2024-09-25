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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import moe.smoothie.androidide.themestore.R
import moe.smoothie.androidide.themestore.data.JetbrainsStorefrontResponse
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme
import moe.smoothie.androidide.themestore.util.formatNumber

@Composable
fun JetbrainsThemeCard(state: JetbrainsStorefrontResponse.Plugin) {
    val spacing = 8.dp
    val downloadUrl = "https://downloads.marketplace.jetbrains.com"
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
                    model = downloadUrl + state.previewImage,
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
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing * 0),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {

                        for (i in 0..5) {
                            Icon(
                                painter = if (state.rating >= i) filledStar else outlinedStar,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Row {
                        Icon(
                            painter = painterResource(R.drawable.baseline_download_24),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formatNumber(state.downloads),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .height(24.dp)
                                .wrapContentHeight(Alignment.CenterVertically)
                                .padding(start = spacing / 2)
                        )
                    }
                }
                Text(
                    text = state.preview,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
fun JetbrainsThemeCardPreview() {
    AndroidIDEThemesTheme {
        Box(modifier = Modifier.width(300.dp)) {
            JetbrainsThemeCard(

                JetbrainsStorefrontResponse.Plugin(
                    id = 11938,
                    xmlId = "com.markskelton.one-dark-theme",
                    link = "/plugin/11938-one-dark-theme",
                    name = "One Dark Theme",
                    preview = "One Dark theme for JetBrains. Do you need help? Please check the docs FAQs to see if we can solve your problem. If that does not fix your problem, please submit an...",
                    downloads = 7971894,
                    pricingModel = "FREE",
                    icon = "/files/11938/605820/icon/pluginIcon.svg",
                    previewImage = "/files/11938/preview_19494.png",
                    cdate = 1726780936000,
                    rating = 4.8f,
                    hasSource = true,
                    tags = listOf("Theme", "User Interface"),
                    vendor = JetbrainsStorefrontResponse.Plugin.Vendor(
                        name = "Mark Skelton",
                        isVerified = false
                    )
                )
            )
        }
    }
}
