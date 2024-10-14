package moe.smoothie.androidide.themestore.util

fun formatDomain(domain: String) = domain
    .removePrefix("https://")
    .removePrefix("http://")
    .removeSuffix("/")
