package `in`.koreatech.business.feature.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorTextField


@Composable
fun EventScreen() {
    val list = listOf("Menu1", "Menu2", "Menu3", "d", "sd", "sd", "sd", "sd")
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorTextField,
                    contentColor = Color.Black
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "편집하기"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "편집하기")
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorTextField,
                    contentColor = Color.Black
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_add_box),
                    contentDescription = "편집하기"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "추가하기")
            }
        }

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
                    Text(text = "가격", color = ColorPrimary)
                }
                Image(
                    modifier = Modifier
                        .width(68.dp)
                        .height(68.dp),
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "메뉴이미지",
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