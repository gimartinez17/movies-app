package com.gmart.gmovies.ui.screen.config

import com.gmart.domain.model.Country
import com.gmart.domain.model.Language

data class ConfigViewState(
    val savedConfig: SavedConfig = SavedConfig(),
    val countryOptions: List<Country>? = null,
    val languageOptions: List<Language>? = null,
    val loading: Boolean = false,
    val errorMessage: String? = null,
)

data class SavedConfig(
    val darkMode: String = "system",
    val language: String = "system",
    val country: String = "system",
)

sealed class ConfigEvent {
    object GetSavedConfiguration : ConfigEvent()
    data class SetLanguage(val language: String) : ConfigEvent()
    data class SetCountry(val country: String) : ConfigEvent()
    data class SetDarkMode(val mode: String) : ConfigEvent()
}