package moe.smoothie.androidide.themestore.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat.ThemeCompat
import coil.compose.AsyncImage
import moe.smoothie.androidide.themestore.R
import moe.smoothie.androidide.themestore.data.JetbrainsStorefrontResponse
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme

data class JetbrainsThemeCardState(
    val data: JetbrainsStorefrontResponse.Plugin,
    var previewBitmap: Bitmap? = null,
    var iconBitmap: Bitmap? = null
) {
    private val tag = "JetbrainsThemeCardState"

    init {
        Log.d(tag, "Created card state with ${previewBitmap == null} and ${iconBitmap == null}")
    }
}

@Composable
fun JetbrainsThemeCard(state: JetbrainsThemeCardState) {
    var preview: ImageBitmap? = null
    if (state.previewBitmap != null) {
        preview = state.iconBitmap?.asImageBitmap()
    }

    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {
//                Image(
//                    bitmap = preview ?: ImageBitmap.imageResource(R.drawable.mock_theme_preview),
//                    contentDescription = null,
//                    modifier = Modifier.height(maxWidth * 9f / 16f).fillMaxWidth(),
//                    contentScale = ContentScale.Crop
//                )
                AsyncImage(
                    model = "https://downloads.marketplace.jetbrains.com" + state.data.previewImage,
                    contentDescription = null,
                    modifier = Modifier.height(maxWidth * 9f / 16f).fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = (state.previewBitmap == null).toString(),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Preview
@Composable
fun JetbrainsThemeCardPreview() {
    AndroidIDEThemesTheme {
        Box(modifier = Modifier.width(120.dp)) {
            JetbrainsThemeCard(
                JetbrainsThemeCardState(
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
                    ),
                    ImageBitmap.imageResource(R.drawable.mock_theme_preview).asAndroidBitmap(),
                    ImageBitmap.imageResource(R.drawable.mock_theme_icon).asAndroidBitmap()
                )
            )
        }
    }
}
