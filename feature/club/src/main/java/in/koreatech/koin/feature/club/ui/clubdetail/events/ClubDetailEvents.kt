package `in`.koreatech.koin.feature.club.ui.clubdetail.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.component.KoinClubEventsDropdown
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ClubDetailEvents(
    isDropdownExpanded: Boolean,
    modifier: Modifier = Modifier,
    showProgressBar: Boolean = false,
    onDropdownExpandChange: (Boolean) -> Unit = {},
    onItemSelected: (Int) -> Unit = {}
) {
    Box {
        if (showProgressBar) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                )
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(
                        top = 16.dp,
                        start = 24.dp,
                        end = 24.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    KoinClubEventsDropdown(
                        title = "행사 진행중",
                        isDropdownExpanded = isDropdownExpanded,
                        items = persistentListOf("최신 등록순", "행사 예정", "종료 행사"),
                        onDropdownExpandChange = onDropdownExpandChange
                    )
                }
                Text(text = "기존 메시지")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClubDetailEventsPreview() {
    ClubDetailEvents(
        isDropdownExpanded = false
    )
}
