package `in`.koreatech.business.feature.insertstore.startinsetstore

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.core.R
import `in`.koreatech.business.ui.theme.Blue1
import `in`.koreatech.business.ui.theme.ColorPrimary

@Composable
fun StartInsertScreen(
    modifier: Modifier = Modifier,
    goToSelectCategoryScreen: () -> Unit,
    onBackPressed: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = modifier
                .padding(top = 56.dp, start = 10.dp , bottom = 18.dp)
                .width(40.dp)
                .height(40.dp)
                .clickable { onBackPressed() }

        ) {
            Image(
                painter = painterResource(R.drawable.ic_arrow_left),
                contentDescription = "backArrow",
                modifier = modifier
                    .width(40.dp)
                    .height(40.dp)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_edit),
            contentDescription = "finish_mark",
            alignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 103.dp, bottom = 30.dp)
                .height(55.dp)
                .width(55.dp)
        )

        Text(
            text = stringResource(R.string.insert_store_info),
            fontSize = 24.sp,
            color = ColorPrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 46.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = stringResource(R.string.insert_store_guide),
            fontSize = 16.sp,
            color = Blue1,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 46.dp)
                .padding(bottom = 51.dp)
        )

        Button(
            onClick = goToSelectCategoryScreen,
            colors = ButtonDefaults.buttonColors(ColorPrimary),
            shape = RectangleShape,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 33.dp)
                .height(44.dp)
        ) {
            Text(
                text = stringResource(id = R.string.insert_store_start),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}


@Preview
@Composable
fun PreviewStartInsertScreen(){
    StartInsertScreen(
        modifier = Modifier,
        goToSelectCategoryScreen = {} ,
        onBackPressed = {}
    )
}