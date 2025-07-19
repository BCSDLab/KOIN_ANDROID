package `in`.koreatech.koin.feature.user.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.dashedBorder
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

/**
 * Koin User Term
 * @param text Term text
 * @param modifier [Modifier]
 */
@Composable
fun KoinUserTerm(
    text: String,
    modifier: Modifier = Modifier
) = Box(modifier = modifier) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(KoinTheme.shapes.extraSmall)
            .dashedBorder(color = KoinTheme.colors.neutral400, shape = RectangleShape)
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        text = text,
        style = KoinTheme.typography.regular10
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewKoinUserTerm() {
    KoinUserTerm(
        text = "Lorem ipsum dolor sit amet"
    )
}
