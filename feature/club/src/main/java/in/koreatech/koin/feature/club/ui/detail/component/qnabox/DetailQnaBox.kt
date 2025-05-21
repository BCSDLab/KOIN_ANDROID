package `in`.koreatech.koin.feature.club.ui.detail.component.qnabox

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
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
    questionText: String,
    createdDate: String,
    onQuestionDeleteClick: () -> Unit,
    onAnswerDeleteClick: () -> Unit,
    answerText: String? = null,
) {
    var text by remember { mutableStateOf("") }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp
            )
            .background(KoinTheme.colors.neutral0)
            .border(
                width = 1.dp,
                color = KoinTheme.colors.neutral300,
                shape = RoundedCornerShape(size = 4.dp)
            )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 8.dp,
                    vertical = 8.dp
                ),
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
            ){
                Text(
                    text = "Q. $questionText",
                    style = KoinTheme.typography.regular18,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                )
                Image(
                    painter = painterResource(id = R.drawable.fi_x),
                    contentDescription = "QuestionDelete",
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 4.dp)
                        .clickable { onQuestionDeleteClick() }
                )
            }
            Spacer(Modifier.height(4.dp))

            Text(
                text = createdDate,
                style = KoinTheme.typography.regular14,
                color = KoinTheme.colors.neutral600
            )
            Spacer(Modifier.height(8.dp))

            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
            ){
                Image(
                    painter = painterResource(id = R.drawable.u_corner_down_right),
                    contentDescription = "QuestionDelete",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically),
                )
                if(answerText.isNullOrEmpty()) {
                    DetailQnaTextField(
                        value = text,
                        onValueChange = { text = it },
                        onButtonClick = {}
                    )
                }
                else {
                    Text(
                        text = "$answerText",
                        style = KoinTheme.typography.regular18,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.fi_x),
                        contentDescription = "QuestionDelete",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 4.dp)
                            .clickable { onAnswerDeleteClick() }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun DetailQnaBoxNoAnswer() {
    DetailQnaBox(
        questionText = "추가 모집 공고는 언제 올라오나요",
        createdDate = "2025.00.00. 00:00",
        onQuestionDeleteClick = {},
        onAnswerDeleteClick = {}
    )
}
@Preview
@Composable
fun DetailQnaBoxAnswer() {
    DetailQnaBox(
        questionText = "추가 모집 공고는 언제 올라오나요",
        createdDate = "2025.00.00. 00:00",
        onQuestionDeleteClick = {},
        onAnswerDeleteClick = {},
        answerText = "올렸습니다!"
    )
}