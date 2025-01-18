package `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.util.getKoreanDayOfWeekShortName
import `in`.koreatech.koin.feature.lostandfound.component.LostItemTypeChip
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 분실물 아이템
 * @param lostOrFound 분실물/습득물
 * @param lostItemCategory 분실물 종류
 * @param location 습득 장소
 * @param date 습득 날짜
 * @param content 설명
 * @param author 작성자
 * @param foundDate 작성 날짜
 * @param modifier Modifier
 */
@Composable
fun LostAndFoundItem(
    lostOrFound: LostOrFoundType = LostOrFoundType.FOUND,
    lostItemCategory: LostItemCategory,
    foundPlace: String,
    content: String,
    author: String,
    foundDate: LocalDate,
    registeredAt: LocalDate,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) = Column(
    modifier = modifier.noRippleClickable {
        onClick()
    }
) {
    val registeredAtFormatType = DateTimeFormatter.ofPattern("MM.dd")
    val convertedRegisteredAt by remember { mutableStateOf("${registeredAt.format(registeredAtFormatType)} ${registeredAt.getKoreanDayOfWeekShortName()}")}

    val foundDateFormatType = DateTimeFormatter.ofPattern("yy.MM.dd")

    Column(
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 2.dp),
            text = stringResource(lostOrFound.stringRes),
            color = KoinTheme.colors.primary600,
            style = KoinTheme.typography.medium12.copy(fontWeight = FontWeight.SemiBold)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LostItemTypeChip(category = lostItemCategory)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$foundPlace | ${foundDate.format(foundDateFormatType)}",
                style = KoinTheme.typography.medium14
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content,
            style = KoinTheme.typography.regular12.copy(color = KoinTheme.colors.neutral800),
            maxLines = 1,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$author • $convertedRegisteredAt",
                color = KoinTheme.colors.neutral500,
                style = KoinTheme.typography.regular12
            )
        }
    }
    HorizontalDivider(color = KoinTheme.colors.neutral100)
}

@Preview
@Composable
fun LostAndFoundItemPreview() {
    KoinTheme {
        Surface {
            LostAndFoundItem(
                lostItemCategory = LostItemCategory.WALLET,
                foundPlace = "담헌실학관 2층",
                content = "에어팟이에요 투명 케이스가 끼워져 있었어요! 담헌실학관 401호에",
                author = "총학생회",
                foundDate = LocalDate.now(),
                registeredAt = LocalDate.now()
            )
        }
    }
}