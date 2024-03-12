package com.gmart.gmovies.ui.screen.details.composables.body

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gmart.domain.model.Crew
import com.gmart.domain.model.CrewType
import com.gmart.domain.model.CrewType.DIRECTOR
import com.gmart.domain.model.CrewType.WRITER
import com.gmart.domain.model.MediaType
import com.gmart.gmovies.R
import com.gmart.gmovies.ui.composable.PoweredBy
import com.gmart.gmovies.ui.screen.details.DetailsViewState
import com.gmart.gmovies.ui.screen.details.composables.body.composables.CastDetails
import com.gmart.gmovies.ui.screen.details.composables.body.composables.CreatorDetails
import com.gmart.gmovies.ui.screen.details.composables.body.composables.CrewDetails
import com.gmart.gmovies.ui.screen.details.composables.body.composables.OverviewDetails
import com.gmart.gmovies.ui.screen.details.composables.body.composables.ProvidersDetails
import com.gmart.gmovies.ui.screen.details.composables.body.composables.RelatedMediaList
import com.gmart.gmovies.ui.screen.details.composables.body.composables.ReleaseDateDetails
import com.gmart.gmovies.ui.screen.details.composables.body.composables.SeasonsDetails
import com.gmart.gmovies.ui.screen.details.composables.body.composables.VideosList
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockMovieDetails
import com.gmart.gmovies.utils.mockWatchProviders

@Composable
internal fun DetailsBody(
    media: DetailsViewState,
    mediaType: MediaType?,
    modifier: Modifier,
    onMediaClick: (Int, MediaType) -> Unit = { _, _ -> },
    onCastItemClick: (Int) -> Unit = {},
    onViewAllCastClick: () -> Unit = {},
    onVideoClick: (String) -> Unit = {},
) {
    val movie = media.details!!
    Column(
        modifier = modifier.padding(bottom = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        OverviewDetails(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
            title = stringResource(id = R.string.details_overview),
            overview = movie.overview
        )
        Spacer(modifier = Modifier.height(16.dp))
        val directors = media.details.credits?.crew?.getCrewMembers(DIRECTOR)
        CrewDetails(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            crew = directors,
            title = directors?.getTitle(DIRECTOR) ?: ""
        )
        CreatorDetails(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            creator = movie.createdBy,
        )

        val writers = media.details.credits?.crew?.getCrewMembers(WRITER)
        CrewDetails(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            crew = writers,
            title = writers?.getTitle(WRITER) ?: ""
        )
        SeasonsDetails(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            seasons = media.details.numberOfSeasons,
        )
        ReleaseDateDetails(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            visibility = mediaType == MediaType.MOVIE,
            releaseDate = media.details.releaseDate,
        )

        ProvidersDetails(
            modifier = Modifier.padding(top = 16.dp),
            watchProviders = media.watchProviders,
        )

        CastDetails(
            modifier = Modifier.padding(top = 16.dp),
            cast = media.details.credits?.cast,
            onCastItemClick = onCastItemClick,
            onViewAllClick = onViewAllCastClick,
        )

        VideosList(
            modifier = Modifier.padding(vertical = 16.dp),
            title = "Trailer",
            videos = media.details.videos,
            onClick = onVideoClick,
        )

        val mediaRecommended =
            if ((media.details.mediaType ?: mediaType) == MediaType.MOVIE)
                stringResource(id = R.string.details_recommended_movies)
            else
                stringResource(id = R.string.details_recommended_tv_shows)
        RelatedMediaList(
            modifier = Modifier.padding(top = 16.dp),
            title = mediaRecommended,
            list = media.details.recommendations,
            onMediaClick = onMediaClick,
        )
        PoweredBy(
            modifier = Modifier
                .padding(top = 32.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

private fun List<Crew>.getCrewMembers(crewType: CrewType) =
    this.filter { it.department == crewType.department && it.job == crewType.job }

@Composable
private fun List<Crew>.getTitle(crewType: CrewType) = when (crewType) {
    DIRECTOR -> if (this.size > 1) stringResource(id = R.string.details_directors)
    else stringResource(id = R.string.details_director)

    WRITER -> if (this.size > 1) stringResource(id = R.string.details_writers)
    else stringResource(id = R.string.details_writer)
}

@ThemePreviews
@Composable
private fun DetailsBodyPreview() {
    PreviewLayout {
        DetailsBody(
            media = DetailsViewState(
                details = mockMovieDetails,
                watchProviders = mockWatchProviders,
            ),
            mediaType = MediaType.MOVIE,
            modifier = Modifier.fillMaxWidth()
        )
    }
}