package com.gmart.gmovies.ui.screen.config

import AppBarState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gmart.domain.model.Country
import com.gmart.domain.model.Language
import com.gmart.gmovies.R
import com.gmart.gmovies.navigation.ScreenRoutes
import com.gmart.gmovies.ui.base.SIDE_EFFECTS_KEY
import com.gmart.gmovies.ui.composable.AppDialog
import com.gmart.gmovies.ui.composable.Loading
import com.gmart.gmovies.ui.screen.config.composables.AboutOption
import com.gmart.gmovies.ui.screen.config.composables.CountryOptions
import com.gmart.gmovies.ui.screen.config.composables.DarkModeOption
import com.gmart.gmovies.ui.screen.config.composables.DarkModeOptions
import com.gmart.gmovies.ui.screen.config.composables.LanguageOptions
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.localeSelection

@Composable
fun ConfigScreen(
    onComposing: (AppBarState) -> Unit = {},
    viewModel: ConfigViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val options = listOf(DarkModeOption.ON, DarkModeOption.OFF, DarkModeOption.SYSTEM)
    var showRestartModal by remember { mutableStateOf(false) }
    var languageOptionSelected by remember { mutableStateOf("") }
    val setting = stringResource(id = R.string.setting)
    val context = LocalContext.current

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        onComposing(configAppBar(setting))
        viewModel.setEvent(ConfigEvent.GetSavedConfiguration)
    }

    if (viewState.loading) Loading(modifier = Modifier.fillMaxSize())
    ConfigContent(
        darkModeSelected = DarkModeOption.fromValue(viewState.savedConfig.darkMode),
        languageSelected = viewState.savedConfig.language,
        countrySelected = viewState.savedConfig.country,
        darkModeOptions = options,
        languageOptions = viewState.languageOptions ?: listOf(),
        countryOptions = viewState.countryOptions ?: listOf(),
        onDarkModeClick = { viewModel.setEvent(ConfigEvent.SetDarkMode(it.value)) },
        onLanguageClick = { option ->
            showRestartModal = true
            languageOptionSelected = option
        },
        onCountryClick = { option -> viewModel.setEvent(ConfigEvent.SetCountry(option)) },
    )

    if (showRestartModal) {
        AppDialog(
            title = stringResource(id = R.string.language_restart_title),
            body = { Text(stringResource(id = R.string.language_restart_message)) },
            dismissText = stringResource(id = R.string.language_restart_cancel),
            confirmText = stringResource(id = R.string.language_restart_confirm),
            neutralText = stringResource(id = R.string.language_restart_later),
            onDismiss = { showRestartModal = false },
            onConfirm = {
                showRestartModal = false
                localeSelection(
                    context = context,
                    localeTag = languageOptionSelected,
                    country = viewState.savedConfig.country,
                    restart = true
                )
                viewModel.setEvent(ConfigEvent.SetLanguage(languageOptionSelected))
            },
            onNeutral = {
                showRestartModal = false
                localeSelection(
                    context = context,
                    localeTag = languageOptionSelected,
                    country = viewState.savedConfig.country,
                    restart = false,
                )
                viewModel.setEvent(ConfigEvent.SetLanguage(languageOptionSelected))
            }
        )
    }
}

private fun configAppBar(setting: String) = AppBarState(
    key = ScreenRoutes.Config.route,
    showBackAction = true,
    title = setting,
    showLogo = false,
)

@Composable
private fun ConfigContent(
    darkModeSelected: DarkModeOption,
    languageSelected: String = "",
    countrySelected: String = "",
    darkModeOptions: List<DarkModeOption> = listOf(),
    languageOptions: List<Language> = listOf(),
    countryOptions: List<Country> = listOf(),
    onDarkModeClick: (DarkModeOption) -> Unit = {},
    onLanguageClick: (String) -> Unit = {},
    onCountryClick: (String) -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            DarkModeOptions(
                modifier = Modifier.padding(horizontal = 48.dp, vertical = 4.dp),
                options = darkModeOptions,
                selectedOption = darkModeSelected,
                valueChanged = onDarkModeClick,
            )
            LanguageOptions(
                modifier = Modifier.padding(horizontal = 48.dp, vertical = 4.dp),
                options = languageOptions,
                selectedOption = languageSelected,
                valueChanged = onLanguageClick,
            )
            CountryOptions(
                modifier = Modifier
                    .padding(horizontal = 48.dp, vertical = 4.dp),
                countries = countryOptions,
                selectedOption = countrySelected,
                valueChanged = onCountryClick,
            )
            AboutOption(modifier = Modifier.padding(horizontal = 48.dp, vertical = 4.dp))
        }
    }
}

@ThemePreviews
@Composable
private fun ConfigContentPreview() {
    PreviewLayout {
        ConfigContent(
            darkModeSelected = DarkModeOption.ON,
            darkModeOptions = listOf(DarkModeOption.ON, DarkModeOption.OFF, DarkModeOption.SYSTEM),
            languageSelected = "Español",
            languageOptions = listOf(
                Language("en-US", "English"),
                Language("es-ES", "Español"),
                Language("fr-FR", "Français"),
            ),
            countrySelected = "United States",
            countryOptions = listOf(
                Country("US", "United States"),
                Country("ES", "España"),
                Country("FR", "France"),
            ),
        )
    }
}
