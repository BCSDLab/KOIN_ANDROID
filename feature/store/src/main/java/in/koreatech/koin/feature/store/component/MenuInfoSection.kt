package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.R
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
            style = RebrandKoinTheme.typography.bold20
        )
        Text(
            text = stringResource(R.string.menu_detail_option_price, price),
            style = RebrandKoinTheme.typography.bold20,
            color = RebrandKoinTheme.colors.primary500
        )
        Text(
            text = detail,
            style = RebrandKoinTheme.typography.regular12,
            color = RebrandKoinTheme.colors.neutral500
        )
    }
}

@Preview
@Composable
private fun MenuInfoSectionPreview() {
    MenuInfoSection(
        menuName = "메뉴명",
        price = 15000,
        detail = "메뉴 설명"
    )
}
