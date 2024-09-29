package moe.smoothie.androidide.themestore.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class NavigationBarRoute(
    @StringRes val nameResource: Int,
    @DrawableRes val iconResource: Int,
    val route: String
)
