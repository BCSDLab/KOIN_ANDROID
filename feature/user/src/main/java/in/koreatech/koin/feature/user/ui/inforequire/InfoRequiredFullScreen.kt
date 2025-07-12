package `in`.koreatech.koin.feature.user.ui.inforequire

import androidx.annotation.RawRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.lottie.NewKoinLottie
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.user.R

/**
 * 정보 입력 필요 알림을 보여주는 화면
 * @param lottieRes lottie 파일(json)
 * @param buttonText 버튼 텍스트
 * @param titleText 제목 텍스트
 * @param descriptionText 제목에 대한 설명 텍스트
 * @param onPositive 버튼 클릭시 실행 함수
 */
@Composable
fun InfoRequiredFullScreen(
    buttonText: String,
    titleText: String,
    descriptionText: String,
    modifier: Modifier = Modifier,
    onPositive: () -> Unit
) {
    val buttonColors = ButtonColors(
        containerColor = RebrandKoinTheme.colors.primary500,
        contentColor = KoinTheme.colors.neutral0,
        disabledContainerColor = KoinTheme.colors.neutral300,
        disabledContentColor = KoinTheme.colors.neutral600
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 중앙 콘텐츠
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NewKoinLottie(
                modifier = Modifier
                    .width(240.dp)
                    .height(140.dp)
            )

            Text(
                text = titleText,
                style = KoinTheme.typography.bold20.copy(
                    color = KoinTheme.colors.neutral700
                )
            )

            Text(
                text = descriptionText,
                style = KoinTheme.typography.regular14.copy(
                    color = KoinTheme.colors.neutral500
                ),
                textAlign = TextAlign.Center
            )
        }

        // 하단 버튼
        FilledButton(
            onClick = { onPositive() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 16.dp,
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 16.dp
                )
                .fillMaxWidth(),
            colors = buttonColors,
            shape = KoinTheme.shapes.small,
            text = buttonText
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        InfoRequiredFullScreen(
            buttonText = "정보 입력하러 가기",
            titleText = "새로워진 코인, 준비 완료!",
            descriptionText = "몇 가지 정보만 더 입력해 주시면\n더 편하고 똑똑하게 이용하실 수 있어요!",
            onPositive = { }
        )
    }
}
