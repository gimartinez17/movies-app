package com.gmart.gmovies.utils

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.gmart.domain.model.Cast
import com.gmart.domain.model.Credit
import com.gmart.domain.model.Crew
import com.gmart.domain.model.Detail
import com.gmart.domain.model.ExternalIds
import com.gmart.domain.model.Genre
import com.gmart.domain.model.MediaType
import com.gmart.domain.model.Person
import com.gmart.domain.model.Provider
import com.gmart.domain.model.ProviderType.BUY
import com.gmart.domain.model.ProviderType.RENT
import com.gmart.domain.model.ProviderType.STREAM
import com.gmart.domain.model.Video
import com.gmart.domain.model.WatchProviders
import com.gmart.gmovies.ui.theme.AppTheme


@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
annotation class ThemePreviews


@Preview(name = "PIXEL_3", device = Devices.PIXEL_3, showSystemUi = true)
@Preview(name = "PIXEL_4", device = Devices.PIXEL_4, showSystemUi = true)
@Preview(name = "PIXEL_3_XL", device = Devices.PIXEL_3_XL, showSystemUi = true)
@Preview(name = "PIXEL_4_XL", device = Devices.PIXEL_4_XL, showSystemUi = true)
@Preview(name = "NEXUS_6", device = Devices.NEXUS_6, showSystemUi = true)
@Preview(name = "PIXEL_XL", device = Devices.PIXEL_XL, showSystemUi = true)
@Preview(name = "NEXUS_5", device = Devices.NEXUS_5, showSystemUi = true)
@Preview(name = "PIXEL_3A_XL", device = Devices.PIXEL_3A_XL, showSystemUi = true)
@Preview(name = "OnePlus 9 Pro", device = "spec:shape=Normal,width=1440,height=3216,unit=px,dpi=525", showSystemUi = true)
@Preview(name = "Samsung Galaxy S21 Ultra", device = "spec:shape=Normal,width=1440,height=3200,unit=px,dpi=515", showSystemUi = true)
@Preview(name = "Samsung Galaxy Z Fold 3", device = Devices.FOLDABLE, showSystemUi = true)
annotation class PortraitDevicesPreviews

@Composable
fun PreviewLayout(content: @Composable () -> Unit) {
    AppTheme {
        Surface {
            content()
        }
    }
}

/*  Mocked data for previewing composable  */

val mockDirectorData = Crew(id = 0, name = "Juan Perez", department = "Directing", job = "Director")
val mockWriterData = Crew(id = 0, name = "Juan Perez", department = "Writing", job = "Writer")
val mockCastData = Cast(id = 0, character = "Pepito Grillo", name = "Juancito Perez", creditId = "")
val mockGenreData = Genre(id = 1, name = "Action", image = null)
val mockApple =
    Provider(id = "1", logoPath = "path", name = "Netflix", from = mutableListOf(RENT, BUY))
val mockNetflix =
    Provider(id = "1", logoPath = "path", name = "Apple TV", from = mutableListOf(STREAM))
val mockHbo = Provider(id = "1", logoPath = "path", name = "HBO", from = mutableListOf(STREAM))
val mockWatchProviders = WatchProviders(info = List(15){mockApple})
val mockVideo = Video(id = "1", key = "key", name = "Trailer", site = "YouTube", type = "Trailer")
val mockMovieDetails = Detail(
    id = 1,
    title = "Lorem ipsum dolor sit",
    mediaType = MediaType.MOVIE,
    overview = List(16) { "Lorem ipsum dolor sit amet." }.joinToString(" "),
    numberOfSeasons = 4,
    voteAverage = 8.5f,
    runtime = 120,
    releaseDate = "2023-12-14",
    originCountry = listOf("GB"),
    genres = List(6) { mockGenreData },
    recommendations = List(6) { Detail(id = 1, title = "Title") },
    credits = Credit(
        cast = List(6) { mockCastData },
        crew = listOf(mockDirectorData, mockWriterData)
    ),
    videos = List(1) { mockVideo },
)
val mockPersonDetails = Person(
    name = "Juan Perez",
    id = 1,
    biography = List(8) { "Lorem ipsum dolor sit amet." }.joinToString(" "),
    birthday = "1990-12-14",
    deathDay = "2023-12-14",
    placeOfBirth = "London, England, UK",
    profilePath = "",
    knownForDepartment = "Acting",
    externalIds = ExternalIds(
        imdbId = "tt1234567",
        facebookId = "facebookId",
        instagramId = "instagramId",
        twitterId = "twitterId",
        tiktokId = "tiktokId",
        youtubeId = "youtubeId",
    ),
    alsoKnownAs = List(2) { "Pepito" },
    castCredits = List(6) { mockMovieDetails },
)
