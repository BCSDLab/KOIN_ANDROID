package `in`.koreatech.koin.feature.banner.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.banner.R
import `in`.koreatech.koin.feature.banner.model.LocalBanner
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun BannerB(
    bannerList: ImmutableList<LocalBanner>,
    currentKoinVersion: Int,
    modifier: Modifier = Modifier,
    dismiss: () -> Unit = {},
    dismissWithRefusal: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenWidth by remember { mutableStateOf(configuration.screenWidthDp.dp - 48.dp) } // minus horizontal padding
    val maxImageHeight by remember { mutableStateOf(screenWidth / 4 * 3) }

    var bannerIndex: Int by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 24.dp)
                .clip(KoinTheme.shapes.large)
                .background(KoinTheme.colors.neutral0)
        ) {
            BannerImage(
                modifier = Modifier.height(maxImageHeight),
                bannerList = bannerList,
                dismiss = dismiss,
                currentKoinVersion = currentKoinVersion,
                onBannerIndexChange = {
                    bannerIndex = it
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 16.dp)
            ) {
                OutlinedBoxButton(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(2f),
                    text = stringResource(R.string.dismiss_for_a_week),
                    textStyle = KoinTheme.typography.medium15.copy(color = KoinTheme.colors.primary500),
                    shape = KoinTheme.shapes.small,
                    onClick = {
                        EventLogger.logCampusClickEvent(
                            label = "main_modal_hide_7d",
                            value = bannerList[bannerIndex].title
                        )
                        dismissWithRefusal()
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                FilledButton(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f),
                    text = stringResource(R.string.dismiss),
                    textStyle = KoinTheme.typography.medium15.copy(color = KoinTheme.colors.neutral0),
                    shape = KoinTheme.shapes.small,
                    onClick = {
                        EventLogger.logCampusClickEvent(
                            label = "main_modal_close",
                            value = bannerList[bannerIndex].title
                        )
                        dismiss()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun BannerBPreview() {
    KoinTheme {
        BannerB(
            bannerList = persistentListOf(),
            currentKoinVersion = 0
        )
    }
}
