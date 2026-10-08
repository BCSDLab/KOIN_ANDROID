package `in`.koreatech.koin.feature.lostandfound.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.lostandfound.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginDialog(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onPositive: () -> Unit = {},
    onNegative: () -> Unit = {}
) {
    BasicAlertDialog(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = RebrandKoinTheme.colors.neutral0,
                shape = RebrandKoinTheme.shapes.extraSmall
            )
            .padding(horizontal = 32.dp, vertical = 24.dp),
        onDismissRequest = onNegative
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = RebrandKoinTheme.typography.medium18.copy(color = RebrandKoinTheme.colors.neutral600, textAlign = TextAlign.Center)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = RebrandKoinTheme.typography.regular14.copy(color = Color(0xFF8E8E8E))
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onNegative,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = RebrandKoinTheme.colors.neutral0
                    ),
                    border = BorderStroke(width = 1.dp, color = Color(0xFF8E8E8E)),
                    shape = RebrandKoinTheme.shapes.small,
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.lost_and_found_my_filter_can_use_logged_in_negative),
                        style = RebrandKoinTheme.typography.medium15
                    )
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onPositive,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RebrandKoinTheme.colors.primary500
                    ),
                    shape = RebrandKoinTheme.shapes.small,
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.lost_and_found_my_filter_can_use_logged_in_positive),
                        style = RebrandKoinTheme.typography.medium15
                    )
                }
            }
        }
    }
}
