package moe.smoothie.androidide.themestore.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import moe.smoothie.androidide.themestore.R
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme
import moe.smoothie.androidide.themestore.util.toDp

private val iconButtonSize = 40.dp

@Composable
fun pillSearchFieldHeight() = max(pillSearchFieldTextStyle().lineHeight.toDp(), iconButtonSize)

@Composable
fun PillSearchField(
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit = { },
    elevation: Dp = 0.dp,
    initialQuery: String = ""
) {
    val tag = "PillSearchFieldSearchField"

    var state by remember { mutableStateOf(initialQuery) }
    var focused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val focusManager = LocalFocusManager.current
    Box(
        modifier = modifier
            .shadow(elevation)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(200.dp)
            )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .size(24.dp),
                painter = painterResource(R.drawable.baseline_search_24),
                contentDescription = null
            )

            Box(Modifier.weight(1f)) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = state.isEmpty(), enter = fadeIn(), exit = fadeOut()
                ) {
                    Text(
                        stringResource(R.string.message_search_bar_placeholder),
                        maxLines = 1,
                        modifier = Modifier
                            .height(pillSearchFieldHeight())
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        style = pillSearchFieldTextStyle()
                            .copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
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
                        .height(pillSearchFieldHeight())
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            focused = it.isFocused
                            Log.d(tag, "Focus changed: $it")
                        },
                    textStyle = pillSearchFieldTextStyle(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search, autoCorrectEnabled = false
                    ),
                    keyboardActions = KeyboardActions(onSearch = {
                        Log.d(tag, "Received the \"Search\" action, freeing focus")
                        focusManager.clearFocus()
                    })
                )
            }
            AnimatedVisibility(
                visible = state.isNotEmpty(), enter = fadeIn(), exit = fadeOut()
            ) {
                IconButton(onClick = {
                    state = ""
                    onValueChanged(state)

                    if (!focused) {
                        focusRequester.requestFocus()
                    }
                }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_close_24),
                        contentDescription = stringResource(R.string.content_description_clear_search)
                    )
                }
            }
        }
    }
}

@Composable
private fun pillSearchFieldTextStyle() = MaterialTheme.typography.bodyMedium

@Preview
@Composable
private fun PillSearchBarPreview() {
    AndroidIDEThemesTheme {
        PillSearchField(
            modifier = Modifier.width(300.dp)
        )
    }
}

@Preview
@Composable
private fun PillSearchBarWithQueryPreview() {
    AndroidIDEThemesTheme {
        PillSearchField(
            modifier = Modifier.width(300.dp),
            initialQuery = "material icons theme"
        )
    }
}
