package `in`.koreatech.business.feature.forcrupdate

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.ui.theme.ColorPrimary600
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.util.GifImage
import `in`.koreatech.koin.core.R

@Composable
fun ForceUpdateScreen(
    goToSelectCategoryScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    val openDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPrimary600)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 18.dp)
                    .fillMaxWidth()
            ){
                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .padding(end = 29.dp)
                        .size(24.dp)
                        .clickable { onBackPressed() }

                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_exit),
                        contentDescription = "backArrow",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            GifImage(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .height(250.dp)
                    .fillMaxWidth()
                ,
                drawableId = R.drawable.koin_logo_gif
            )
        }

        Text(
            text = "코인을 사용하기 위해\n업데이트가 꼭 필요해요.",
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 46.dp)
        )

        ForceUpdateDialog(
            isShow = openDialog
        )

        Text(
            text = "코인 앱을 실행해 주셔서 감사합니다!\n코인을 사용하기 위해 아래 버튼을 눌러\n스토어에서 업데이트를 진행해 주세요.",
            fontSize = 14.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp)
        )

        Button(
            onClick = goToSelectCategoryScreen,
            colors = ButtonDefaults.buttonColors(ColorSecondary),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 100.dp)
                .padding(top = 72.dp)
                .height(48.dp)
        ) {
            Text(
                text = stringResource(id = R.string.force_update_do_update),
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Text(
            text = stringResource(id = R.string.force_update_already_update),
            fontSize = 12.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp)
                .clickable{
                    openDialog.value = true
                }
        )

        Text(
            text = stringResource(id = R.string.copyright_2024),
            fontSize = 12.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 103.dp)
        )
    }
}

@Preview
@Composable
fun PreviewForceUpdateScreen() {
    ForceUpdateScreen(

    )
}