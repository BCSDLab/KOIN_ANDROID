package `in`.koreatech.business.navigation

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun TestScreen(
    onSignUpComplete: () -> Unit = {}
) {
    Button(onClick =  onSignUpComplete ) {
        Text("임시 버튼")
    }
}