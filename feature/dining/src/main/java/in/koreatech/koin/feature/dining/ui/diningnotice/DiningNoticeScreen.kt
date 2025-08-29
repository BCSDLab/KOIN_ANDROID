package `in`.koreatech.koin.feature.dining.ui.diningnotice

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.coopshop.CoopShop
import `in`.koreatech.koin.domain.model.coopshop.CoopShopDayType
import `in`.koreatech.koin.domain.model.coopshop.OpenCloseInfo
import `in`.koreatech.koin.domain.model.coopshop.OpenCloseTimeInfo
import `in`.koreatech.koin.feature.dining.R
import `in`.koreatech.koin.feature.dining.component.DiningTimetableItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiningNoticeScreen(
    viewModel: DiningNoticeViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {}
) {
    val diningNotice by viewModel.diningNotice.collectAsState()
    val context = LocalContext.current

    val window = (context as Activity).window
    LaunchedEffect(Unit) {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            WindowCompat.getInsetsController(window, window.decorView).apply {
                isAppearanceLightStatusBars = false
            }
        }
    }

    Scaffold(
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.dining_notice_appbar_title),
                onNavigationIconClick = onTopbarBackClick
            )
        }
    ) { contentPadding ->
        DiningNoticeScreenImpl(
            diningNotice = diningNotice,
            modifier = Modifier
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
                .systemBarsPadding(),
            isLoading = viewModel.isLoading.value ?: false
        )
    }
}

@Composable
fun DiningNoticeScreenImpl(
    diningNotice: CoopShop,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {

    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = rememberLazyListState()
    ) {
        item {
            Column(
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.dining_notice_title_format, diningNotice.name, diningNotice.semester),
                    style = KoinTheme.typography.bold20
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.dining_notice_location),
                        style = KoinTheme.typography.medium14,
                        color = KoinTheme.colors.neutral600
                    )
                    Text(
                        text = diningNotice.location,
                        style = KoinTheme.typography.medium14,
                        color = KoinTheme.colors.neutral600
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.dining_notice_phone),
                        style = KoinTheme.typography.medium14,
                        color = KoinTheme.colors.neutral600
                    )
                    Text(
                        text = diningNotice.phone,
                        style = KoinTheme.typography.medium14,
                        color = KoinTheme.colors.neutral600
                    )
                }
            }
        }
        item {
            HorizontalDivider(
                thickness = 6.dp,
                color = KoinTheme.colors.neutral100
            )
            diningNotice.opens.forEach {
                DiningTimetableItem(
                    titleText = stringResource(R.string.dining_notice_subtitle_time_format, it.dayOfWeek.value),
                    openCloseTimeInfoList = it.opensByDayType
                )
            }
        }
        item {
            Text(
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp),
                text = stringResource(R.string.dining_notice_updated_at_format, diningNotice.updatedAt),
                style = KoinTheme.typography.regular12,
                color = KoinTheme.colors.neutral500 // Change to a similar color; original color is 0xFF8E8E8E
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DiningNoticeScreenImplPreview() {
    DiningNoticeScreenImpl(
        diningNotice = CoopShop(
            id = -1,
            name = "학생식당",
            semester = "25-하계방학",
            opens = listOf(
                OpenCloseInfo(
                    dayOfWeek = CoopShopDayType.Weekday,
                    opensByDayType = listOf(
                        OpenCloseTimeInfo(
                            type = "아침",
                            openTime = "08:00",
                            closeTime = "09:00"
                        ),
                        OpenCloseTimeInfo(
                            type = "점심",
                            openTime = "13:00",
                            closeTime = "14:00"
                        ),
                        OpenCloseTimeInfo(
                            type = "저녁",
                            openTime = "17:00",
                            closeTime = "18:00"
                        )
                    )
                ),
                OpenCloseInfo(
                    dayOfWeek = CoopShopDayType.Weekend,
                    opensByDayType = listOf(
                        OpenCloseTimeInfo(
                            type = "아침",
                            openTime = "08:00",
                            closeTime = "09:00"
                        ),
                        OpenCloseTimeInfo(
                            type = "점심",
                            openTime = "13:00",
                            closeTime = "14:00"
                        ),
                        OpenCloseTimeInfo(
                            type = "저녁",
                            openTime = "17:00",
                            closeTime = "18:00"
                        )
                    )
                )
            ),
            phone = "000-0000-0000",
            location = "location",
            remarks = "",
            updatedAt = "2025.08.15"
        )
    )
}
