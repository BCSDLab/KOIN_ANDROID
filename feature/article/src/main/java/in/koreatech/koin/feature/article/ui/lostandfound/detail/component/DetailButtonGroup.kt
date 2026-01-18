package `in`.koreatech.koin.feature.article.ui.lostandfound.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.article.R

@Composable
fun DetailButtonGroup(
    modifier: Modifier = Modifier,
    showDeleteButton: Boolean = false,
    showDeleteDialog: Boolean = false,
    isLoggedIn: Boolean = false,
    isAuthorWithdraw: Boolean = false,
    onShowDeleteDialogChange: (Boolean) -> Unit = {},
    onArticleListClick: () -> Unit = {},
    onDeleteArticleClick: () -> Unit = {},
    onEditArticleClick: () -> Unit = {},
    onChatRoomClick: () -> Unit = {},
    onReportArticleClick: () -> Unit = {}
) {
    if (showDeleteDialog) {
        DetailDialog(
            title = stringResource(R.string.detail_delete_dialog_title),
            titleStyle = KoinTheme.typography.medium14.copy(color = KoinTheme.colors.neutral600),
            onPositive = {
                EventLogger.logCampusClickEvent(
                    AnalyticsConstant.Label.LostAndFound.FIND_USER_DELETE_CONFIRM,
                    "확인"
                )
                onDeleteArticleClick()
                onShowDeleteDialogChange(false)
            },
            onNegative = {
                onShowDeleteDialogChange(false)
            }
        )
    }

    Row(
        modifier = modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
            .fillMaxWidth()
    ) {
        Button(
            modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
            contentPadding = PaddingValues(12.dp, 6.dp),
            onClick = onArticleListClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = KoinTheme.colors.neutral300,
                contentColor = KoinTheme.colors.neutral600
            ),
            shape = KoinTheme.shapes.extraSmall
        ) {
            Text(
                modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                style = KoinTheme.typography.regular12,
                text = stringResource(R.string.detail_article_list_button)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (showDeleteButton) {
            Button(
                modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                contentPadding = PaddingValues(10.dp, 6.dp),
                onClick = onEditArticleClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = KoinTheme.colors.neutral300,
                    contentColor = KoinTheme.colors.neutral600
                ),
                shape = KoinTheme.shapes.extraSmall
            ) {
                Text(
                    style = KoinTheme.typography.regular12,
                    text = stringResource(R.string.detail_edit_button)
                )
            }

            Button(
                modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                contentPadding = PaddingValues(10.dp, 6.dp),
                onClick = {
                    onShowDeleteDialogChange(true)
                    EventLogger.logCampusClickEvent(
                        AnalyticsConstant.Label.LostAndFound.FIND_USER_DELETE,
                        "삭제"
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = KoinTheme.colors.neutral300,
                    contentColor = KoinTheme.colors.neutral600
                ),
                shape = KoinTheme.shapes.extraSmall
            ) {
                Text(
                    modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                    style = KoinTheme.typography.regular12,
                    text = stringResource(R.string.detail_delete_button)
                )
            }
        } else {
            if (isLoggedIn) {
                if (!isAuthorWithdraw) {
                    Button(
                        modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                        contentPadding = PaddingValues(10.dp, 6.dp),
                        onClick = onChatRoomClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = KoinTheme.colors.neutral300,
                            contentColor = KoinTheme.colors.neutral600
                        ),
                        shape = KoinTheme.shapes.extraSmall
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = R.drawable.ic_chat),
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                style = KoinTheme.typography.regular12,
                                text = stringResource(R.string.detail_chat_room_button)
                            )
                        }
                    }
                }

                Button(
                    modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                    contentPadding = PaddingValues(10.dp, 6.dp),
                    onClick = onReportArticleClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = KoinTheme.colors.neutral300,
                        contentColor = KoinTheme.colors.neutral600
                    ),
                    shape = KoinTheme.shapes.extraSmall
                ) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.ic_article_report),
                        contentDescription = null
                    )
                }
            }
        }
    }
}
