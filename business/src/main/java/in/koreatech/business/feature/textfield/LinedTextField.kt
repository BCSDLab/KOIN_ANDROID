package `in`.koreatech.business.feature.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.search.SearchBar
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorHelper
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.ColorTextField


@Composable
fun LinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    textStyle: TextStyle = TextStyle.Default.copy(fontSize = 15.sp),
    isPassword: Boolean = false,
    helperText: String = "",
    errorText: String = "",
    isError: Boolean = false,
) {

    OutlinedTextField(
        modifier = modifier
            .height(40.dp).fillMaxWidth()
            .background(color = ColorTextField)
            .border(
                shape = RoundedCornerShape(4.dp),
                width = 1.dp,
                color = if (isError) ColorSecondary else ColorTextField,
            ),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = label, fontSize = 15.sp, color = ColorHelper)
        },
        isError = isError,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
    )
    Box {
        Text(
            modifier = Modifier,
            text = helperText,
            fontSize = 11.sp,
            color = ColorHelper,
        )
        if (isError) Text(text = errorText, fontSize = 11.sp, color = ColorSecondary)

    }
}
