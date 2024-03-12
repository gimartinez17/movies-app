package com.gmart.domain.model

enum class ProviderType(val type: String) {
    STREAM("Subscription"), BUY("Buy"), RENT("Rent");
}

data class WatchLocale(
    var results: Map<String, WatchProviders>
)

data class WatchProviders(
    var link: String? = null,
    var info: List<Provider>? = null,
)

data class Provider(
    var id: String? = null,
    var name: String? = null,
    var logoPath: String? = null,
    var displayPriority: Int? = null,
    var from: MutableList<ProviderType>? = null,
)
