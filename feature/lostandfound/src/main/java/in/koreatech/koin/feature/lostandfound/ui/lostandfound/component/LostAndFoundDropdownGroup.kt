package `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.component.Dropdown

@Composable
fun LostAndFoundDropdownGroup(
    modifier: Modifier = Modifier,
    onItemSelected: (Int) -> Unit
) = Box(
    modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)
) {
    Dropdown(
        title = stringResource(R.string.dropdown_type_all),
        items = arrayOf("모두보기", "습득물", "분실물"),
        modifier = Modifier.align(Alignment.CenterEnd),
        onItemSelected = onItemSelected
    )
}
