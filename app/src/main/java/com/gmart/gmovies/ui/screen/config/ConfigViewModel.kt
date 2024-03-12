package com.gmart.gmovies.ui.screen.config

import androidx.lifecycle.viewModelScope
import com.gmart.data.source.utils.IoDispatcher
import com.gmart.domain.model.Country
import com.gmart.domain.model.Language
import com.gmart.domain.model.Resource
import com.gmart.domain.usecase.ConfigUseCase
import com.gmart.gmovies.ui.base.BaseViewModel
import com.gmart.gmovies.utils.Quadruple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val useCase: ConfigUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : BaseViewModel<ConfigViewState, ConfigEvent, Unit>(dispatcher) {

    private val _savedConfig: MutableStateFlow<SavedConfig> = MutableStateFlow(SavedConfig())

    init {
        getConfiguration()
    }

    override fun setInitialState() = ConfigViewState()

    override fun handleEvents(event: ConfigEvent) {
        when (event) {
            ConfigEvent.GetSavedConfiguration -> getSavedConfig()
            is ConfigEvent.SetLanguage -> setLanguage(event.language)
            is ConfigEvent.SetCountry -> setCountry(event.country)
            is ConfigEvent.SetDarkMode -> setDarkMode(event.mode)
        }
    }


    private fun getSavedConfig() {
        viewModelScope.launch(dispatcher) {
            val languageDeferred = async { useCase.getLanguage() }
            val countryDeferred = async { useCase.getCountry() }
            val darkModeDeferred = async { useCase.getDarkMode() }

            combine(
                languageDeferred.await(),
                countryDeferred.await(),
                darkModeDeferred.await(),
                ::Triple
            ).collect { (language, country, darkMode) ->
                _savedConfig.value = SavedConfig(
                    language = language,
                    country = country,
                    darkMode = darkMode
                )
            }
        }
    }

    private fun setLanguage(language: String) {
        viewModelScope.launch(dispatcher) {
            useCase.saveLanguage(language)
        }
    }

    private fun setCountry(country: String) {
        viewModelScope.launch(dispatcher) {
            useCase.saveCountry(country)
        }
    }

    private fun setDarkMode(mode: String) {
        viewModelScope.launch(dispatcher) {
            useCase.saveDarkMode(mode)
        }
    }

    private fun getConfiguration() {
        viewModelScope.launch(dispatcher) {
            val primaryTranslationsDeferred = async { useCase.getPrimaryTranslations() }
            val countriesDeferred = async { useCase.getCountryList() }
            val languagesDeferred = async { useCase.getLanguageList() }

            combine(
                primaryTranslationsDeferred.await(),
                countriesDeferred.await(),
                languagesDeferred.await(),
                _savedConfig,
                ::Quadruple
            ).onStart {
                setViewState(ConfigViewState(loading = true))
            }
                .collect { (primaryTranslationsResource, countriesResource, languagesResource, savedConfig) ->
                    val primaryTranslations = when (primaryTranslationsResource) {
                        is Resource.Success -> primaryTranslationsResource.response
                        else -> emptyList()
                    }
                    val countries = when (countriesResource) {
                        is Resource.Success -> countriesResource.response.sortedBy { it.nativeName }
                        else -> emptyList()
                    }
                    val languages = when (languagesResource) {
                        is Resource.Success -> {
                            val list = languagesResource.response.filterLanguages(
                                primaryTranslations,
                                countries
                            )
                            list.sortedBy { it.nativeName }
                        }

                        else -> emptyList()
                    }
                    setViewState(
                        ConfigViewState(
                            countryOptions = countries,
                            languageOptions = languages,
                            savedConfig = savedConfig
                        )
                    )
                }
        }
    }

    private fun List<Language>.filterLanguages(
        primaryTranslations: List<String>,
        countries: List<Country>
    ): List<Language> {
        val allowedLanguages = listOf("es", "en", "it", "de", "fr")
        val filteredLanguages = mutableListOf<Language>()
        primaryTranslations.forEach { primaryTranslation ->
            this.find {
                it.language == primaryTranslation.substring(0, 2) && it.language in allowedLanguages
            }?.let { language ->
                countries.find { it.country == primaryTranslation.substring(3, 5) }
                    ?.let { country ->
                        filteredLanguages.add(
                            Language(
                                language = language.language,
                                englishName = language.englishName,
                                nativeName = language.nativeName,
                                code = primaryTranslation,
                                country = country.englishName,
                            )
                        )
                    }
            }
        }
        return filteredLanguages
    }
}
