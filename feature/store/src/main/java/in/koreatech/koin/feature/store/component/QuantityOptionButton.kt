package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun QuantityOptionButton(
    modifier: Modifier = Modifier,
    quantity: Int,
    onOptionClick: () -> Unit = {},
    onMinusClick: () -> Unit = {},
    onPlusClick: () -> Unit = {},
    onDeleteMenuClick: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = onOptionClick,
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, Color.LightGray),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.change_option),
                color = KoinTheme.colors.neutral600
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedButton(
            onClick = {},
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral200),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            enabled = false
        ) {
            if (quantity <= 1) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "",
                    tint = KoinTheme.colors.neutral600,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(onClick = onDeleteMenuClick)
                )
            } else {
                Icon(
                    modifier = Modifier.size(20.dp).clickable(onClick = onMinusClick),
                    painter = painterResource(id = R.drawable.ic_quantity_minus),
                    contentDescription = "",
                    tint = KoinTheme.colors.neutral600
                )
            }

            Text(modifier = Modifier.padding(horizontal = 14.dp), text = "$quantity", color = KoinTheme.colors.neutral600)
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "",
                tint = KoinTheme.colors.neutral600,
                modifier = Modifier
                    .size(20.dp)
                    .clickable(onClick = onPlusClick)
            )
        }
    }
}

@Composable
@Preview
fun QuantityOptionButtonPreview() {
    KoinTheme {
        QuantityOptionButton(
            quantity = 2,
            onOptionClick = {},
            onMinusClick = {},
            onPlusClick = {},
            onDeleteMenuClick = {}
        )
    }
}
