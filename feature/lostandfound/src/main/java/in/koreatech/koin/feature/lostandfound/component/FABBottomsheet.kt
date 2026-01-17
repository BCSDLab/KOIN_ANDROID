package `in`.koreatech.koin.feature.lostandfound.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R

@Composable
fun LostAndFoundFABContent(
    onDismissRequest: () -> Unit,
    onFindOwnerClick: () -> Unit,
    onLostItemClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = KoinTheme.colors.neutral0,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 12.dp,
                    bottom = 12.dp,
                    start = 32.dp,
                    end = 24.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.bottom_sheet_title),
                style = KoinTheme.typography.bold18
            )
            IconButton(onClick = onDismissRequest) {
                Icon(
                    painter = painterResource(R.drawable.ic_bottomsheet_close),
                    contentDescription = stringResource(R.string.bottom_sheet_close)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = KoinTheme.colors.neutral300
                )
                .padding(
                    top = 16.dp,
                    bottom = 12.dp,
                    start = 32.dp,
                    end = 32.dp
                )
        ) {
            LostAndFoundSheetButton(
                text = stringResource(R.string.finding_btn),
                icon = painterResource(R.drawable.ic_found),
                onClick = onFindOwnerClick
            )
            Spacer(modifier = Modifier.height(16.dp))
            LostAndFoundSheetButton(
                text = stringResource(R.string.lost_btn),
                icon = painterResource(R.drawable.ic_lost),
                onClick = onLostItemClick
            )
        }
    }
}

@Composable
private fun LostAndFoundSheetButton(
    text: String,
    icon: Painter,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(49.dp)
            .background(color = KoinTheme.colors.neutral0, shape = RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = KoinTheme.colors.neutral300,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = KoinTheme.typography.bold18,
            color = KoinTheme.colors.neutral600
        )
    }
}