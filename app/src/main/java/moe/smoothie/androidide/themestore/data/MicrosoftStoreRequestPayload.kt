package moe.smoothie.androidide.themestore.data

import kotlinx.serialization.Serializable

@Serializable
data class MicrosoftStoreRequestPayload(
    val assetTypes: List<String>,
    val filters: List<Filter>,
    val flags: Int
) {
    @Serializable
    data class Filter(
        val criteria: List<FilterCriteria>,

        // TODO: Determine what direction stays for
        val direction: Int = 2,

        val pageSize: Int = 54,

        /** Start with 1 **/
        val pageNumber: Int,

        // TODO: Define sort order indices. 4 sorts by installs, is default
        val sortBy: Int = 4,
        val sortOrder: Int = 0,

        // TODO: Determine what this is and what type it has
        val pagingToken: String?
    ) {
        @Serializable
        data class FilterCriteria(
            val filterType: Int,
            val value: String
        )
    }

    companion object {
        /**
         * Construct the payload with default parameters
         * @param pageSize the size of each page
         * @param pageNumber number of the desired page
         * @return the constructed payload
         */
        fun construct(pageSize: Int, pageNumber: Int) = MicrosoftStoreRequestPayload(
            assetTypes = listOf(
                "Microsoft.VisualStudio.Services.Icons.Default",
                "Microsoft.VisualStudio.Services.Icons.Branding",
                "Microsoft.VisualStudio.Services.Icons.Small"
            ),
            filters = listOf(
                Filter(
                    criteria = listOf(
                        Filter.FilterCriteria(
                            filterType = 8,
                            value = "Microsoft.VisualStudio.Code"
                        ),
                        Filter.FilterCriteria(
                            filterType = 10,
                            value = "target:\"Microsoft.VisualStudio.Code\" "
                        ),
                        Filter.FilterCriteria(
                            filterType = 12,
                            value = "3788"
                        ),
                        Filter.FilterCriteria(
                            filterType = 5,
                            value = "Themes"
                        )
                    ),
                    direction = 2,
                    pageSize = pageSize,
                    pageNumber = pageNumber,
                    sortBy = 4,
                    sortOrder = 0,
                    pagingToken = null
                )
            ),
            flags = 870
        )
    }
}
