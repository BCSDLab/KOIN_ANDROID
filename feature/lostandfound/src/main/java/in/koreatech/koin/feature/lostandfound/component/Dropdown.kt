package `in`.koreatech.koin.feature.lostandfound.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R

/**
 * Dropdown Component
 * @param title Dropdown title
 * @param items Dropdown items
 * @param onItemSelected Callback when item is selected
 */
@Composable
fun Dropdown(
    title: String,
    isDropdownExpanded: Boolean,
    items: List<String>,
    modifier: Modifier = Modifier,
    onDropdownExpandChange: (Boolean) -> Unit = {},
    onItemSelected: (Int) -> Unit = {}
) = Column(modifier = modifier) {
    Row(
        modifier = Modifier
            .clip(KoinTheme.shapes.medium)
            .background(
                color = KoinTheme.colors.info200
            )
            .noRippleClickable {
                onDropdownExpandChange(!isDropdownExpanded)
            }
            .padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = KoinTheme.typography.medium14.copy(fontWeight = FontWeight.SemiBold),
            color = KoinTheme.colors.primary600,
            maxLines = 1
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            modifier = Modifier.rotate(if (isDropdownExpanded) 180f else 0f),
            painter = painterResource(id = R.drawable.ic_dropdown_arrow),
            contentDescription = "",
        )
    }

    /**
     * Vertical padding of DropdownMenu is hardcoded to 8.dp by google.
     * So, we will leave vertical padding value to 0.dp
     * @see [androidx.compose.material3.DropdownMenu]
     */
    DropdownMenu(
        modifier = Modifier
            .width(96.dp)
            .padding(0.dp),
        expanded = isDropdownExpanded,
        onDismissRequest = { onDropdownExpandChange(false) },
        containerColor = Color.Transparent,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(KoinTheme.shapes.medium)
                .background(
                    color = KoinTheme.colors.info200
                )
        ) {
            Column {
                items.forEachIndexed { index, it ->
                    Text(
                        text = it,
                        style = KoinTheme.typography.medium14.copy(textAlign = TextAlign.Center),
                        color = KoinTheme.colors.primary600,
                        modifier = Modifier
                            .fillMaxWidth()
                            .noRippleClickable {
                                onItemSelected(index)
                                onDropdownExpandChange(false)
                            }
                            .padding(vertical = 8.dp, horizontal = 12.dp),
                        maxLines = 1,
                    )
                }
            }
        }
    }
}