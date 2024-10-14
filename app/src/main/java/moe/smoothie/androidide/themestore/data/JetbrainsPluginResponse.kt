package moe.smoothie.androidide.themestore.data
import kotlinx.serialization.Serializable

@Serializable
data class JetbrainsPluginResponse(
    val id: Int,
    val name: String,
    val link: String,
    val approve: Boolean,
    val xmlId: String,
    val description: String,
    val customIdeList: Boolean,
    val preview: String,
    val email: String,
    val cdate: Long,
    val family: String,
    val copyright: String,
    val downloads: Int,
    val vendor: Vendor,
    val urls: Urls,
    val tags: List<Tag>,
    val hasUnapprovedUpdate: Boolean,
    val pricingModel: String,
    val screens: List<Screen>,
    val themes: List<Theme>,
    val icon: String,
    val semverOnly: Boolean,
    val isHidden: Boolean,
    val isMonetizationAvailable: Boolean,
    val isBlocked: Boolean,
    val isModificationAllowed: Boolean
) {
    @Serializable
    data class Vendor(
        val type: String,
        val id: Int,
        val name: String,
        val url: String,
        val link: String,
        val publicName: String,
        val email: String,
        val countryCode: String,
        val country: String,
        val isVerified: Boolean,
        val isTrader: Boolean,
        val description: String
    )

    @Serializable
    data class Urls(
        val url: String,
        val forumUrl: String,
        val licenseUrl: String,
        val privacyPolicyUrl: String,
        val bugtrackerUrl: String,
        val docUrl: String,
        val sourceCodeUrl: String,
        val customContacts: List<CustomContact>
    ) {
        @Serializable
        data class CustomContact(
            val title: String,
            val link: String
        )
    }

    @Serializable
    data class Tag(
        val id: Int,
        val name: String,
        val privileged: Boolean,
        val searchable: Boolean,
        val link: String
    )

    @Serializable
    data class Screen(
        val url: String
    )

    @Serializable
    data class Theme(
        val name: String,
        val dark: Boolean
    )
}
