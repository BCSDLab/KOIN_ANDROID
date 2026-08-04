package `in`.koreatech.koin.feature.department.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

private const val SKELETON_ITEM_COUNT = 4
private const val SKELETON_DURATION_MILLIS = 700

@Composable
internal fun DepartmentLoadingList(
    modifier: Modifier = Modifier,
    itemCount: Int = SKELETON_ITEM_COUNT
) {
    val transition = rememberInfiniteTransition(label = "department_skeleton")
    val alpha by transition.animateFloat(
        initialValue = .4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(SKELETON_DURATION_MILLIS),
            repeatMode = RepeatMode.Reverse
        ),
        label = "department_skeleton_alpha"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(itemCount) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .alpha(alpha)
                    .clip(RoundedCornerShape(16.dp))
                    .background(RebrandKoinTheme.colors.neutral50)
            ) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DepartmentLoadingListPreview() {
    RebrandKoinTheme {
        DepartmentLoadingList()
    }
}
