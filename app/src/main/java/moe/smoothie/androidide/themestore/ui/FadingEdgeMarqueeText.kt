package moe.smoothie.androidide.themestore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun FadingEdgeMarqueeText(
    modifier: Modifier = Modifier,
    text: String = "",
    style: TextStyle = LocalTextStyle.current,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    gradientColors: List<Color> = listOf(
        MaterialTheme.colorScheme.surface,
        Color.Transparent,
        Color.Transparent,
        Color.Transparent,
        Color.Transparent,
        MaterialTheme.colorScheme.surface,
    )
) {
    var boxWidth by remember { mutableIntStateOf(0) }
    var textWidth by remember { mutableIntStateOf(0) }

    Box(modifier.onGloballyPositioned { boxWidth = it.size.width }) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee()
                .clip(RectangleShape)
                .onGloballyPositioned { textWidth = it.size.width },
            text = text,
            style = style,
            textAlign = textAlign,
            maxLines = maxLines,
        )

        if (boxWidth < textWidth) {
            Box(
                Modifier
                    .matchParentSize()
                    .graphicsLayer { clip = true }
                    .background(Brush.horizontalGradient(gradientColors))
            )
        }
    }
}
