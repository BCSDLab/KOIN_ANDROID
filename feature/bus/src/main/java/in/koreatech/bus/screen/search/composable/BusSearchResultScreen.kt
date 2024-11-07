package `in`.koreatech.bus.screen.search.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.bus.screen.search.viewmodel.BusSearchResultViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BusSearchResultScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    viewModel: BusSearchResultViewModel = hiltViewModel()
) {

    LazyColumn (
        modifier = modifier
    ) {
        item {
            KoinTopAppBar(
                title = "한기대 → 천안터미널", // TODO : 방향
                onNavigationIconClick = onNavigationIconClick
            )
        }

        stickyHeader {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.noRippleClickable {
                        // TODO : 타임 피커 오픈
                    }, verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("오늘 오전 10:30")
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                            ) {
                                append(" 출발")
                            }
                        }, style = KoinTheme.typography.bold16,
                        color = KoinTheme.colors.info700
                    )
                    Icon(
                        modifier = Modifier.padding(start = 4.dp),
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.set_time_content_description),
                        tint = KoinTheme.colors.neutral500
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

}