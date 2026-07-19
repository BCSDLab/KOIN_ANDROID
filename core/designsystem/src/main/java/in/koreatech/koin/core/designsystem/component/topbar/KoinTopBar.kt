package `in`.koreatech.koin.core.designsystem.component.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable

@Composable
fun KoinTopBar(
    leftIcon: ImageVector,
    middleIcon: ImageVector,
    rightIcon: ImageVector,
    modifier: Modifier = Modifier,
    leftIconContentDescription: String? = null,
    middleIconContentDescription: String? = null,
    rightIconContentDescription: String? = null,
    contentPadding: PaddingValues = PaddingValues(vertical = 8.dp, horizontal = 24.dp),
    onRightClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = leftIcon,
            contentDescription = leftIconContentDescription
        )

        Image(
            imageVector = middleIcon,
            contentDescription = middleIconContentDescription
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = Modifier.noRippleClickable(onClick = onRightClick),
            imageVector = rightIcon,
            contentDescription = rightIconContentDescription
        )
    }
}

@Preview
@Composable
private fun KoinTopBarPreview() {
    KoinTopBar(
        leftIcon = Icons.Default.Home,
        middleIcon = Icons.Default.Menu,
        rightIcon = Icons.Default.Search
    )
}
