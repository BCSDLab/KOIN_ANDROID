package `in`.koreatech.business.feature.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorPrimary

@Composable
fun OwnerStoreAppBar(
    title: String,
    showDeleteUserDialog: () -> Unit = {}
) {
    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .height(62.dp)
            .background(ColorPrimary)
    ) {
        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(color = Color.White, fontSize = 18.sp)
        )
        IconButton(
            modifier =
            Modifier
                .align(Alignment.BottomEnd),
            onClick = {
                showDeleteUserDialog()
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = stringResource(R.string.delete_user),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}
