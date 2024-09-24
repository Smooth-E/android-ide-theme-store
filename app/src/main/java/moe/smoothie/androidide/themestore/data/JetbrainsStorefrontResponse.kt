package moe.smoothie.androidide.themestore.data

import kotlinx.serialization.Serializable

// TODO: Some fields are required, others - not. Mark only optional fields with their default values

@Serializable
data class JetbrainsStorefrontResponse(
    val plugins: List<Plugin>,
    val total: Int,
    val correctedQuery: String
) {
    @Serializable
    data class Plugin(
        val id: Int = -1,
        val xmlId: String = "",
        val link: String = "",
        val name: String = "",
        val preview: String = "",
        val downloads: Long= 0,
        val pricingModel: String = "",
        val icon: String = "",
        val previewImage: String = "",
        val cdate: Long = 0,
        val rating: Float = 0f,
        val hasSource: Boolean = false,
        val tags: List<String> = emptyList(),
        val vendor: Vendor = Vendor(),
    ) {
        @Serializable
        data class Vendor(
            val name: String = "",
            val isVerified: Boolean = false
        )
    }
}
