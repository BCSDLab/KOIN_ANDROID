package `in`.koreatech.koin.feature.land.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
internal fun LandRentalTypeBadge(
    text: String,
    containerColor: Color,
    contentColor: Color
) {
    Text(
        text = text,
        modifier = Modifier
            .background(containerColor, RoundedCornerShape(50))
            .padding(horizontal = 8.dp),
        style = RebrandKoinTheme.typography.regular10.copy(fontWeight = FontWeight.Medium),
        color = contentColor
    )
}
