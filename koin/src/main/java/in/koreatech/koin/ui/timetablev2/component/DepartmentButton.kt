package `in`.koreatech.koin.ui.timetablev2.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.compose.ui.ColorPrimary

@Composable
fun DepartmentButton(
    modifier: Modifier = Modifier,
    onCompleted: () -> Unit,
) {
    Box(modifier = modifier) {
        Button(
            onClick = onCompleted,
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(ColorPrimary),
            contentPadding = PaddingValues(
                horizontal = 14.dp
            ),
            modifier = Modifier
                .height(26.dp)
        ) {
            Text(
                text = "선택완료",
                color = Color.White,
                fontSize = 15.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DepartmentPreview() {
    DepartmentButton(
        onCompleted = {}
    )
}