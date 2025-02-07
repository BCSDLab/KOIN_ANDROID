package `in`.koreatech.business.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.core.R
import `in`.koreatech.business.ui.theme.Gray3
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.business.ui.theme.ColorPrimary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NullSettingTime(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    sheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        ),
    updateIsSettingScreenState: (Boolean) -> Unit = {},
) {
    Column(
        modifier = modifier
    ){
        Divider(
            modifier = Modifier
                .fillMaxWidth(),
            color = Gray3,
            thickness = 0.5.dp
        )

        Text(
            modifier = Modifier
                .padding(vertical = 135.dp)
                .fillMaxWidth()
            ,
            text = "등록한 운영시간이 없습니다.",
            style = KoinTheme.typography.medium16,
            textAlign = TextAlign.Center
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth(),
            color = Gray3,
            thickness = 0.5.dp
        )

        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .clickable {
                    updateIsSettingScreenState(true)
                }
            ,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = "설정시간 추가",
                style = KoinTheme.typography.medium16,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(R.drawable.fi_plus),
                contentDescription = "플러스버튼"
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth(),
            color = Gray3,
            thickness = 0.5.dp
        )
    }

    Row(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 36.dp)
            .fillMaxWidth()
        ,
        horizontalArrangement = Arrangement.Center
    ){
        Button(
            onClick = {
                updateIsSettingScreenState(false)
                coroutineScope.launch {
                    sheetState.hide()
                }
            },
            colors = ButtonDefaults.buttonColors(Color.White),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Gray3),
            modifier = Modifier
                .height(44.dp)
                .width(128.dp)

        ) {
            Text(
                text = stringResource(id = R.string.cancel),
                style = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Gray3
                )
            )
        }

        Spacer(modifier = Modifier.width(32.dp))

        Button(
            onClick = {updateIsSettingScreenState(true)},
            colors = ButtonDefaults.buttonColors(ColorPrimary),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, ColorPrimary),
            modifier = Modifier
                .height(44.dp)
                .width(128.dp)
        ) {
            Text(
                text = "등록하기",
                style = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            )
        }
    }
}