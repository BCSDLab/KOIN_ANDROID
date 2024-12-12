package `in`.koreatech.bus.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R

@Composable
fun CommonEmptyView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.width(120.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_empty_bus),
            contentDescription = stringResource(R.string.no_result),
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            modifier = Modifier,
            text = stringResource(R.string.empty_description),
            style = KoinTheme.typography.bold15,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Preview
@Composable
private fun CommonFailureViewPreview() {
    CommonFailureView()
}
