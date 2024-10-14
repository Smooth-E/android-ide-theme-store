package moe.smoothie.androidide.themestore.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import moe.smoothie.androidide.themestore.R
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme

enum class LoadingStatus {
    LOADING,
    ERROR_PARSING,
    ERROR_RECEIVING,
    NO_NETWORK
}

@Composable
fun StatusView(
    modifier: Modifier = Modifier,
    hero: @Composable () -> Unit,
    header: @Composable () -> Unit,
    description: @Composable () -> Unit,
    reloadCallback: () -> Unit = { },
) {
    Box(modifier = modifier) {
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
                ),
            ) {
                description()
            }
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { reloadCallback() }
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_refresh_24),
                    contentDescription = null
                )
                Text(stringResource(R.string.button_reload))
            }
        }
    }
}

@Composable
fun StatusView(
    modifier: Modifier = Modifier,
    loadingStatus: LoadingStatus,
    reloadCallback: () -> Unit = { }
) {
    val hero: @Composable () -> Unit
    val header: String
    val description: String

    when (loadingStatus) {
        LoadingStatus.LOADING -> {
            hero = { CircularProgressIndicator(Modifier.size(24.dp)) }
            header = stringResource(R.string.header_fetching_items)
            description = stringResource(R.string.message_fetching_items)
        }
        LoadingStatus.ERROR_PARSING -> {
            hero = {
                Icon(
                    painter = painterResource(R.drawable.round_translate_24),
                    contentDescription = null
                )
            }
            header = stringResource(R.string.header_unexpected_response)
            description = stringResource(R.string.message_unexpected_response)
        }
        LoadingStatus.ERROR_RECEIVING -> {
            hero = {
                Icon(
                    painter = painterResource(R.drawable.round_data_object_24),
                    contentDescription = null
                )
            }
            header = stringResource(R.string.header_failure_receiving)
            description = stringResource(R.string.message_failure_receiving)
        }
        LoadingStatus.NO_NETWORK -> {
            hero = {
                Icon(
                    painter = painterResource(R.drawable.round_signal_wifi_off_24),
                    contentDescription = null
                )
            }
            header = stringResource(R.string.header_no_connection)
            description = stringResource(R.string.message_no_connection)
        }
    }

    StatusView(
        modifier = modifier,
        hero = { hero() },
        header = { Text(header) },
        description = { Text(description) },
        reloadCallback = { reloadCallback() }
    )
}

@Preview
@Composable
private fun PreviewLoading() {
    AndroidIDEThemesTheme {
        StatusView(
            modifier = Modifier.fillMaxSize(),
            loadingStatus = LoadingStatus.LOADING
        )
    }
}

@Preview
@Composable
private fun PreviewErrorParsing() {
    AndroidIDEThemesTheme {
        StatusView(
            modifier = Modifier.fillMaxSize(),
            loadingStatus = LoadingStatus.ERROR_PARSING
        )
    }
}

@Preview
@Composable
private fun PreviewErrorReceiving() {
    AndroidIDEThemesTheme {
        StatusView(
            modifier = Modifier.fillMaxSize(),
            loadingStatus = LoadingStatus.ERROR_RECEIVING
        )
    }
}

@Preview
@Composable
private fun PreviewNoNetwork() {
    AndroidIDEThemesTheme {
        StatusView(
            modifier = Modifier.fillMaxSize(),
            loadingStatus = LoadingStatus.NO_NETWORK
        )
    }
}
