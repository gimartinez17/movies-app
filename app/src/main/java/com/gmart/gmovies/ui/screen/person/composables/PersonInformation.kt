package com.gmart.gmovies.ui.screen.person.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gmart.domain.model.MediaType
import com.gmart.domain.model.Person
import com.gmart.gmovies.R
import com.gmart.gmovies.ui.screen.details.composables.body.composables.OverviewDetails
import com.gmart.gmovies.ui.screen.details.composables.body.composables.RelatedMediaList
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.calculateAge
import com.gmart.gmovies.utils.getSimpleFormatDate
import com.gmart.gmovies.utils.mockPersonDetails

@Composable
fun PersonInformation(
    modifier: Modifier = Modifier,
    data: Person,
    onMediaClick: (Int, MediaType) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.padding(bottom = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        OverviewDetails(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
            title = stringResource(id = R.string.person_biography),
            overview = data.biography
        )
        Spacer(modifier = Modifier.padding(top = 12.dp))
        if (data.knownForDepartment != null) {
            PersonInfoData(
                modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp),
                title = stringResource(id = R.string.person_known_for) + ": ",
                value = stringResource(id = getForKnownFor(data.knownForDepartment!!))
            )
        }
        if (data.birthday != null) {
            val age = calculateAge(context, data.birthday!!)
            PersonInfoData(
                modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp),
                title = stringResource(id = R.string.person_birthdate),
                value = data.birthday?.getSimpleFormatDate(context) +
                        if (data.deathDay == null) getAgeText(age) else ""
            )
        }
        if (data.birthday != null && data.deathDay != null) {
            PersonInfoData(
                modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp),
                title = stringResource(id = R.string.person_deathday),
                value = data.deathDay?.getSimpleFormatDate(context) + getAgeText(
                    calculateAge(
                        context,
                        data.birthday!!,
                        data.deathDay
                    )
                )
            )
        }
        PersonInfoData(
            modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp),
            title = stringResource(id = R.string.person_place_of_birth),
            value = data.placeOfBirth
        )
        if (data.castCredits?.isNotEmpty() == true)
            RelatedMediaList(
                modifier = Modifier.padding(top = 16.dp),
                title = stringResource(id = R.string.person_known_for),
                list = data.castCredits!!
                    .distinctBy { it.id }
                    .sortedByDescending { it.voteCount }
                    .take(25),
                onMediaClick = onMediaClick,
            )
    }
}

fun getForKnownFor(knownForDepartment: String): Int {
    return when (knownForDepartment) {
        "Acting" -> R.string.known_for_acting
        "Directing" -> R.string.known_for_directing
        "Writing" -> R.string.known_for_writing
        "Crew" -> R.string.known_for_crew
        "Production" -> R.string.known_for_production
        else -> R.string.known_for_acting
    }
}

@Composable
fun getAgeText(age: Int) = " ($age ${stringResource(id = R.string.person_years_old)})"

@ThemePreviews
@Composable
private fun PersonInformationPreview() {
    PreviewLayout {
        PersonInformation(data = mockPersonDetails)
    }
}