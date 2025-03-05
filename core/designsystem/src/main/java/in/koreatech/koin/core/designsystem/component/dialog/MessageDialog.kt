package `in`.koreatech.koin.core.designsystem.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.R
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.FilledButtonColors
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButtonColors
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme



/**
 *  1개의 버튼이 있는 다이얼로그
 * @param title 다이얼로그 제목 텍스트
 * @param onPositive 긍정 버튼 클릭시 동작할 함수
 * @param onNegative 부정 버튼 클릭시 동작할 함수
 * @param titleStyle 제목 텍스트 스타일
 * @param positiveButtonText 긍정 버튼 텍스트
 * @param positiveButtonColors 긍정 버튼 색상
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageDialog(
    modifier: Modifier = Modifier,
    title: String = "",
    onPositive: () -> Unit = {},
    onNegative: () -> Unit = {},
    titleStyle: TextStyle = KoinTheme.typography.medium18,
    positiveButtonText: String = stringResource(id = R.string.navigate_back_content_description),
    positiveButtonColors: FilledButtonColors = FilledButtonColors.Primary,
) {
    BasicAlertDialog(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = KoinTheme.colors.neutral0,
                shape = KoinTheme.shapes.large
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
            Spacer(modifier = Modifier.height(32.dp))

            FilledButton(
                modifier = Modifier
                    .wrapContentWidth(),
                text = positiveButtonText,
                onClick = onPositive,
                colors = positiveButtonColors
            )
        }
    }
}


@Preview
@Composable
fun PreviewMassageDialog(

){
    MessageDialog(
        title = "요일을 선택해 주세요."
    )
}