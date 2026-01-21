package `in`.koreatech.koin.feature.lostandfound.ui.list.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.component.LostItemTypeChip
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import `in`.koreatech.koin.feature.lostandfound.ui.detail.component.LostAndFoundStatusChip
import `in`.koreatech.koin.feature.lostandfound.util.formatLostAndFoundTitle
import `in`.koreatech.koin.feature.lostandfound.util.getKoreanMMddEForm
import java.time.LocalDate

/**
 * 분실물 아이템
 * @param articleId 개시글 ID
 * @param lostOrFound 분실물/습득물
 * @param lostItemCategory 분실물 종류
 * @param foundPlace 습득 장소
 * @param foundDate 습득 날짜
 * @param isFound 찾음 상태
 * @param content 설명
 * @param author 작성자
 * @param registeredAt 작성 날짜
 * @param modifier Modifier
 */
@Composable
fun ListItem(
    articleId: Int,
    lostOrFound: LostOrFoundType,
    lostItemCategory: LostItemCategory,
    foundPlace: String,
    content: String,
    author: String,
    isReported: Boolean,
    isFound: Boolean,
    foundDate: LocalDate,
    registeredAt: LocalDate,
    modifier: Modifier = Modifier,
    onArticleClick: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onArticleClick(articleId) }
            .padding(vertical = 12.dp, horizontal = 24.dp)
    ) {
        Text(
            text = if (lostOrFound == LostOrFoundType.LOST) stringResource(R.string.lost_item) else stringResource(R.string.found_item),
            style = KoinTheme.typography.bold12.copy(
                fontWeight = FontWeight.SemiBold,
                color = KoinTheme.colors.primary600
            )
        )

        Spacer(Modifier.height(2.dp))

        if (isReported) {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.ic_article_report),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.article_reported),
                    color = KoinTheme.colors.neutral500,
                    style = KoinTheme.typography.regular14
                )
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LostItemTypeChip(category = lostItemCategory)

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = formatLostAndFoundTitle(foundPlace = foundPlace, foundDate = foundDate),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = KoinTheme.typography.medium14.copy(
                        color = Color.Black
                    ),
                    modifier = Modifier.weight(1f)
                )
                LostAndFoundStatusChip(isFound = isFound)
            }

            Spacer(Modifier.height(4.dp))

            if (content.isNotEmpty()) {
                Text(
                    text = content,
                    color = KoinTheme.colors.neutral800,
                    style = KoinTheme.typography.regular12,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
        Spacer(Modifier.height(4.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            ProvideTextStyle(
                value = KoinTheme.typography.regular12.copy(color = KoinTheme.colors.neutral500)
            ) {
                Text(text = author)
                Text(text = "•", color = KoinTheme.colors.neutral400)
                Text(text = registeredAt.getKoreanMMddEForm())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListItemPreview() {
    ListItem(
        articleId = -1,
        lostOrFound = LostOrFoundType.FOUND,
        lostItemCategory = LostItemCategory.WALLET,
        foundPlace = "담헌실학관 2층",
        content = "에어팟이에요 투명 케이스가 끼워져 있었어요! 담헌실학관 401호에",
        author = "총학생회",
        isReported = false,
        isFound = false,
        foundDate = LocalDate.now(),
        registeredAt = LocalDate.now(),
        onArticleClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ListItemPreviewReported() {
    ListItem(
        articleId = -1,
        lostOrFound = LostOrFoundType.FOUND,
        lostItemCategory = LostItemCategory.WALLET,
        foundPlace = "담헌실학관 2층",
        content = "에어팟이에요 투명 케이스가 끼워져 있었어요! 담헌실학관 401호에",
        author = "총학생회",
        isReported = true,
        isFound = false,
        foundDate = LocalDate.now(),
        registeredAt = LocalDate.now(),
        onArticleClick = {}
    )
}
