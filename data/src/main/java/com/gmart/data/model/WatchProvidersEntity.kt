package com.gmart.data.model

import com.google.gson.annotations.SerializedName
import com.gmart.domain.model.Provider
import com.gmart.domain.model.ProviderType
import com.gmart.domain.model.WatchLocale
import com.gmart.domain.model.WatchProviders

data class WatchLocaleEntity(
    var results: Map<String, WatchProvidersEntity>
)

data class WatchProvidersEntity(
    var link: String,
    var flatrate: List<ProviderEntity>?,
    var rent: List<ProviderEntity>?,
    var buy: List<ProviderEntity>?,
)

data class ProviderEntity(
    @SerializedName("logo_path")
    var logoPath: String?,
    @SerializedName("provider_id")
    var providerId: String?,
    @SerializedName("provider_name")
    var providerName: String?,
    @SerializedName("display_priority")
    var displayPriority: Int?,
)

fun WatchLocaleEntity.mapToModel(): WatchLocale {
    return WatchLocale(
        results = results.mapValues { it.value.mapToModel() }
    )
}

fun WatchProvidersEntity.mapToModel(): WatchProviders {
    val groupedProviders = flatrate.mapToModel(ProviderType.STREAM) +
            rent.mapToModel(ProviderType.RENT) +
            buy.mapToModel(ProviderType.BUY)

    val providers = groupedProviders.groupBy { it.id }
        .values
        .map { group ->
            group.reduce { acc, providerInfo ->
                Provider(
                    logoPath = providerInfo.logoPath,
                    id = providerInfo.id,
                    name = providerInfo.name,
                    displayPriority = providerInfo.displayPriority,
                    from = acc.from.apply {
                        this?.addAll(providerInfo.from ?: emptyList())
                    }
                )
            }
        }

    return WatchProviders(
        link = link,
        info = providers,
    )
}

fun List<ProviderEntity>?.mapToModel(type: ProviderType): List<Provider> {
    return this?.map {
        Provider(
            logoPath = it.logoPath,
            id = it.providerId,
            name = it.providerName,
            displayPriority = it.displayPriority,
            from = mutableListOf(type),
        )
    } ?: emptyList()
}

fun ProviderEntity.mapToModel(type: ProviderType) = Provider(
    logoPath = logoPath,
    id = providerId,
    name = providerName,
    displayPriority = displayPriority,
    from = mutableListOf(type),
)

//example.json
val example = """
    {
        "id": 466420,
        "results": {
            "US": {
                "link": "https://www.themoviedb.org/movie/466420-killers-of-the-flower-moon/watch?locale=US",
                "rent": [
                    {
                        "logo_path": "/peURlLlr8jggOwK53fJ5wdQl05y.jpg",
                        "provider_id": 2,
                        "provider_name": "Apple TV",
                        "display_priority": 4
                    },
                    {
                        "logo_path": "/5NyLm42TmCqCMOZFvH4fcoSNKEW.jpg",
                        "provider_id": 10,
                        "provider_name": "Amazon Video",
                        "display_priority": 15
                    },
                    {
                        "logo_path": "/tbEdFQDwx5LEVr8WpSeXQSIirVq.jpg",
                        "provider_id": 3,
                        "provider_name": "Google Play Movies",
                        "display_priority": 16
                    },
                    {
                        "logo_path": "/oIkQkEkwfmcG7IGpRR1NB8frZZM.jpg",
                        "provider_id": 192,
                        "provider_name": "YouTube",
                        "display_priority": 17
                    },
                    {
                        "logo_path": "/21dEscfO8n1tL35k4DANixhffsR.jpg",
                        "provider_id": 7,
                        "provider_name": "Vudu",
                        "display_priority": 42
                    },
                    {
                        "logo_path": "/shq88b09gTBYC4hA7K7MUL8Q4zP.jpg",
                        "provider_id": 68,
                        "provider_name": "Microsoft Store",
                        "display_priority": 53
                    },
                    {
                        "logo_path": "/1tLCqSH5xiViDxMiTVWl6DmE8hd.jpg",
                        "provider_id": 486,
                        "provider_name": "Spectrum On Demand",
                        "display_priority": 170
                    }
                ],
                "buy": [
                    {
                        "logo_path": "/peURlLlr8jggOwK53fJ5wdQl05y.jpg",
                        "provider_id": 2,
                        "provider_name": "Apple TV",
                        "display_priority": 4
                    },
                    {
                        "logo_path": "/5NyLm42TmCqCMOZFvH4fcoSNKEW.jpg",
                        "provider_id": 10,
                        "provider_name": "Amazon Video",
                        "display_priority": 15
                    },
                    {
                        "logo_path": "/tbEdFQDwx5LEVr8WpSeXQSIirVq.jpg",
                        "provider_id": 3,
                        "provider_name": "Google Play Movies",
                        "display_priority": 16
                    },
                    {
                        "logo_path": "/oIkQkEkwfmcG7IGpRR1NB8frZZM.jpg",
                        "provider_id": 192,
                        "provider_name": "YouTube",
                        "display_priority": 17
                    },
                    {
                        "logo_path": "/21dEscfO8n1tL35k4DANixhffsR.jpg",
                        "provider_id": 7,
                        "provider_name": "Vudu",
                        "display_priority": 42
                    },
                    {
                        "logo_path": "/shq88b09gTBYC4hA7K7MUL8Q4zP.jpg",
                        "provider_id": 68,
                        "provider_name": "Microsoft Store",
                        "display_priority": 53
                    }
                ]
            }
        }
    }
""".trimIndent()
