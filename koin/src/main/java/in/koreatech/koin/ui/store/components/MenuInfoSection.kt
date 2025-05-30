package `in`.koreatech.koin.ui.store.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun MenuInfoSection(
    menuName: String,
    price: Int,
    detail: String
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
        Text(
            text = menuName,
            style = KoinTheme.typography.bold20,
            fontSize = 20.sp
        )
        Text(
            text = "${price}원",
            style = KoinTheme.typography.bold20,
            fontSize = 20.sp,
            color = RebrandKoinTheme.colors.primary500
        )
        Text(
            text = detail,
            style = KoinTheme.typography.regular12,
            fontSize = 12.sp,
            color = KoinTheme.colors.neutral500
        )
    }
}

@Preview
@Composable
fun MenuInfoSectionPreview() {
    MenuInfoSection(
        menuName = "메뉴명",
        price = 15000,
        detail = "메뉴 설명"
    )
}
