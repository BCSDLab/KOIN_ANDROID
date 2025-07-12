package `in`.koreatech.koin.feature.club.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KoinClubEventsDropdown(
    text: String,
    isDropdownExpanded: Boolean,
    items: ImmutableList<String>,
    modifier: Modifier = Modifier,
    borderColor: Color = KoinTheme.colors.neutral300,
    onDropdownExpandChange: (Boolean) -> Unit = {},
    onItemSelected: (Int) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    ExposedDropdownMenuBox(
        expanded = isDropdownExpanded,
        onExpandedChange = {
            focusManager.clearFocus()
            onDropdownExpandChange(it)
        },
        modifier = modifier
    ) {
        val rotateDegree: Float by animateFloatAsState(
            targetValue = if (isDropdownExpanded) 180f else 0f,
            label = "degree"
        )

        val dropdownCorner: Dp by animateDpAsState(
            targetValue = if (isDropdownExpanded) 0.dp else 8.dp
        )

        val dropdownShape = KoinTheme.shapes.small.copy(
            bottomStart = CornerSize(dropdownCorner),
            bottomEnd = CornerSize(dropdownCorner)
        )

        Row(
            modifier = Modifier
                .background(
                    color = KoinTheme.colors.neutral50,
                    shape = dropdownShape
                )
                .border(1.dp, borderColor, dropdownShape)
                .padding(vertical = 8.dp, horizontal = 12.dp)
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = KoinTheme.typography.regular14,
                color = KoinTheme.colors.neutral800,
                maxLines = 1
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Icon(
                modifier = Modifier.rotate(rotateDegree),
                imageVector = ImageVector.vectorResource(R.drawable.ic_club_dropdown_arrow),
                contentDescription = null
            )
        }

        ExposedDropdownMenu(
            modifier = Modifier
                .wrapContentSize()
                .border(
                    1.dp,
                    KoinTheme.colors.neutral300,
                    KoinTheme.shapes.small.copy(
                        topEnd = CornerSize(0.dp),
                        topStart = CornerSize(0.dp)
                    )
                ),
            expanded = isDropdownExpanded,
            onDismissRequest = { onDropdownExpandChange(false) },
            containerColor = KoinTheme.colors.neutral50,
            shape = KoinTheme.shapes.small.copy(
                topEnd = CornerSize(0.dp),
                topStart = CornerSize(0.dp)
            ),
            shadowElevation = 0.dp,
            tonalElevation = 0.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column {
                    items.forEachIndexed { index, it ->
                        Text(
                            text = it,
                            style = KoinTheme.typography.medium14,
                            modifier = Modifier
                                .noRippleClickable {
                                    onItemSelected(index)
                                    onDropdownExpandChange(false)
                                }
                                .padding(vertical = 8.dp, horizontal = 12.dp),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinClubEventsDropdownPreview() {
    KoinClubEventsDropdown(
        text = "생성순",
        isDropdownExpanded = false,
        items = persistentListOf("생성순", "조회순")
    )
}
