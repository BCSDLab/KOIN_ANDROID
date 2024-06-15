package `in`.koreatech.koin.ui.timetablev2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.compose.ui.ColorPrimary
import `in`.koreatech.koin.domain.model.timetable.Semester

@Composable
fun TimetableContentHeader(
    semesters: List<Semester>,
    modifier: Modifier = Modifier,
    onSavedImage: () -> Unit,
    onVisibleBottomSheet: () -> Unit,
    onSemesterTextChanged: (semester: Semester) -> Unit,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SemesterDropdown(
            semesters = semesters,
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            onSemesterTextChanged = onSemesterTextChanged
        )
        TimetableSaveButton(
            modifier = Modifier
                .padding(4.dp)
                .weight(1f, fill = false)
                .fillMaxHeight()
                .background(color = ColorPrimary, shape = RoundedCornerShape(4.dp))
                .padding(8.dp),
            onClick = onSavedImage
        )
        Icon(
            imageVector = Icons.Default.AddCircle,
            contentDescription = null,
            tint = ColorPrimary,
            modifier = Modifier.clickable {
                onVisibleBottomSheet()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimetableContentHeaderPreview() {
    val semesters = listOf(
        Semester(1, "20241"),
        Semester(2, "20242"),
    )
    Box(modifier = Modifier.fillMaxSize()) {
        TimetableContentHeader(
            semesters = semesters,
            onSavedImage = {},
            onVisibleBottomSheet = {},
            onSemesterTextChanged = {}
        )
    }
}