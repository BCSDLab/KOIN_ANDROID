package `in`.koreatech.koin.feature.store.review.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.feature.store.R

@Composable
fun ReviewCheckbox(
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Image(
        modifier = modifier
            .noRippleClickable {
                onCheckedChange(!checked)
            }
            .size(20.dp)
            .padding(1.dp)
            .semantics {
                this.role = Role.Checkbox
            },
        imageVector = if (checked) {
            ImageVector.vectorResource(R.drawable.ic_store_checkbox_checked)
        } else {
            ImageVector.vectorResource(R.drawable.ic_store_checkbox_unchecked)
        },
        contentDescription = "Checkbox"
    )
}
