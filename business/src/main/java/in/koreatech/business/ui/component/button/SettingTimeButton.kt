package `in`.koreatech.business.ui.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.Gray3
import `in`.koreatech.koin.core.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SettingTimeButton(
    modifier: Modifier = Modifier,
    onCancelButtonClicked: () -> Unit = {},
    onRegisterButtonClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .padding(top = 12.dp, bottom = 36.dp)
            .fillMaxWidth()
        ,
        horizontalArrangement = Arrangement.Center
    ){
        Button(
            onClick = onCancelButtonClicked,
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
            onClick = onRegisterButtonClicked,
            colors = ButtonDefaults.buttonColors(ColorPrimary),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, ColorPrimary),
            modifier = Modifier
                .height(44.dp)
                .width(128.dp)
        ) {
            Text(
                text = stringResource(R.string.register),
                style = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            )
        }
    }
}