package `in`.koreatech.koin.feature.lostandfound.ui.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.component.LostItemTypeChip
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import `in`.koreatech.koin.feature.lostandfound.util.getKoreanDayOfWeekShortName
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DetailHeader(
    lostOrFound: LostOrFoundType,
    category: LostItemCategory,
    foundPlace: String,
    foundDate: LocalDate,
    author: String,
    registeredAt: LocalDate,
    modifier: Modifier = Modifier,
) {
    val registeredAtFormatType = DateTimeFormatter.ofPattern("MM.dd")
    val convertedRegisteredAt = "${registeredAt.format(registeredAtFormatType)} ${registeredAt.getKoreanDayOfWeekShortName()}"

    val foundDateFormatType = DateTimeFormatter.ofPattern("yy.MM.dd")

    Column(
        modifier = modifier.padding(vertical = 12.dp, horizontal = 24.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 2.dp),
            text = stringResource(lostOrFound.stringRes),
            color = KoinTheme.colors.primary600,
            fontWeight = FontWeight(600),
            style = KoinTheme.typography.medium12
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            LostItemTypeChip(category = category)
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "$foundPlace | ${foundDate.format(foundDateFormatType)}",
                fontWeight = FontWeight(500),
                style = KoinTheme.typography.medium14
            )
        }
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
}