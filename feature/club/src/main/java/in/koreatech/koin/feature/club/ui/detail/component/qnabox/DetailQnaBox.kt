package `in`.koreatech.koin.feature.club.ui.detail.component.qnabox

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.ui.detail.component.dialog.DetailDialog
import `in`.koreatech.koin.feature.club.ui.detail.component.dialog.content.DetailDialogDeleteQnaContent
import `in`.koreatech.koin.feature.club.ui.detail.component.textfield.DetailQnaTextField

@Composable
fun DetailQnaBox(
    qnaId: Int,
    questionText: String,
    createdDate: String,
    addAnswerText: String,
    modifier: Modifier = Modifier,
    onDeleteQnaClick: (Int) -> Unit = {},
    onAddAnswerClick: (Int, String) -> Unit = { _, _ -> },
    onAddAnswerTextChange: (String) -> Unit = {},
    isQnaEditable: Boolean = false,
    isAnswerEditable: Boolean = false,
    answerQnaId: Int? = null,
    answerText: String? = null
) {
    var isError by remember { mutableStateOf(false) }
    var showDeleteQnaDialog by remember { mutableStateOf(Pair(false, -1)) }
    if (showDeleteQnaDialog.first) {
        DetailDialog(
            modifier = Modifier,
            title = stringResource(R.string.detail_dialog_qna_delete_title),
            onPositive = {
                onDeleteQnaClick(showDeleteQnaDialog.second)
                showDeleteQnaDialog = Pair(false, -1)
            },
            onNegative = { showDeleteQnaDialog = Pair(false, -1) },
            content = {
                DetailDialogDeleteQnaContent()
            },
            positiveButtonText = stringResource(R.string.detail_dialog_qna_delete_positive),
            positiveButtonColors = ButtonColors(
                containerColor = KoinTheme.colors.danger500,
                contentColor = KoinTheme.colors.neutral0,
                disabledContainerColor = KoinTheme.colors.neutral300,
                disabledContentColor = KoinTheme.colors.neutral600
            )
        )
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(KoinTheme.colors.neutral0)
            .border(
                width = 1.dp,
                color = KoinTheme.colors.neutral300,
                shape = KoinTheme.shapes.extraSmall
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 8.dp,
                    vertical = 8.dp
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
            ) {
                Text(
                    text = "Q. $questionText",
                    style = KoinTheme.typography.regular18,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                )
                if (isQnaEditable) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_qna_delete),
                        contentDescription = "QuestionDelete",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 4.dp)
                            .clickable {
                                showDeleteQnaDialog = Pair(true, qnaId)
                            }
                    )
                }
            }
            Spacer(Modifier.height(4.dp))

            Text(
                text = createdDate,
                style = KoinTheme.typography.regular14,
                color = KoinTheme.colors.neutral600
            )
            if (isAnswerEditable || !answerText.isNullOrEmpty()) {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_qna_answer),
                        contentDescription = "QuestionDelete",
                        modifier = Modifier
                            .size(24.dp)
                            .offset { IntOffset(x = 0, y = if(isError) -(13.dp.roundToPx()) else 0) }
                    )
                    if (answerText.isNullOrEmpty() || answerQnaId == null) {
                        DetailQnaTextField(
                            modifier = Modifier.padding(end = 4.dp),
                            value = addAnswerText,
                            textFieldColor = KoinTheme.colors.primary300,
                            onValueChange = onAddAnswerTextChange,
                            onSendClick = {
                                if (addAnswerText.isNotEmpty()) {
                                    onAddAnswerClick(qnaId, addAnswerText)
                                    isError = false
                                }
                                else {
                                    isError = true
                                }
                            },
                            isError = isError,
                            errorMessage = stringResource(R.string.detail_textfield_error_empty)
                        )
                    } else {
                        Text(
                            text = "$answerText",
                            style = KoinTheme.typography.regular18,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f)
                        )
                        if (isAnswerEditable) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_qna_delete),
                                contentDescription = "QuestionDelete",
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 4.dp)
                                    .clickable {
                                        showDeleteQnaDialog = Pair(true, qnaId)
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DetailQnaBoxNoAnswer() {
    DetailQnaBox(
        qnaId = 1,
        questionText = "추가 모집 공고는 언제 올라오나요",
        createdDate = "2025.00.00. 00:00",
        addAnswerText = ""
    )
}

@Preview
@Composable
fun DetailQnaBoxNoAnswerQnaEditable() {
    DetailQnaBox(
        qnaId = 1,
        questionText = "추가 모집 공고는 언제 올라오나요",
        createdDate = "2025.00.00. 00:00",
        addAnswerText = "",
        isQnaEditable = true
    )
}

@Preview
@Composable
fun DetailQnaBoxNoAnswerAllEditable() {
    DetailQnaBox(
        qnaId = 1,
        questionText = "추가 모집 공고는 언제 올라오나요",
        createdDate = "2025.00.00. 00:00",
        addAnswerText = "",
        isQnaEditable = true,
        isAnswerEditable = true
    )
}

@Preview
@Composable
fun DetailQnaBoxAnswer() {
    DetailQnaBox(
        qnaId = 1,
        questionText = "추가 모집 공고는 언제 올라오나요",
        createdDate = "2025.00.00. 00:00",
        addAnswerText = "",
        answerText = "올렸습니다!",
        answerQnaId = 2

    )
}

@Preview
@Composable
fun DetailQnaBoxAnswerQnaEditable() {
    DetailQnaBox(
        qnaId = 1,
        questionText = "추가 모집 공고는 언제 올라오나요",
        createdDate = "2025.00.00. 00:00",
        addAnswerText = "",
        answerText = "올렸습니다!",
        answerQnaId = 2,
        isQnaEditable = true
    )
}

@Preview
@Composable
fun DetailQnaBoxAnswerAllEditable() {
    DetailQnaBox(
        qnaId = 1,
        questionText = "추가 모집 공고는 언제 올라오나요",
        createdDate = "2025.00.00. 00:00",
        addAnswerText = "",
        answerText = "올렸습니다!",
        answerQnaId = 2,
        isQnaEditable = true,
        isAnswerEditable = true
    )
}
