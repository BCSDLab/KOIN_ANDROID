package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.domain.model.cart.CartType
import `in`.koreatech.koin.feature.store.R
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun KoinStoreFloatingButton(
    etaTime: LocalTime,
    storeName: String,
    cartType: CartType,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .shadow(elevation = 4.dp, shape = CircleShape)
            .clip(CircleShape)
            .background(color = KoinTheme.colors.neutral0, shape = CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_store_fab))

        LottieAnimation(
            composition = composition,
            modifier = Modifier.size(48.dp),
            iterations = LottieConstants.IterateForever
        )

        Column(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            BasicText(
                stringResource(
                    when (cartType) {
                        CartType.DELIVERY -> R.string.store_fab_delivery_eta
                        CartType.TAKE_OUT -> R.string.store_fab_takeout_eta
                    },
                    etaTime.format(DateTimeFormatter.ofPattern("a h시 m분"))
                ),
                style = KoinTheme.typography.bold16.copy(
                    color = RebrandKoinTheme.colors.primary600,
                    lineHeightStyle = LineHeightStyle(
                        trim = LineHeightStyle.Trim.Both,
                        alignment = LineHeightStyle.Alignment.Center
                    )
                )
            )

            BasicText(
                storeName,
                style = RebrandKoinTheme.typography.medium12.copy(
                    color = RebrandKoinTheme.colors.neutral600,
                    lineHeightStyle = LineHeightStyle(
                        trim = LineHeightStyle.Trim.Both,
                        alignment = LineHeightStyle.Alignment.Center
                    )
                )
            )
        }

        Image(
            modifier = Modifier.size(24.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_store_arrow_right_new),
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinStoreFloatingButtonPreview() {
    KoinStoreFloatingButton(
        etaTime = LocalTime.now(),
        cartType = CartType.TAKE_OUT,
        storeName = "Store name"
    )
}
