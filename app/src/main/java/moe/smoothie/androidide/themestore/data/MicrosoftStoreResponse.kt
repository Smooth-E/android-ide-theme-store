package moe.smoothie.androidide.themestore.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class MicrosoftStoreResponse(
    val results: List<Result>
) {
    @Serializable
    data class Result(
        val extensions: List<Extension>,
        val pagingToken: String?,
        val resultMetadata: List<ResultMetadata>
    ) {
        @Serializable
        data class Extension(
            val publisher: Publisher,
            val extensionId: String,
            val extensionName: String,
            val displayName: String,
            val flags: String,
            val lastUpdated: String,
            val releaseDate: String,
            val shortDescription: String,
            val versions: List<Version>,
            val categories: List<String>,
            val tags: List<String>,
            val statistics: List<Statistic>,
            val installationTargets: List<InstallationTarget>,
            val deploymentType: Int
        ) {
            @Serializable
            data class Publisher(
                val publisherId: String,
                val publisherName: String,
                val displayName: String,
                val flags: String,
                val domain: String,
                val isDomainVerified: Boolean
            )

            @Serializable
            data class Version(
                val version: String,
                val flags: String,
                val lastUpdated: String,
                val files: List<ExtensionFile>,
                val properties: List<ExtensionProperty>,
                val assetUri: String,
                val fallbackAssetUri: String
            ) {
                @Serializable
                data class ExtensionFile(
                    val assetType: String,
                    val source: String
                )

                @Serializable
                data class ExtensionProperty(
                    val key: String,
                    val value: String
                )
            }

            @Serializable
            data class Statistic(
                val statisticName: String,
                @Contextual val value: Any
            )

            @Serializable
            data class InstallationTarget(
                val target: String,
                val targetVersion: String
            )
        }

        @Serializable
        data class ResultMetadata(
            val metadataType: String,
            val metadataItems: List<ResultMetadataItem>
        ) {
            @Serializable
            data class ResultMetadataItem(
                val name: String,
                val count: Int
            )
        }
    }
}
