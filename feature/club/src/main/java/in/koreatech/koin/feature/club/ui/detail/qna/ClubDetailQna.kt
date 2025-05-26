package `in`.koreatech.koin.feature.club.ui.detail.qna

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.domain.model.club.ClubQnasInfo
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.ui.detail.component.qnabox.DetailQnaBox

//TODO QNA 로딩중 progrssbar 추가
@Composable
fun ClubDetailQna(
    qnaList: List<ClubQnasInfo.Qna>?,
    modifier: Modifier = Modifier,
    userId: Int? = null,
    isManager: Boolean = false,
    onDeleteQnaClick: (Int) -> Unit = {},
    onAddQnaClick: () -> Unit = {},
    onAddAnswerClick: (Int, String) -> Unit = { _, _ -> }
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .height(800.dp)
            .verticalScroll(scrollState)
            .padding(
                top = 16.dp,
                start = 24.dp,
                end = 24.dp
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        userId?.let {
            Row {
                Spacer(Modifier.weight(1f))
                FilledButton(
                    modifier = Modifier
                        .padding(
                            bottom = 4.dp
                        ),
                    text = stringResource(R.string.detail_add_qna_button),
                    onClick = onAddQnaClick,
                    contentPadding = PaddingValues(horizontal = 22.dp, vertical = 5.dp)
                )
            }
        }
        qnaList?.forEach {
            var addAnswerText by remember { mutableStateOf("") }
            DetailQnaBox(
                qnaId = it.id,
                questionText = it.content,
                createdDate = it.createdAt,
                addAnswerText = addAnswerText,
                onAddAnswerTextChange = { addAnswerText = it },
                onDeleteQnaClick = onDeleteQnaClick,
                onAddAnswerClick = onAddAnswerClick,
                answerQnaId = it.children.firstOrNull()?.id,
                answerText = it.children.firstOrNull()?.content,
                isQnaEditable = isManager || (userId == it.authorId),
                isAnswerEditable = isManager
            )
        }
    }
}
