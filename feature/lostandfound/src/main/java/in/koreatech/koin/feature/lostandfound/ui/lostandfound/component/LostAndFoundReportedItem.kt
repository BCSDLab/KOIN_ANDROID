package `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import `in`.koreatech.koin.feature.lostandfound.util.getKoreanDayOfWeekShortName
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun LostAndFoundReportedItem(
    author: String,
    registeredAt: LocalDate,
    modifier: Modifier = Modifier,
    lostOrFound: LostOrFoundType = LostOrFoundType.FOUND,
    ) = Column(modifier = modifier) {
    val registeredAtFormatType = DateTimeFormatter.ofPattern("MM.dd")
    val convertedRegisteredAt by remember {
        mutableStateOf(
            "${
                registeredAt.format(
                    registeredAtFormatType
                )
            } ${registeredAt.getKoreanDayOfWeekShortName()}"
        )
    }

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
            Image(
                painter = painterResource(id = R.drawable.ic_reported_item),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.reported_item_title),
                color = KoinTheme.colors.neutral600,
                style = KoinTheme.typography.regular14
            )
        }
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

@Preview(showBackground = true)
@Composable
fun PreviewLostAndFoundReportedItem() {
    KoinTheme {
        LostAndFoundReportedItem(
            author = "작성자",
            registeredAt = LocalDate.now()
        )
    }
}