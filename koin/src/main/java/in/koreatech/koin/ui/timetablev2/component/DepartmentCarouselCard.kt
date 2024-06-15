package `in`.koreatech.koin.ui.timetablev2.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.compose.ui.ColorMain400
import `in`.koreatech.koin.compose.ui.ColorPrimary
import `in`.koreatech.koin.compose.ui.ColorPrimaryMain400_ALPAH10
import `in`.koreatech.koin.domain.model.timetable.Department

@Composable
fun DepartmentCarouselCard(
    department: Department,
    modifier: Modifier = Modifier,
    onCancel: (Department) -> Unit,
) {
    Card(
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.border(1.dp, ColorMain400, RoundedCornerShape(16.dp)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.White)
                .wrapContentSize()
                .padding(
                    vertical = 2.dp,
                    horizontal = 6.dp
                )
        ) {
            Text(
                text = department.name,
                fontSize = 12.sp,
                color = ColorMain400
            )
            Spacer(modifier = Modifier.width(2.dp))
            Box(
                modifier = Modifier.clickable {
                    onCancel(department)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DepartmentCarouselBoxPreview() {
    DepartmentCarouselCard(
        Department(
            1,
            "컴퓨터공학과"
        ),
        onCancel = {}
    )
}
