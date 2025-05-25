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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.ui.detail.component.textfield.DetailQnaTextField

@Composable
fun DetailQnaBox(
    modifier: Modifier = Modifier,
    qnaId: Int,
    questionText: String,
    answerQnaId: Int? = null,
    answerText: String? = null,
    createdDate: String,
    onDeleteQuestionClick: (Int) -> Unit,
    onDeleteAnswerClick: (Int) -> Unit,
    onAddAnswerClick: (Int, String) -> Unit,
    isQnaEditable: Boolean = false,
    isAnswerEditable: Boolean = false
) {
    var text by remember { mutableStateOf("") }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(KoinTheme.colors.neutral0)
            .border(
                width = 1.dp,
                color = KoinTheme.colors.neutral300,
                shape = RoundedCornerShape(size = 4.dp)
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
                            .clickable { onDeleteQuestionClick(qnaId) }
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_qna_answer),
                        contentDescription = "QuestionDelete",
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterVertically)
                    )
                    if (answerText.isNullOrEmpty() || answerQnaId == null) {
                        DetailQnaTextField(
                            modifier = Modifier.padding(end = 4.dp),
                            value = text,
                            textFieldColor = KoinTheme.colors.primary300,
                            onValueChange = { text = it },
                            onSendClick = {
                                if (text.isNotEmpty()) {
                                    onAddAnswerClick(qnaId, text)
                                }
                            }
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
                                        onDeleteAnswerClick(answerQnaId)
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
    val id = 0
    val content = ""
    DetailQnaBox(
        qnaId = 1,
        questionText = "추가 모집 공고는 언제 올라오나요",
        createdDate = "2025.00.00. 00:00",
        onDeleteQuestionClick = {},
        onDeleteAnswerClick = {},
        onAddAnswerClick = { id, content -> }
    )
}

@Preview
@Composable
fun DetailQnaBoxNoAnswerQnaEditable() {
    val id = 0
    val content = ""
    DetailQnaBox(
        qnaId = 1,
        questionText = "추가 모집 공고는 언제 올라오나요",
        createdDate = "2025.00.00. 00:00",
        onDeleteQuestionClick = {},
        onDeleteAnswerClick = {},
        onAddAnswerClick = { id, content -> },
        isQnaEditable = true
    )
}

@Preview
@Composable
fun DetailQnaBoxNoAnswerAllEditable() {
    val id = 0
    val content = ""
    DetailQnaBox(
        qnaId = 1,
        questionText = "추가 모집 공고는 언제 올라오나요",
        createdDate = "2025.00.00. 00:00",
        onDeleteQuestionClick = {},
        onDeleteAnswerClick = {},
        onAddAnswerClick = { id, content -> },
        isQnaEditable = true,
        isAnswerEditable = true
    )
}

@Preview
@Composable
fun DetailQnaBoxAnswer() {
    val id = 0
    val content = ""
    DetailQnaBox(
        qnaId = 1,
        questionText = "추가 모집 공고는 언제 올라오나요",
        createdDate = "2025.00.00. 00:00",
        onDeleteQuestionClick = {},
        onDeleteAnswerClick = {},
        onAddAnswerClick = { id, content -> },
        answerText = "올렸습니다!",
        answerQnaId = 2

    )
}

@Preview
@Composable
fun DetailQnaBoxAnswerQnaEditable() {
    val id = 0
    val content = ""
    DetailQnaBox(
        qnaId = 1,
        questionText = "추가 모집 공고는 언제 올라오나요",
        createdDate = "2025.00.00. 00:00",
        onDeleteQuestionClick = {},
        onDeleteAnswerClick = {},
        onAddAnswerClick = { id, content -> },
        answerText = "올렸습니다!",
        answerQnaId = 2,
        isQnaEditable = true
    )
}

@Preview
@Composable
fun DetailQnaBoxAnswerAllEditable() {
    val id = 0
    val content = ""
    DetailQnaBox(
        qnaId = 1,
        questionText = "추가 모집 공고는 언제 올라오나요",
        createdDate = "2025.00.00. 00:00",
        onDeleteQuestionClick = {},
        onDeleteAnswerClick = {},
        onAddAnswerClick = { id, content -> },
        answerText = "올렸습니다!",
        answerQnaId = 2,
        isQnaEditable = true,
        isAnswerEditable = true
    )
}
