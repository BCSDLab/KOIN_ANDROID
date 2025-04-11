package `in`.koreatech.koin.feature.banner.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.banner.R
import `in`.koreatech.koin.feature.banner.model.LocalBanner
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun BannerA(
    bannerList: ImmutableList<LocalBanner>,
    currentKoinVersion: Int,
    bannerIndex: Int,
    modifier: Modifier = Modifier,
    dismiss: () -> Unit = {},
    dismissWithRefusal: () -> Unit = {},
    onBannerIndexChange: (Int) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenWidth by remember { mutableStateOf(configuration.screenWidthDp.dp) }
    val maxImageHeight by remember { mutableStateOf(screenWidth / 4 * 3) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.BottomStart
    ) {
        Column(
            modifier = modifier
                .clip(
                    KoinTheme.shapes.medium.copy(
                        bottomStart = CornerSize(0.dp),
                        bottomEnd = CornerSize(0.dp)
                    )
                )
                .background(Color.White)
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                TextButton(
                    onClick = {
                        dismissWithRefusal()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.dismiss_for_a_week),
                        style = KoinTheme.typography.regular14,
                        color = KoinTheme.colors.neutral500
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = {
                        EventLogger.logCampusClickEvent(
                            label = "main_modal_close",
                            value = bannerList[bannerIndex].title
                        )
                        dismiss()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.dismiss),
                        style = KoinTheme.typography.regular14,
                        color = KoinTheme.colors.neutral800
                    )
                }
            }
            BannerImage(
                modifier = Modifier.height(maxImageHeight),
                bannerList = bannerList,
                dismiss = dismiss,
                currentKoinVersion = currentKoinVersion,
                onBannerIndexChange = onBannerIndexChange
            )
        }
    }
}

@Preview
@Composable
fun BannerPreview() {
    BannerA(persistentListOf(), 0, 0)
}
