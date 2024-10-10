package moe.smoothie.androidide.themestore.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import moe.smoothie.androidide.themestore.R
import moe.smoothie.androidide.themestore.ui.theme.AndroidIDEThemesTheme

@Composable
fun RatingRow(
    modifier: Modifier = Modifier,
    rating: Float = 0f,
) {
    Row(modifier = modifier) {
        for (i in 1..5) {
            val painter =
                if (i <= rating)
                    painterResource(R.drawable.baseline_grade_24)
                else painterResource(
                    R.drawable.outline_grade_24
                )
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

@Preview
@Composable
internal fun RatingRowPreview() {
    AndroidIDEThemesTheme {
        RatingRow()
    }
}