package moe.smoothie.androidide.themestore.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    icon: Painter?,
    header: String? = null,
    message: String? = null,
    buttonIcon: Painter? = null,
    buttonText: String? = null,
    onClick: () -> Unit = { },
) {
    ElevatedCard {
        Column(
            modifier = modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            icon?.let {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            header?.let {
                Text(
                    text = header,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
            message?.let {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
            FilledTonalButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    buttonIcon?.let {
                        Icon(buttonIcon, contentDescription = null)
                    }
                    buttonText?.let {
                        Text(buttonText)
                    }
                }
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
            icon = painterResource(R.drawable.baseline_warning_24),
            header = "An error has occurred",
            message = "And this error has some long description which spans several lines because it is indeed pretty long",
            buttonText = "Reload and try again",
            buttonIcon = painterResource(R.drawable.baseline_settings_24)
        )
    }
}
