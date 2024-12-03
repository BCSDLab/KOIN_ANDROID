package `in`.koreatech.bus.screen.timetabledetail.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.chip.ReadOnlyTextChip
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ShuttleTimetableDetailScreenContent(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {}
) {

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        KoinTopAppBar(
            title = stringResource(R.string.title_bus_timetable),
            onNavigationIconClick = onNavigationIconClick
        )

        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            ReadOnlyTextChip(
                title = "순환",
                containerColor = Color(0xFF4ED92C),
                textStyle = KoinTheme.typography.regular12.copy(color = Color.White)
            )

            Text(
                text = "천안 셔틀 시간표",
                style = KoinTheme.typography.bold20,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        HorizontalDivider(
            color = KoinTheme.colors.neutral400
        )

        Box(
            modifier = Modifier.fillMaxWidth().height(14.dp).background(
                color = KoinTheme.colors.neutral100
            )
        )

        val timetableRowScrollState = rememberScrollState()
        Column (

        ) {
            repeat(5) {
                ShuttleTimetableDetailItem(
                    scrollState = timetableRowScrollState
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShuttleTimetableDetailScreenContentPreview() {
    ShuttleTimetableDetailScreenContent()
}