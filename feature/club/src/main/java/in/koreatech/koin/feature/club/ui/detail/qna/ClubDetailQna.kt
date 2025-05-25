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
import `in`.koreatech.koin.domain.model.club.ClubQnasInfo
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.ui.detail.component.button.DetailButton
import `in`.koreatech.koin.feature.club.ui.detail.component.dialog.DetailDialog
import `in`.koreatech.koin.feature.club.ui.detail.component.dialog.content.DetailDialogAddQnaContent
import `in`.koreatech.koin.feature.club.ui.detail.component.qnabox.DetailQnaBox

@Composable
fun ClubDetailQna(
    modifier: Modifier = Modifier,
    qnaList: List<ClubQnasInfo.Qna>?,
    isManager: Boolean,
    userId: Int?,
    onAddQuestionClick: (String) -> Unit,
    onDeleteQuestionClick: (Int) -> Unit,
    onDeleteAnswerClick: (Int) -> Unit,
    onAddAnswerClick: (Int, String) -> Unit
) {
    var showAddQnaDialog by remember { mutableStateOf(false) }
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
        if (showAddQnaDialog) {
            var addQnaText by remember { mutableStateOf("") }

            DetailDialog(
                modifier = Modifier,
                title = stringResource(R.string.detail_add_qna_button),
                onPositive = {
                    if (!addQnaText.isEmpty()) {
                        onAddQuestionClick(addQnaText)
                        showAddQnaDialog = false
                    }
                },
                onNegative = { showAddQnaDialog = false },
                content = {
                    DetailDialogAddQnaContent(
                        text = addQnaText,
                        onValueChange = { addQnaText = it }
                    )
                }
            )
        }
        userId?.let {
            Row {
                Spacer(Modifier.weight(1f))
                DetailButton(
                    modifier = Modifier
                        .padding(
                            bottom = 4.dp
                        ),
                    text = stringResource(R.string.detail_add_qna_button),
                    onClick = {
                        showAddQnaDialog = true
                    },
                    contentPadding = PaddingValues(horizontal = 22.dp, vertical = 5.dp)
                )
            }
        }
        qnaList?.forEach {
            DetailQnaBox(
                qnaId = it.id,
                questionText = it.content,
                createdDate = it.createdAt,
                answerQnaId = it.children.firstOrNull()?.id,
                answerText = it.children.firstOrNull()?.content,
                onDeleteQuestionClick = onDeleteQuestionClick,
                onDeleteAnswerClick = onDeleteAnswerClick,
                onAddAnswerClick = onAddAnswerClick,
                isQnaEditable = isManager || (userId == it.authorId),
                isAnswerEditable = isManager
            )
        }
    }
}
