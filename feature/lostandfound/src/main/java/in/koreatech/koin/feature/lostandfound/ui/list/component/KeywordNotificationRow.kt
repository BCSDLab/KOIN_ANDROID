package `in`.koreatech.koin.feature.lostandfound.ui.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.component.LostAndFoundTextChipScrollGroup
import `in`.koreatech.koin.feature.lostandfound.component.keywordChipColors
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun KeywordNotificationRow(
    keywords: ImmutableList<String>,
    selectedKeywordIndex: Int,
    onKeywordSelect: (Int) -> Unit,
    onSettingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val seeAllText = stringResource(R.string.keyword_see_all)
    val addNewText = stringResource(R.string.keyword_add_new)

    // keywords가 변경되었을 때만 리스트 재계산 및 인스턴스 유지
    val chipTexts = remember(keywords, seeAllText, addNewText) {
        val list = mutableListOf(seeAllText)
        if (keywords.isEmpty()) {
            list.add(addNewText)
        } else {
            list.addAll(keywords.map { "#$it" })
        }
        list.toPersistentList()
    }

    // 선택된 인덱스 리스트 인스턴스 유지
    val selectedIndices = remember(selectedKeywordIndex) {
        persistentListOf(selectedKeywordIndex)
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onSettingClick,
            modifier = Modifier.size(48.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(KoinTheme.colors.neutral100),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_keyword_notification_setting),
                    contentDescription = stringResource(R.string.keyword_notification_setting),
                    tint = Color.Unspecified
                )
            }
        }

        LostAndFoundTextChipScrollGroup(
            modifier = Modifier.weight(1f),
            titles = chipTexts,
            selectedChipIndexes = selectedIndices,
            onChipSelected = { index ->
                if (keywords.isEmpty() && index == 1) {
                    onSettingClick()
                } else {
                    onKeywordSelect(index)
                }
            },
            shape = RoundedCornerShape(50),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            chipColors = keywordChipColors()
        )
    }
}

@Preview(name = "키워드 없음", showBackground = true)
@Composable
private fun KeywordNotificationRowEmptyPreview() {
    KoinTheme {
        KeywordNotificationRow(
            keywords = persistentListOf(),
            selectedKeywordIndex = 0,
            onKeywordSelect = {},
            onSettingClick = {}
        )
    }
}

@Preview(name = "키워드 있음", showBackground = true)
@Composable
private fun KeywordNotificationRowWithKeywordsPreview() {
    KoinTheme {
        KeywordNotificationRow(
            keywords = persistentListOf("태블릿", "애플펜슬", "키링"),
            selectedKeywordIndex = 0,
            onKeywordSelect = {},
            onSettingClick = {}
        )
    }
}
