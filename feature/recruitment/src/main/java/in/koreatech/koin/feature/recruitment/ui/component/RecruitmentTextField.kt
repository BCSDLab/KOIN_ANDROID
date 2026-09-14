package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

private val TextFieldShape = RoundedCornerShape(16.dp)

/**
 * 모집 도메인 전반에서 사용하는 기본 텍스트 필드
 * @param value 입력 값
 * @param onValueChange 값 변경 콜백
 * @param hint 입력값이 없을 때 보여줄 힌트
 * @param maxLength 입력 최대 길이. 글자수 카운터는 이 컴포넌트가 아니라
 * 상위 FormSection의 타이틀 라인(예: "제목 * 9/50")에서 표시합니다.
 * @param singleLine 한 줄 입력 여부
 * @param minLines 최소 줄 수 (singleLine = false 인 경우에만 유효)
 */
@Composable
fun RecruitmentTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    enabled: Boolean = true,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLength: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(modifier = modifier) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, RebrandKoinTheme.colors.neutral200, TextFieldShape)
                .background(RebrandKoinTheme.colors.neutral0, TextFieldShape),
            value = value,
            onValueChange = { newValue ->
                if (maxLength == null || newValue.length <= maxLength) {
                    onValueChange(newValue)
                } else {
                    onValueChange(newValue.take(maxLength))
                }
            },
            enabled = enabled,
            singleLine = singleLine,
            minLines = if (singleLine) 1 else minLines,
            textStyle = RebrandKoinTheme.typography.regular14.copy(
                color = RebrandKoinTheme.colors.neutral800
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            style = RebrandKoinTheme.typography.regular14,
                            color = RebrandKoinTheme.colors.neutral500
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentTextFieldPreview() {
    RebrandKoinTheme {
        RecruitmentTextField(
            value = "",
            onValueChange = {},
            hint = "제목을 입력해주세요",
            maxLength = 60
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentTextFieldMultilinePreview() {
    RebrandKoinTheme {
        RecruitmentTextField(
            value = "",
            onValueChange = {},
            hint = "모집 소개를 작성해주세요",
            singleLine = false,
            minLines = 5,
            maxLength = 500
        )
    }
}
