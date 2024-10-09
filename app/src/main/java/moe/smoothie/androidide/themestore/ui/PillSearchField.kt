package moe.smoothie.androidide.themestore.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import moe.smoothie.androidide.themestore.R
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme

@Composable
fun PillSearchField(
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit = {  }
) {
    var state by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(200.dp)
            )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(40.dp).padding(8.dp),
                painter = painterResource(R.drawable.baseline_search_24),
                contentDescription = null
            )

            val textStyle = MaterialTheme.typography.bodyMedium
            val lineHeight = with(LocalDensity.current) {
                textStyle.lineHeight.toDp()
            }

            Box(Modifier.weight(1f)) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = state.isEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        stringResource(R.string.message_search_bar_placeholder),
                        maxLines = 1,
                        modifier = Modifier.height(lineHeight),
                        style = textStyle.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    )
                }
                BasicTextField(
                    value = state,
                    onValueChange = {
                        state = it
                        onValueChanged(it)
                    },
                    maxLines = 1,
                    modifier = Modifier.height(lineHeight),
                    textStyle = textStyle,
                )
            }
            AnimatedVisibility(
                visible = state.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(
                    onClick = { state = "" }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_close_24),
                        contentDescription = stringResource(R.string.content_description_clear_search)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
internal fun PillSearchBarPreview() {
    AndroidIDEThemesTheme {
        PillSearchField(
            modifier = Modifier.width(300.dp)
        )
    }
}
