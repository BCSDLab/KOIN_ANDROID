package `in`.koreatech.koin.ui.main.widget

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.abtest.ExperimentGroup
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.onboarding.ArrowDirection
import `in`.koreatech.koin.core.onboarding.OnboardingType
import `in`.koreatech.koin.core.onboarding.rememberOnboardingManager
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningPlace
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.ui.dining.DiningActivity
import kotlinx.coroutines.launch

@Composable
fun DiningWidget(
    diningData: List<Dining>,
    selectedPosition: Int?,
    selectedType: DiningType,
    diningABTestExperimentGroup: String,
    modifier: Modifier = Modifier
) {
    val pagerState =
        rememberPagerState(
            initialPage = 0
        ) {
            4
        }

    LaunchedEffect(selectedPosition) {
        pagerState.scrollToPage(selectedPosition ?: 0)
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val tabIndex = pagerState.currentPage
    val type = DiningUtil.getCurrentType()
    val onboardingManager = rememberOnboardingManager()

    EventLogger.logClickEvent(
        EventAction.CAMPUS,
        AnalyticsConstant.Label.MAIN_MENU_MOVEDETAILVIEW,
        "${
            context.getString(
                if (type == DiningType.NextBreakfast) R.string.dining_tomorrow else R.string.dining_today
            )
        } ${context.getString(R.string.navigation_item_dining)}"
    )

    Column(
        modifier = modifier
    ) {
        Row {
            with(onboardingManager) {
                ShowOnboardingTooltipIfNeeded(
                    type = OnboardingType.DINING_IMAGE,
                    arrowDirection = ArrowDirection.LEFT
                ) {
                    Text(
                        modifier = Modifier.padding(start = 20.dp),
                        text = "${
                            stringResource(
                                if (type == DiningType.NextBreakfast) R.string.dining_tomorrow else R.string.dining_today
                            )
                        } ${stringResource(R.string.navigation_item_dining)}",
                        style = KoinTheme.typography.bold15,
                        color = KoinTheme.colors.primary500
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (diningABTestExperimentGroup == ExperimentGroup.MAIN_DINING_NEW) {
                Row(
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .noRippleClickable {
                            Intent(context, DiningActivity::class.java).let {
                                context.startActivity(it)
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        style = KoinTheme.typography.regular14.copy(color = KoinTheme.colors.neutral500),
                        text = stringResource(R.string.dining_see_more)
                    )

                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_right),
                        contentDescription = stringResource(R.string.dining_see_more)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        ScrollableTabRow(
            selectedTabIndex = tabIndex,
            containerColor = Color.Transparent,
            edgePadding = 20.dp
        ) {
            DiningPlace.entries.filter { it != DiningPlace.Campus2 }
                .forEachIndexed { index, place ->
                    Tab(
                        text = {
                            Text(
                                text = place.place,
                                style = KoinTheme.typography.medium14
                            )
                        },
                        unselectedContentColor = KoinTheme.colors.neutral500,
                        selected = tabIndex == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
        }

        HorizontalPager(
            modifier = Modifier.background(KoinTheme.colors.neutral50),
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { index ->
            Box(
                modifier = Modifier.background(KoinTheme.colors.neutral50)
            ) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .fillMaxWidth()
                        .clip(KoinTheme.shapes.small),
                    onClick = {
                        Intent(context, DiningActivity::class.java).let {
                            context.startActivity(it)
                        }
                    },
                    colors =
                    CardDefaults.cardColors(
                        containerColor = KoinTheme.colors.neutral0
                    ),
                    shape = KoinTheme.shapes.small,
                    border = BorderStroke(1.dp, KoinTheme.colors.neutral300)
                ) {
                    if (diningData.lastIndex >= index && diningData[index].menu.isNotEmpty()) {
                        DiningContent(
                            diningData = diningData[index],
                            selectedType = selectedType
                        )
                    } else {
                        DiningEmptyContent()
                    }
                }
            }
        }
    }
}

@Composable
fun DiningContent(
    diningData: Dining,
    selectedType: DiningType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                style = KoinTheme.typography.regular16,
                text = selectedType.typeKorean
            )

            Spacer(modifier = Modifier.weight(1f))

            if (diningData.soldOutAt.isNotEmpty() || diningData.changedAt.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(KoinTheme.shapes.extraSmall)
                        .background(
                            when {
                                diningData.soldOutAt.isNotEmpty() -> KoinTheme.colors.warning200
                                diningData.changedAt.isNotEmpty() -> KoinTheme.colors.neutral200
                                else -> Color.Unspecified
                            }
                        )
                        .padding(vertical = 4.dp, horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        style = KoinTheme.typography.regular14,
                        color = when {
                            diningData.soldOutAt.isNotEmpty() -> KoinTheme.colors.warning600
                            diningData.changedAt.isNotEmpty() -> KoinTheme.colors.neutral600
                            else -> Color.Unspecified
                        },
                        text = when {
                            diningData.soldOutAt.isNotEmpty() -> stringResource(R.string.dining_soldout)
                            diningData.changedAt.isNotEmpty() -> stringResource(R.string.dining_changed)
                            else -> ""
                        }
                    )
                }
            }
        }

        HorizontalDivider(
            color = KoinTheme.colors.neutral300
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            diningData.menu.chunked(5).forEach {
                Text(
                    modifier = Modifier.weight(1f),
                    style = KoinTheme.typography.regular12,
                    minLines = 5,
                    text = it.joinToString("\n")
                )
            }
        }
    }
}

@Composable
fun DiningEmptyContent() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Column { // Placeholder
            Text(
                modifier = Modifier.padding(16.dp),
                style = KoinTheme.typography.bold16,
                text = ""
            )
            HorizontalDivider(color = KoinTheme.colors.neutral0)
            Text(
                modifier = Modifier.padding(16.dp),
                style = KoinTheme.typography.regular12,
                minLines = 5,
                text = ""
            )
        }
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(R.drawable.ic_dining_not_valid),
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDiningWidget() {
    KoinTheme {
        DiningWidget(
            selectedPosition = 0,
            selectedType = DiningType.Breakfast,
            diningABTestExperimentGroup = ExperimentGroup.MAIN_DINING_NEW,
            diningData = listOf(
                Dining(
                    id = 0,
                    date = "2025-01-01",
                    type = "아침",
                    place = "학생식당",
                    priceCard = "5000",
                    priceCash = "5000",
                    kcal = "500",
                    menu = listOf("밥", "김치찌개", "김치", "두부조림", "계란후라이", "깍두기"),
                    imageUrl = "",
                    createdAt = "",
                    updatedAt = "",
                    soldOutAt = "",
                    changedAt = ""
                )
            )
        )
    }
}
