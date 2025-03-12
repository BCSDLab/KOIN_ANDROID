package `in`.koreatech.business.feature.textfield

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorHelper
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.ColorSuccess

@Composable
fun HelperMessage(
    helperText: String,
    isError: Boolean,
    isSuccess: Boolean,
    errorText: String,
    successText: String,
    focused: Boolean,
) {
    Box(modifier = Modifier.padding(start = 8.dp, top = 4.dp)) {
        Text(
            text = if (isError) "" else helperText,
            fontSize = 16.sp,
            color = ColorHelper,
        )
        if (isError) {
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_error),
                    contentDescription = stringResource(id = R.string.error),
                    tint = ColorSecondary,
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = errorText,
                    fontSize = 16.sp,
                    color = ColorSecondary,
                )
            }
        } else if (isSuccess && focused) {
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_success),
                    contentDescription = stringResource(id = R.string.success),
                    tint = ColorSuccess,
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = successText,
                    fontSize = 16.sp,
                    color = ColorSuccess,
                )
            }
        }
    }
}
