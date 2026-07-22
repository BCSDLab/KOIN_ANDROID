package `in`.koreatech.koin.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.home.R

@Composable
fun HomeIcon(
    imageVector: ImageVector,
    tint: Color,
    backgroundColor: Color,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    contentPadding: PaddingValues = PaddingValues(vertical = 8.dp, horizontal = 8.dp)
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

@Preview
@Composable
private fun HomeIconPreview() {
    HomeIcon(
        tint = RebrandKoinTheme.colors.primary500,
        backgroundColor = RebrandKoinTheme.colors.neutral100,
        imageVector = ImageVector.vectorResource(R.drawable.ic_home_bus_timetable),
        contentDescription = null
    )
}
