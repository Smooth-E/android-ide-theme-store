package moe.smoothie.androidide.themestore.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import moe.smoothie.androidide.themestore.R
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme

@Composable
fun PillSearchField(
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit = {  },
    elevation: Dp = 0.dp,
    initialQuery: String = ""
) {
    var state by remember { mutableStateOf(initialQuery) }

    Box(
        modifier = modifier
            .shadow(elevation)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(200.dp)
            )
    ) {
        var rowHeight by remember { mutableIntStateOf(0) }

        Row(
            modifier = Modifier.onGloballyPositioned { rowHeight = it.size.height },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.padding(16.dp).size(24.dp),
                painter = painterResource(R.drawable.baseline_search_24),
                contentDescription = null
            )

            val textStyle = MaterialTheme.typography.bodyMedium
            val height = with(LocalDensity.current) { rowHeight.toDp() }

            Box(Modifier.weight(1f)) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = state.isEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        stringResource(R.string.message_search_bar_placeholder),
                        maxLines = 1,
                        modifier = Modifier
                            .height(height)
                            .wrapContentHeight()
                            .fillMaxWidth(),
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
                    modifier = Modifier
                        .height(height)
                        .wrapContentHeight()
                        .fillMaxWidth(),
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

@Preview
@Composable
internal fun PillSearchBarWithQueryPreview() {
    AndroidIDEThemesTheme {
        PillSearchField(
            modifier = Modifier.width(300.dp),
            initialQuery = "material icons theme"
        )
    }
}
