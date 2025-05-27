package `in`.koreatech.koin.feature.club.ui

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.component.KoinClubCategoryItem
import `in`.koreatech.koin.feature.club.component.KoinClubMainItem
import `in`.koreatech.koin.feature.club.model.clubCategories
import `in`.koreatech.koin.feature.club.navigation.CATEGORY_ID

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
        Text(
            text = stringResource(R.string.club_main_activity_widget_title),
            style = KoinTheme.typography.bold18.copy(fontSize = 15.sp),
            color = KoinTheme.colors.primary500
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            clubCategories.forEach {
                KoinClubCategoryItem(
                    modifier = Modifier.weight(1f),
                    categoryName = stringResource(it.stringRes),
                    icon = painterResource(it.drawableRes),
                    onClick = {
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
    hotClubImageUrl: String,
    modifier: Modifier = Modifier
) {
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
                icon = rememberAsyncImagePainter(hotClubImageUrl)
            )

            Spacer(modifier = Modifier.width(16.dp))

            KoinClubMainItem(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.club_main_widget_club_list),
                description = stringResource(R.string.club_main_widget_go),
                icon = painterResource(R.drawable.ic_club_list)
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
            hotClubImageUrl = ""
        )
    }
}
