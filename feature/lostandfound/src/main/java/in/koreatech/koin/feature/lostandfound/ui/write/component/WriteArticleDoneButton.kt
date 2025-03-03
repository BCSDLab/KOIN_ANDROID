package `in`.koreatech.koin.feature.lostandfound.ui.write.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R

@Composable
fun WriteArticleDoneButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    HorizontalDivider(
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth(),
        color = KoinTheme.colors.neutral100,
    )
    Box(
        modifier =
            modifier
                .padding(24.dp)
                .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = onClick,
            modifier =
                Modifier
                    .padding(vertical = 8.dp, horizontal = 24.dp)
                    .width(160.dp)
                    .height(38.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KoinTheme.colors.primary600),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                style = KoinTheme.typography.regular14,
                text = stringResource(id = R.string.write_done),
            )
        }
    }
}
