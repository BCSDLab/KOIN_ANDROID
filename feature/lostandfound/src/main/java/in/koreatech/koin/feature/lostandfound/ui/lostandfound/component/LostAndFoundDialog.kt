package `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.R
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.FilledButtonColors
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButtonColors
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

/**
 * @see in.koreatech.koin.core.designsystem.component.dialog.ChoiceDialog
 * 긍정, 부정 버튼이 있는 다이얼로그
 * 버튼 수정
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
fun LostAndFoundDialog(
    title: String,
    description: String,
    onPositive: () -> Unit = {},
    onNegative: () -> Unit = {},
    modifier: Modifier = Modifier,
    lostAndFoundDialogStyle: LostAndFoundDialogStyle = lostAndFoundDialogStyle(),
) {
    BasicAlertDialog(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = KoinTheme.colors.neutral0,
                    shape = KoinTheme.shapes.small,
                )
                .padding(horizontal = 32.dp, vertical = 24.dp),
        onDismissRequest = { onNegative() },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = lostAndFoundDialogStyle.titleStyle,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = lostAndFoundDialogStyle.descriptionStyle,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedBoxButton(
                    modifier = Modifier.weight(1.0F),
                    text = lostAndFoundDialogStyle.negativeButtonText,
                    onClick = onNegative,
                    shape = KoinTheme.shapes.small,
                    colors = lostAndFoundDialogStyle.negativeButtonColors,
                    contentPadding = PaddingValues(12.dp, 12.dp),
                )
                FilledButton(
                    modifier = Modifier.weight(1.0F),
                    text = lostAndFoundDialogStyle.positiveButtonText,
                    onClick = onPositive,
                    shape = KoinTheme.shapes.small,
                    colors = lostAndFoundDialogStyle.positiveButtonColors,
                    contentPadding = PaddingValues(12.dp, 12.dp),
                )
            }
        }
    }
}

@Composable
fun lostAndFoundDialogStyle(
    titleStyle: TextStyle = KoinTheme.typography.medium18,
    descriptionStyle: TextStyle = KoinTheme.typography.regular14,
    positiveButtonText: String = stringResource(id = R.string.common_confirmation),
    negativeButtonText: String = stringResource(id = R.string.common_cancellation),
    positiveButtonColors: FilledButtonColors = FilledButtonColors.Primary,
    negativeButtonColors: OutlinedBoxButtonColors = OutlinedBoxButtonColors.Neutral,
) = LostAndFoundDialogStyle(
    titleStyle = titleStyle,
    descriptionStyle = descriptionStyle,
    positiveButtonText = positiveButtonText,
    negativeButtonText = negativeButtonText,
    positiveButtonColors = positiveButtonColors,
    negativeButtonColors = negativeButtonColors,
)

@Immutable
class LostAndFoundDialogStyle(
    val titleStyle: TextStyle,
    val descriptionStyle: TextStyle,
    val positiveButtonText: String,
    val negativeButtonText: String,
    val positiveButtonColors: FilledButtonColors,
    val negativeButtonColors: OutlinedBoxButtonColors,
) {
    fun copy(
        titleStyle: TextStyle = this.titleStyle,
        descriptionStyle: TextStyle = this.descriptionStyle,
        positiveButtonText: String = this.positiveButtonText,
        negativeButtonText: String = this.negativeButtonText,
        positiveButtonColors: FilledButtonColors = this.positiveButtonColors,
        negativeButtonColors: OutlinedBoxButtonColors = this.negativeButtonColors,
    ) = LostAndFoundDialogStyle(
        titleStyle = titleStyle,
        descriptionStyle = descriptionStyle,
        positiveButtonText = positiveButtonText,
        negativeButtonText = negativeButtonText,
        positiveButtonColors = positiveButtonColors,
        negativeButtonColors = negativeButtonColors,
    )
}
