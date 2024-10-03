package moe.smoothie.androidide.themestore.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import moe.smoothie.androidide.themestore.R
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme

@Composable
fun FooterCard(
    modifier: Modifier = Modifier,
    hero: @Composable () -> Unit = { },
    header: @Composable () -> Unit = { },
    message: @Composable () -> Unit = { },
    buttonContent: @Composable RowScope.() -> Unit = { },
    onClick: () -> Unit = { },
) {
    ElevatedCard {
        Column(
            modifier = modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.secondary
            ) {
                hero()
            }
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.titleMedium.copy(
                    textAlign = TextAlign.Center
                )
            ) {
                header()
            }
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            ) {
                message()
            }
            FilledTonalButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                buttonContent()
            }
        }
    }
}

@Preview
@Composable
fun FooterCardPreview() {
    AndroidIDEThemesTheme {
        FooterCard(
            modifier = Modifier.width(300.dp),
            hero = { Icon(painterResource(R.drawable.baseline_warning_24), contentDescription = null) },
            header = { Text("An error has occurred") },
            message = { Text("And this error has some long description which spans several lines because it is indeed pretty long") },
            buttonContent = {
                Text("Reload and try again")
                Icon(
                    painter = painterResource(R.drawable.baseline_settings_24),
                    contentDescription = null
                )
            }
        )
    }
}
