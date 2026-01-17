package `in`.koreatech.koin.feature.lostandfound.write.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
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
            colors =
            ButtonDefaults.buttonColors(
                containerColor = KoinTheme.colors.info200
            ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(12.dp, 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_item_add),
                    contentDescription = stringResource(id = R.string.add_item),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    color = KoinTheme.colors.primary600,
                    style = KoinTheme.typography.regular14,
                    fontWeight = FontWeight.Medium,
                    text = stringResource(id = R.string.add_item)
                )
            }
        }
    }
}
