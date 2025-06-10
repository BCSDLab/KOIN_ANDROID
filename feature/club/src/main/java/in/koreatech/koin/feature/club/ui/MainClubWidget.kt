package `in`.koreatech.koin.feature.club.ui

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.component.KoinClubCategoryItem
import `in`.koreatech.koin.feature.club.component.KoinClubMainItem
import `in`.koreatech.koin.feature.club.model.clubCategories
import `in`.koreatech.koin.feature.club.navigation.CATEGORY_ID
import `in`.koreatech.koin.feature.club.navigation.CLUB_ID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainClubWidgetA(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.club_main_activity_widget_title),
                style = KoinTheme.typography.bold18.copy(fontSize = 15.sp),
                color = KoinTheme.colors.primary500
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                modifier = Modifier.noRippleClickable {
                    Intent(context, ClubActivity::class.java).apply {
                        context.startActivity(this)
                    }
                },
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                tint = KoinTheme.colors.primary500,
                contentDescription = stringResource(R.string.club_main_activity_widget_title)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.CenterHorizontally)
        ) {
            clubCategories.forEach {
                KoinClubCategoryItem(
                    categoryName = stringResource(it.stringRes),
                    icon = painterResource(it.drawableRes),
                    onClick = {
                        EventLogger.logCampusClickEvent(
                            AnalyticsConstant.Label.Club.MAIN_CLUB,
                            context.getString(it.stringRes)
                        )
                        Intent(context, ClubActivity::class.java).apply {
                            putExtra(CATEGORY_ID, it.id)
                            context.startActivity(this)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MainClubWidgetB(
    hotClubId: Int,
    hotClubImageUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.club_main_activity_widget_title),
            style = KoinTheme.typography.bold18.copy(fontSize = 15.sp),
            color = KoinTheme.colors.primary500
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            KoinClubMainItem(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.club_main_widget_popular_club),
                description = stringResource(R.string.club_main_widget_go),
                icon = rememberAsyncImagePainter(hotClubImageUrl),
                onClick = {
                    EventLogger.logCampusClickEvent(
                        AnalyticsConstant.Label.Club.MAIN_POPULAR_CLUB,
                        "인기 동아리"
                    )
                    Intent(context, ClubActivity::class.java).apply {
                        putExtra(CLUB_ID, hotClubId)
                        context.startActivity(this)
                    }
                }
            )

            Spacer(modifier = Modifier.width(16.dp))

            KoinClubMainItem(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.club_main_widget_club_list),
                description = stringResource(R.string.club_main_widget_go),
                icon = painterResource(R.drawable.ic_club_list),
                onClick = {
                    EventLogger.logCampusClickEvent(
                        AnalyticsConstant.Label.Club.MAIN_CLUB,
                        ""
                    )
                    Intent(context, ClubActivity::class.java).apply {
                        context.startActivity(this)
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainClubWidgetA() {
    KoinTheme {
        MainClubWidgetA()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainClubWidgetB() {
    KoinTheme {
        MainClubWidgetB(
            hotClubId = 0,
            hotClubImageUrl = ""
        )
    }
}
