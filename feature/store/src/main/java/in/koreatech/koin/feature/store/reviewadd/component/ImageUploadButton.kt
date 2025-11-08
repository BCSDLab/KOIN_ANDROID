package `in`.koreatech.koin.feature.store.reviewadd.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.dashedBorder
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun ImageUploadButton(
    modifier: Modifier = Modifier,
    currentCount: Int = 0,
    maxCount: Int = 3,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .dashedBorder(
                color = RebrandKoinTheme.colors.neutral300,
                shape = RoundedCornerShape(size = 8.dp)
            )
            .background(RebrandKoinTheme.colors.neutral0)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_add_image),
                contentDescription = "",
                tint = RebrandKoinTheme.colors.neutral500,
                modifier = Modifier.size(38.dp)
            )

            BasicText(
                text = "$currentCount/$maxCount",
                style = RebrandKoinTheme.typography.medium14.copy(color = RebrandKoinTheme.colors.neutral500)
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun ImageUploadButtonPreview() {
    ImageUploadButton()
}
