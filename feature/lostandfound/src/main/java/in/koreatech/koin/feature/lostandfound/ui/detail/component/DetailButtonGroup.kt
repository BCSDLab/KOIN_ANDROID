package `in`.koreatech.koin.feature.lostandfound.ui.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R

@Composable
fun DetailButtonGroup(
    showDeleteButton: Boolean = false,
    modifier: Modifier = Modifier,
    onArticleListClick: () -> Unit,
    onDeleteArticleClick: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        DetailDialog(
            title = stringResource(R.string.detail_delete_dialog_title),
            titleStyle = KoinTheme.typography.medium14.copy(color = KoinTheme.colors.neutral600),
            onPositive = {
                EventLogger.logCampusClickEvent(
                    AnalyticsConstant.Label.LOST_AND_FOUND.FIND_USER_DELETE,
                    "확인"
                )
                onDeleteArticleClick()
                showDeleteDialog = false
            },
            onNegative = {
                showDeleteDialog = false
            }
        )
    }

    Row(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Button(
            contentPadding = PaddingValues(12.dp, 6.dp),
            onClick = onArticleListClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = KoinTheme.colors.neutral300,
                contentColor = KoinTheme.colors.neutral600
            ),
            shape = KoinTheme.shapes.extraSmall
        ) {
            Text(
                style = KoinTheme.typography.regular12,
                text = stringResource(R.string.detail_article_list_button)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (showDeleteButton) {
            Button(
                contentPadding = PaddingValues(10.dp, 6.dp),
                onClick = {
                    showDeleteDialog = true
                    EventLogger.logCampusClickEvent(
                        AnalyticsConstant.Label.LOST_AND_FOUND.FIND_USER_DELETE,
                        "삭제"
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = KoinTheme.colors.neutral300,
                    contentColor = KoinTheme.colors.neutral600
                ),
                shape = KoinTheme.shapes.extraSmall
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        style = KoinTheme.typography.regular12,
                        text = stringResource(R.string.detail_delete_button)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Image(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = R.drawable.ic_article_delete),
                        contentDescription = null
                    )
                }
            }
        }
    }
}
