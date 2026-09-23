package `in`.koreatech.koin.feature.lostandfound.ui.write.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.lostandfound.R

@Composable
fun WriteArticleAddItemButton(
    modifier: Modifier = Modifier,
    onItemAdd: () -> Unit = {}
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = onItemAdd,
            colors = ButtonDefaults.buttonColors(
                containerColor = RebrandKoinTheme.colors.neutral0
            ),
            border = BorderStroke(1.dp, RebrandKoinTheme.colors.primary500),
            shape = RebrandKoinTheme.shapes.small,
            contentPadding = PaddingValues(12.dp, 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_item_add),
                    contentDescription = stringResource(id = R.string.add_item),
                    modifier = Modifier.size(20.dp),
                    tint = RebrandKoinTheme.colors.primary500
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    color = RebrandKoinTheme.colors.primary600,
                    style = RebrandKoinTheme.typography.regular14,
                    fontWeight = FontWeight.Medium,
                    text = stringResource(id = R.string.add_item)
                )
            }
        }
    }
}
