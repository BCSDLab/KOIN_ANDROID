package `in`.koreatech.koin.core.designsystem.component.dialog

import androidx.annotation.RawRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import `in`.koreatech.koin.core.designsystem.R
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.FilledButtonColors
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButtonColors
import `in`.koreatech.koin.core.designsystem.component.lottie.NewKoinLottie
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

/**
 * 긍정, 부정 버튼이 있는 다이얼로그
 * @param title 다이얼로그 제목 텍스트
 * @param description 제목에 대한 설명 텍스트
 * @param onPositive 긍정 버튼 클릭시 동작할 함수
 * @param onNegative 부정 버튼 클릭시 동작할 함수
 * @param titleStyle 제목 텍스트 스타일
 * @param descriptionStyle 설명 텍스트 스타일
 * @param negativeButtonText 부정 버튼 텍스트
 * @param positiveButtonText 긍정 버튼 텍스트
 * @param positiveButtonColors 긍정 버튼 색상
 * @param negativeButtonColors 부정 버튼 색상
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceDialog(
    title: String,
    description: String,
    onPositive: () -> Unit = {},
    onNegative: () -> Unit = {},
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = KoinTheme.typography.medium18,
    descriptionStyle: TextStyle = KoinTheme.typography.regular14,
    positiveButtonText: String = stringResource(id = R.string.common_confirmation),
    negativeButtonText: String = stringResource(id = R.string.common_cancellation),
    positiveButtonColors: FilledButtonColors = FilledButtonColors.Primary,
    negativeButtonColors: OutlinedBoxButtonColors = OutlinedBoxButtonColors.Neutral
) {
    BasicAlertDialog(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = KoinTheme.colors.neutral0,
                shape = KoinTheme.shapes.extraSmall
            )
            .padding(horizontal = 32.dp, vertical = 24.dp),
        onDismissRequest = { onNegative() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = titleStyle
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = descriptionStyle
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedBoxButton(
                    modifier = Modifier.weight(1.0F),
                    text = negativeButtonText,
                    onClick = onNegative,
                    colors = negativeButtonColors
                )
                FilledButton(
                    modifier = Modifier.weight(1.0F),
                    text = positiveButtonText,
                    onClick = onPositive,
                    colors = positiveButtonColors
                )
            }
        }
    }
}

/**
 * 긍정, 부정 버튼이 있는 다이얼로그
 * @param title 다이얼로그 제목 컴포저블
 * @param description 제목에 대한 설명 컴포저블
 * @param onPositive 긍정 버튼 클릭시 동작할 함수
 * @param onNegative 부정 버튼 클릭시 동작할 함수
 * @param negativeButtonText 부정 버튼 텍스트
 * @param positiveButtonText 긍정 버튼 텍스트
 * @param positiveButtonColors 긍정 버튼 색상
 * @param negativeButtonColors 부정 버튼 색상
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceDialog(
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    onPositive: () -> Unit = {},
    onNegative: () -> Unit = {},
    modifier: Modifier = Modifier,
    positiveButtonText: String = stringResource(id = R.string.common_confirmation),
    negativeButtonText: String = stringResource(id = R.string.common_cancellation),
    positiveButtonColors: FilledButtonColors = FilledButtonColors.Primary,
    negativeButtonColors: OutlinedBoxButtonColors = OutlinedBoxButtonColors.Neutral
) {
    BasicAlertDialog(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = KoinTheme.colors.neutral0,
                shape = KoinTheme.shapes.extraSmall
            )
            .padding(horizontal = 32.dp, vertical = 24.dp),
        onDismissRequest = { onNegative() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            title()
            Spacer(modifier = Modifier.height(8.dp))
            description()
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedBoxButton(
                    modifier = Modifier.weight(1.0F),
                    text = negativeButtonText,
                    onClick = onNegative,
                    colors = negativeButtonColors
                )
                FilledButton(
                    modifier = Modifier.weight(1.0F),
                    text = positiveButtonText,
                    onClick = onPositive,
                    colors = positiveButtonColors
                )
            }
        }
    }
}

/**
 * 긍정, 부정 버튼과 lottie가 있는 다이얼로그
 * @param title 다이얼로그 제목 텍스트
 * @param description 제목에 대한 설명 텍스트
 * @param onPositive 긍정 버튼 클릭시 동작할 함수
 * @param onNegative 부정 버튼 클릭시 동작할 함수
 * @param titleStyle 제목 텍스트 스타일
 * @param descriptionStyle 설명 텍스트 스타일
 * @param negativeButtonText 부정 버튼 텍스트
 * @param positiveButtonText 긍정 버튼 텍스트
 * @param positiveButtonColors 긍정 버튼 색상
 * @param negativeButtonColors 부정 버튼 색상
 * @param lottieRes lottie 파일(json)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceDialog(
    title: String,
    description: String,
    onPositive: () -> Unit = {},
    onNegative: () -> Unit = {},
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = KoinTheme.typography.medium18,
    descriptionStyle: TextStyle = KoinTheme.typography.regular14,
    positiveButtonText: String = stringResource(id = R.string.common_confirmation),
    negativeButtonText: String = stringResource(id = R.string.common_cancellation),
    positiveButtonColors: FilledButtonColors = FilledButtonColors.Primary,
    negativeButtonColors: OutlinedBoxButtonColors = OutlinedBoxButtonColors.Neutral,
    lottieCompose: @Composable (() -> Unit)? = { NewKoinLottie(Modifier.width(150.dp).height(120.dp)) }
) {
    BasicAlertDialog(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = KoinTheme.colors.neutral0,
                shape = KoinTheme.shapes.extraSmall
            )
            .padding(horizontal = 32.dp, vertical = 24.dp),
        onDismissRequest = { onNegative() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            lottieCompose?.invoke()
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = title,
                style = titleStyle
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = descriptionStyle
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedBoxButton(
                    modifier = Modifier.weight(1.0F),
                    text = negativeButtonText,
                    onClick = onNegative,
                    colors = negativeButtonColors
                )
                FilledButton(
                    modifier = Modifier.weight(1.0F),
                    text = positiveButtonText,
                    onClick = onPositive,
                    colors = positiveButtonColors
                )
            }
        }
    }
}

/**
 * 긍정, 부정 버튼과 lottie가 있는 다이얼로그
 * @param title 다이얼로그 제목 컴포저블
 * @param description 제목에 대한 설명 컴포저블
 * @param onPositive 긍정 버튼 클릭시 동작할 함수
 * @param onNegative 부정 버튼 클릭시 동작할 함수
 * @param negativeButtonText 부정 버튼 텍스트
 * @param positiveButtonText 긍정 버튼 텍스트
 * @param positiveButtonColors 긍정 버튼 색상
 * @param negativeButtonColors 부정 버튼 색상
 * @param lottieRes lottie 파일(json)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceDialog(
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    onPositive: () -> Unit = {},
    onNegative: () -> Unit = {},
    modifier: Modifier = Modifier,
    positiveButtonText: String = stringResource(id = R.string.common_confirmation),
    negativeButtonText: String = stringResource(id = R.string.common_cancellation),
    positiveButtonColors: FilledButtonColors = FilledButtonColors.Primary,
    negativeButtonColors: OutlinedBoxButtonColors = OutlinedBoxButtonColors.Neutral,
    lottieCompose: @Composable (() -> Unit)? = { NewKoinLottie(Modifier.width(150.dp).height(120.dp)) }
) {
    BasicAlertDialog(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = KoinTheme.colors.neutral0,
                shape = KoinTheme.shapes.extraSmall
            )
            .padding(horizontal = 32.dp, vertical = 24.dp),
        onDismissRequest = { onNegative() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            lottieCompose?.invoke()
            Spacer(modifier = Modifier.height(24.dp))
            title()
            Spacer(modifier = Modifier.height(8.dp))
            description()
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedBoxButton(
                    modifier = Modifier.weight(1.0F),
                    text = negativeButtonText,
                    onClick = onNegative,
                    colors = negativeButtonColors
                )
                FilledButton(
                    modifier = Modifier.weight(1.0F),
                    text = positiveButtonText,
                    onClick = onPositive,
                    colors = positiveButtonColors
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChoiceDialogPreviewWithLottie() {
    KoinTheme {
        ChoiceDialog(
            title = "다이얼로그 제목",
            description = "이러쿵 저러쿵 이러쿵 저러쿵 이러쿵 저러쿵 이러쿵 저러쿵 ",
            descriptionStyle = KoinTheme.typography.regular14.copy(
                color = KoinTheme.colors.neutral500
            ),
            onPositive = {},
            onNegative = {},
            lottieCompose = { NewKoinLottie(Modifier.width(150.dp).height(120.dp)) }
        )
    }
}

@Preview
@Composable
private fun ChoiceDialogPreview() {
    KoinTheme {
        ChoiceDialog(
            title = "다이얼로그 제목",
            description = "이러쿵 저러쿵 이러쿵 저러쿵 이러쿵 저러쿵 이러쿵 저러쿵 ",
            descriptionStyle = KoinTheme.typography.regular14.copy(
                color = KoinTheme.colors.neutral500
            ),
            onPositive = {},
            onNegative = {}
        )
    }
}

@Preview
@Composable
private fun ChoiceDialogComposableTextPreview() {
    KoinTheme {
        ChoiceDialog(
            title = {
                Text(
                    text =
                    buildAnnotatedString {
                        withStyle(KoinTheme.typography.medium16.toSpanStyle()) {
                            append("이렇게 ")
                        }
                        withStyle(KoinTheme.typography.bold16.toSpanStyle()) {
                            append("강조하는")
                        }
                        withStyle(KoinTheme.typography.medium16.toSpanStyle()) {
                            append(" 제목은 컴포저블로")
                        }
                    },
                    textAlign = TextAlign.Center
                )
            },
            description = {
                Text(
                    text =
                    buildAnnotatedString {
                        withStyle(KoinTheme.typography.regular16.toSpanStyle()) {
                            append("설명하는 부분도 ")
                        }
                        withStyle(KoinTheme.typography.medium16.copy(color = KoinTheme.colors.danger700).toSpanStyle()) {
                            append("이렇게 강조하는")
                        }
                        withStyle(KoinTheme.typography.regular16.toSpanStyle()) {
                            append(" 컴포저블로 추가할 수 있습니다")
                        }
                    },
                    textAlign = TextAlign.Center
                )
            },
            onPositive = {},
            onNegative = {},
            positiveButtonColors = FilledButtonColors.Warning
        )
    }
}
