package `in`.koreatech.business.feature.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorTextField


@Composable
fun MenuScreen() {
    val list = listOf("Menu1", "Menu2", "Menu3")
    Column(modifier = Modifier.fillMaxSize()) {
        list.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = item, fontWeight = FontWeight(500))
                    Text(text = stringResource(R.string.price), color = ColorPrimary)
                }
                Image(
                    modifier = Modifier
                        .width(68.dp)
                        .height(68.dp),
                    painter = painterResource(id = R.drawable.ic_koin_logo),
                    contentDescription = stringResource(R.string.menu_default_image),
                )
            }
            Divider(
                color = ColorTextField,
                modifier = Modifier
                    .width(327.dp)
                    .height(1.dp)
            )
        }
    }
}

