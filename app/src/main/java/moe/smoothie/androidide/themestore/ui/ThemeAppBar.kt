package moe.smoothie.androidide.themestore.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import moe.smoothie.androidide.themestore.R
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeActivityTopBar(
    storeName: String,
    storeIcon: Painter,
    scrolled: Boolean,
    backButtonCallback: () -> Unit = { },
    openStoreButtonCallback: () -> Unit = { }
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (scrolled)
            MaterialTheme.colorScheme.surfaceContainer
        else
            MaterialTheme.colorScheme.surface,
        label = "top_bar_background"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
    ) {
        Box(Modifier.statusBarsPadding()) {
            Row(
                modifier = Modifier
                    .height(TopAppBarDefaults.MediumAppBarCollapsedHeight),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { backButtonCallback() }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_24),
                        contentDescription = stringResource(R.string.content_description_go_back)
                    )
                }
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = storeIcon,
                    contentDescription = null
                )
                Text(
                    text = storeName,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { openStoreButtonCallback() }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_open_in_new_24),
                        contentDescription = stringResource(R.string.action_open_store)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ThemeActivityTopBarPreview() {
    AndroidIDEThemesTheme {
        ThemeActivityTopBar(
            storeIcon = painterResource(R.drawable.jetbrains_marketplace_icon),
            storeName = "JetBrains Marketplace",
            scrolled = false
        )
    }
}
