package `in`.koreatech.koin.feature.user.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.user.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Koin User Info Dropdown Component
 * @param text Dropdown text
 * @param hint Dropdown hint
 * @param isSelected Dropdown item selected state
 * @param isDropdownExpanded Dropdown expanded state
 * @param items Dropdown items
 * @param modifier Modifier
 * @param arrowDirection Direction of the dropdown arrow
 * @param onDropdownExpandChange Callback when dropdown expanded state is changed
 * @param onItemSelected Callback when item is selected
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KoinUserDropdown(
    text: String,
    hint: String,
    isSelected: Boolean,
    isDropdownExpanded: Boolean,
    items: ImmutableList<String>,
    modifier: Modifier = Modifier,
    shape: CornerBasedShape = KoinTheme.shapes.large,
    arrowDirection: KoinUserDropdownArrowDirection = KoinUserDropdownArrowDirection.DOWN,
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
            targetValue = when (arrowDirection) {
                KoinUserDropdownArrowDirection.UP -> if (isDropdownExpanded) 0f else 180f
                KoinUserDropdownArrowDirection.DOWN -> if (isDropdownExpanded) 180f else 0f
            },
            label = "degree"
        )

        val dropdownCorner: Dp by animateDpAsState(
            targetValue = if (isDropdownExpanded) 0.dp else 14.dp
        )

        val dropdownShape = when (arrowDirection) {
            KoinUserDropdownArrowDirection.UP -> shape.copy(
                topStart = CornerSize(dropdownCorner),
                topEnd = CornerSize(dropdownCorner)
            )

            KoinUserDropdownArrowDirection.DOWN -> shape.copy(
                bottomStart = CornerSize(dropdownCorner),
                bottomEnd = CornerSize(dropdownCorner)
            )
        }

        Row(
            modifier = Modifier
                .shadow(
                    elevation = 4.dp,
                    shape = dropdownShape
                )
                .background(
                    color = KoinTheme.colors.neutral0,
                    shape = dropdownShape
                )
                .padding(vertical = 8.dp, horizontal = 12.dp)
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isSelected) text else hint,
                style = KoinTheme.typography.regular14,
                color = if (isSelected) Color.Unspecified else KoinTheme.colors.neutral400,
                maxLines = 1
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier.rotate(rotateDegree),
                painter = painterResource(id = R.drawable.ic_arrow_down),
                tint = KoinTheme.colors.neutral400,
                contentDescription = ""
            )
        }

        /**
         * Vertical padding of DropdownMenu is hardcoded to 8.dp by google.
         * So, we will leave vertical padding value to 0.dp
         * @see [androidx.compose.material3.DropdownMenu]
         */
        ExposedDropdownMenu(
            modifier = Modifier
                .height(180.dp) // Set a fixed height for the dropdown menu
                .wrapContentSize(),
            expanded = isDropdownExpanded,
            onDismissRequest = { onDropdownExpandChange(false) },
            containerColor = KoinTheme.colors.neutral0,
            shape = when (arrowDirection) {
                KoinUserDropdownArrowDirection.UP -> shape.copy(
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp)
                )

                KoinUserDropdownArrowDirection.DOWN -> shape.copy(
                    topEnd = CornerSize(0.dp),
                    topStart = CornerSize(0.dp)
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape)
            ) {
                Column {
                    items.forEachIndexed { index, it ->
                        Text(
                            text = it,
                            style = KoinTheme.typography.medium14,
                            modifier = Modifier
                                .fillMaxWidth()
                                .noRippleClickable {
                                    onItemSelected(index)
                                    onDropdownExpandChange(false)
                                }
                                .padding(vertical = 8.dp, horizontal = 12.dp),
                            maxLines = 1
                        )

                        if (index != items.size - 1) {
                            HorizontalDivider(
                                color = KoinTheme.colors.neutral200
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class KoinUserDropdownArrowDirection {
    UP,
    DOWN
}

@Preview(showBackground = true)
@Composable
private fun PreviewKoinUserDropdown() {
    KoinUserDropdown(
        text = "컴퓨터공학과",
        hint = "학과를 선택해주세요",
        isSelected = false,
        isDropdownExpanded = false,
        items = persistentListOf("컴퓨터공학과", "전자공학과", "기계공학과")
    )
}
