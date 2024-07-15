package `in`.koreatech.business.feature.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorPrimary

@Composable
fun OwnerStoreAppBar(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorPrimary),
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = stringResource(R.string.back),
                colorFilter = ColorFilter.tint(Color.White),
            )
        }
        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(color = Color.White, fontSize = 18.sp),
        )
    }
}