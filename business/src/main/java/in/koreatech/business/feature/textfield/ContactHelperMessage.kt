package `in`.koreatech.business.feature.textfield

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.Gray500
import `in`.koreatech.koin.data.constant.URLConstant.CONTACT_URL

@Composable
fun ContactHelperMessage(errorText: String) {
    val uriHandler = LocalUriHandler.current
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = errorText,
            fontSize = 16.sp,
            color = Gray500,
        )
        Text(
            modifier =
                Modifier
                    .padding(end = 8.dp)
                    .clickable {
                        uriHandler.openUri(CONTACT_URL)
                    },
            text = stringResource(id = R.string.contact),
            fontSize = 16.sp,
            color = ColorPrimary,
        )
    }
}
