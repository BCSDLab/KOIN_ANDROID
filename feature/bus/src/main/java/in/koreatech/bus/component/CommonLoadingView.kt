package `in`.koreatech.bus.component

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import `in`.koreatech.koin.feature.bus.R

@Composable
internal fun CommonLoadingView(
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    @RawRes rawRes: Int = R.raw.lottie_loading,
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(rawRes),
    )
    val lottieAnimatable = rememberLottieAnimatable()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        LottieAnimation(
            modifier = Modifier.size(size),
            composition = composition,
            progress = { lottieAnimatable.progress },
            contentScale = ContentScale.FillHeight,
        )
    }
    LaunchedEffect(composition) {
        lottieAnimatable.animate(
            composition = composition,
            iterations = Int.MAX_VALUE,
        )
    }
}
