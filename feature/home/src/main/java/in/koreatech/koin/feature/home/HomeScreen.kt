package `in`.koreatech.koin.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.core.util.KoinCoilImageLoader
import `in`.koreatech.koin.feature.home.component.DiningPager
import `in`.koreatech.koin.feature.home.component.DiningPagerData
import `in`.koreatech.koin.feature.home.component.HomeChip
import `in`.koreatech.koin.feature.home.component.HomeChipDefaults
import `in`.koreatech.koin.feature.home.component.HomeIcon
import `in`.koreatech.koin.feature.home.component.HomeSection
import `in`.koreatech.koin.feature.home.component.LargeHomeCard
import `in`.koreatech.koin.feature.home.component.MediumHomeCard
import `in`.koreatech.koin.feature.home.component.ShuttleTicketCard
import `in`.koreatech.koin.feature.home.component.SmallHomeCard
import `in`.koreatech.koin.feature.home.model.LocalWeather
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.collections.immutable.ImmutableList
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToNotification: () -> Unit = {}
) {
    val uiState by viewModel.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars
    ) { paddingValues ->
        HomeScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8FA))
                .padding(paddingValues),
            uiState = uiState,
            navigateToNotification = navigateToNotification
        )
    }
}

@Composable
private fun HomeScreen(
    uiState: HomeState,
    modifier: Modifier = Modifier,
    navigateToNotification: () -> Unit = {}
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        HeaderSection(
            isNewNotificationReceived = uiState.isNewNotificationReceived,
            onNotificationClick = navigateToNotification
        )
        InfoSection(
            username = uiState.username,
            weather = uiState.weather
        )
        DiningSection(
            diningData = uiState.diningData
        )
        BusSection(
            callVanRecruitCount = uiState.callVanRecruitCount
        )
        ShopSection(
            shopEventForKoin = uiState.shopEventForKoin,
            openShopCount = uiState.openShopCount,
            shopCount = uiState.shopCount
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun HeaderSection(
    isNewNotificationReceived: Boolean,
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_bcsd_symbol),
            contentDescription = "Logo"
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = Modifier.noRippleClickable {
                EventLogger.logCampusClickEvent(AnalyticsConstant.Label.NOTIFICATION, "알림 아이콘")
                onNotificationClick()
            },
            imageVector = if (isNewNotificationReceived) {
                ImageVector.vectorResource(R.drawable.ic_home_notification_dot)
            } else {
                ImageVector.vectorResource(R.drawable.ic_home_notification)
            },
            contentDescription = null
        )
    }
}

@Composable
private fun InfoSection(
    username: String,
    weather: LocalWeather?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val formatter = remember { DateTimeFormatter.ofPattern("M월 dd일 EEEE", Locale.KOREAN) }
    val currentDate = remember(LocalDate.now()) { LocalDate.now().format(formatter) }

    Column(
        modifier = modifier.padding(top = 16.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currentDate,
                style = RebrandKoinTheme.typography.regular13,
                color = Color(0xFFB611F5)
            )

            weather?.let {
                Spacer(modifier = Modifier.width(4.dp))

                AsyncImage(
                    model = it.weatherIconUrl,
                    contentDescription = it.weather,
                    modifier = Modifier.size(16.dp),
                    imageLoader = remember(context) { KoinCoilImageLoader.getImageLoader(context) }
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "${it.weather} ${it.temperature}°",
                    style = RebrandKoinTheme.typography.regular13,
                    color = Color(0xFF6F6F6F)
                )
            }
        }

        Text(
            text = stringResource(
                R.string.home_welcome_text_1,
                username.ifEmpty { stringResource(R.string.home_username_default) }
            ),
            style = RebrandKoinTheme.typography.bold20.copy(fontSize = 24.sp),
            color = Color(0xFF0B0B0D)
        )
    }
}

@Composable
private fun DiningSection(
    diningData: ImmutableList<DiningPagerData>,
    modifier: Modifier = Modifier
) {
    val navigator = rememberNavigator()
    val context = LocalContext.current

    HomeSection(
        modifier = modifier.fillMaxWidth(),
        text = {
            Text(
                text = stringResource(R.string.dining_section),
                style = RebrandKoinTheme.typography.medium18
            )
        },
        more = {
            Row(
                modifier = Modifier.noRippleClickable {
                    EventLogger.logCampusClickEvent(AnalyticsConstant.Label.TODAY_MEAL, "전체보기")
                    navigator.navigateToDining(context).let(context::startActivity)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.view_all),
                    style = RebrandKoinTheme.typography.regular13
                )
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_home_card_arrow),
                    contentDescription = null
                )
            }
        }
    ) {
        DiningPager(data = diningData) {
            navigator.navigateToDining(context).let(context::startActivity)
        }
    }
}

@Composable
private fun BusSection(
    callVanRecruitCount: Int,
    modifier: Modifier = Modifier
) {
    val navigator = rememberNavigator()
    val context = LocalContext.current

    HomeSection(
        modifier = modifier.fillMaxWidth(),
        text = {
            Text(
                text = stringResource(R.string.bus_section),
                style = RebrandKoinTheme.typography.medium18
            )
        },
        more = {
            ShuttleTicketCard {
                EventLogger.logCampusClickEvent(AnalyticsConstant.Label.SHUTTLE_TICKET, "셔틀 탑승권")
                navigator.navigateToUnibus(context).let(context::startActivity)
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            MediumHomeCard(
                title = {
                    Text(
                        text = stringResource(R.string.callvan_title),
                        style = RebrandKoinTheme.typography.medium15,
                        color = Color.White
                    )
                },
                icon = {
                    HomeIcon(
                        tint = Color(0xFFFFFFFF),
                        backgroundColor = Color(0xFFB611F5),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_home_bus_timetable),
                        contentDescription = null
                    )
                },
                label = {
                    HomeChip(
                        text = stringResource(R.string.callvan_recruit_count, callVanRecruitCount),
                        colors = HomeChipDefaults.colors(
                            textColor = Color(0xFF980AC9),
                            backgroundColor = Color(0xFFFAFAFA)
                        )
                    )
                },
                description = {
                    Text(
                        text = stringResource(R.string.callvan_description),
                        style = RebrandKoinTheme.typography.regular12,
                        color = Color(0xFFA8A8A8)
                    )
                },
                actionButton = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFB611F5))
                            .padding(vertical = 4.dp, horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.callvan_action),
                            color = Color(0xFFFAFAFA),
                            style = RebrandKoinTheme.typography.medium13
                        )
                    }
                },
                onClick = {
                    EventLogger.logCampusClickEvent(AnalyticsConstant.Label.CALLVANPOT, "콜밴팟 모집보기")
                    navigator.navigateToCallvan(context).let(context::startActivity)
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SmallHomeCard(
                modifier = Modifier.weight(1f),
                title = {
                    Text(
                        text = stringResource(R.string.bus_timetable),
                        style = RebrandKoinTheme.typography.medium15
                    )
                },
                icon = {
                    HomeIcon(
                        tint = RebrandKoinTheme.colors.primary500,
                        backgroundColor = RebrandKoinTheme.colors.neutral100,
                        imageVector = ImageVector.vectorResource(R.drawable.ic_home_bus_timetable),
                        contentDescription = null
                    )
                },
                description = {
                    Text(
                        text = stringResource(R.string.bus_timetable_description),
                        style = RebrandKoinTheme.typography.regular12,
                        color = Color(0xFFA8A8A8)
                    )
                },
                actionButton = {
                    Text(
                        text = stringResource(R.string.view_more),
                        color = Color(0xFFB611F5),
                        style = RebrandKoinTheme.typography.regular10
                    )
                },
                onClick = {
                    EventLogger.logCampusClickEvent(AnalyticsConstant.Label.BUS_TIMETABLE, "버스 시간표 조회하기")
                    navigator.navigateToBusTimeTable(context).let(context::startActivity)
                }
            )

            SmallHomeCard(
                modifier = Modifier.weight(1f),
                title = {
                    Text(
                        text = stringResource(R.string.bus_route),
                        style = RebrandKoinTheme.typography.medium15
                    )
                },
                icon = {
                    HomeIcon(
                        tint = RebrandKoinTheme.colors.primary500,
                        backgroundColor = RebrandKoinTheme.colors.neutral100,
                        imageVector = ImageVector.vectorResource(R.drawable.ic_home_bus_route),
                        contentDescription = null
                    )
                },
                description = {
                    Text(
                        text = stringResource(R.string.bus_route_description),
                        style = RebrandKoinTheme.typography.regular12,
                        color = Color(0xFFA8A8A8)
                    )
                },
                actionButton = {
                    Text(
                        text = stringResource(R.string.view_more),
                        color = Color(0xFFB611F5),
                        style = RebrandKoinTheme.typography.regular10
                    )
                },
                onClick = {
                    EventLogger.logCampusClickEvent(AnalyticsConstant.Label.BUS_ROUTE, "버스 노선 조회하기")
                    navigator.navigateToBusSearch(context).let(context::startActivity)
                }
            )
        }
    }
}

@Composable
private fun ShopSection(
    shopEventForKoin: Int,
    openShopCount: Int,
    shopCount: Int,
    modifier: Modifier = Modifier
) {
    val navigator = rememberNavigator()
    val context = LocalContext.current
    val shopOpen = stringResource(R.string.shop_open)
    val shopTotal = stringResource(R.string.shop_total, shopCount)

    HomeSection(
        modifier = modifier.fillMaxWidth(),
        text = {
            Text(
                text = stringResource(R.string.shop_section),
                style = RebrandKoinTheme.typography.medium18
            )
        },
        more = {
            Row(
                modifier = Modifier.noRippleClickable {
                    EventLogger.logCampusClickEvent(AnalyticsConstant.Label.SHOP, "전체보기")
                    navigator.navigateToStore(context).let(context::startActivity)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.view_all),
                    style = RebrandKoinTheme.typography.regular13
                )
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_home_card_arrow),
                    contentDescription = null
                )
            }
        }
    ) {
        LargeHomeCard(
            title = {
                Text(
                    text = stringResource(R.string.shop_title),
                    style = RebrandKoinTheme.typography.medium16
                )
            },
            icon = {
                HomeIcon(
                    tint = RebrandKoinTheme.colors.primary500,
                    backgroundColor = RebrandKoinTheme.colors.neutral100,
                    imageVector = ImageVector.vectorResource(R.drawable.ic_home_store),
                    contentDescription = null
                )
            },
            label = {
                HomeChip(
                    text = stringResource(R.string.shop_event_count, shopEventForKoin)
                )
            },
            description = {
                Text(
                    text = buildAnnotatedString {
                        append(shopOpen)
                        withStyle(style = RebrandKoinTheme.typography.bold13.toSpanStyle().copy(color = Color(0xFFB611F5))) {
                            append(" ${openShopCount}곳")
                        }
                        append(" | ")
                        append(shopTotal)
                    },
                    style = RebrandKoinTheme.typography.regular13,
                    color = Color(0xFFA8A8A8)
                )
            },
            onClick = {
                EventLogger.logCampusClickEvent(AnalyticsConstant.Label.POPULAR_SHOP, "많이 찾는 상점 둘러보기")
                navigator.navigateToStore(context).let(context::startActivity)
            }
        )
    }
}
