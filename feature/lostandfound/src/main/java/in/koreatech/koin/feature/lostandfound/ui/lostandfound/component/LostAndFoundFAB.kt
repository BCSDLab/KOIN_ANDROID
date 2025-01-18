package `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R

@Composable
fun LostAndFoundFAB(
    mainOnClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column {
        LostAndFoundFABButton(
            painter = painterResource(id = R.drawable.ic_fab_write),
            text = stringResource(id = R.string.fab_write),
        ) {
            mainOnClick()
        }
    }
}

@Composable
fun LostAndFoundFAB(
    modifier: Modifier = Modifier,
    isDialogExpanded: Boolean,
    dialogExpandButtonText: String,
    dialogExpandButtonPainter: Painter,
    onDialogExpandedChange: (Boolean) -> Unit = {},
    firstButtonText: String,
    firstButtonPainter: Painter,
    onFirstButtonClick: () -> Unit = {},
    secondButtonText: String,
    secondButtonPainter: Painter,
    onSecondButtonClick: () -> Unit = {}
) = Column(modifier = modifier) {

    if (isDialogExpanded) {
        LostAndFoundFABButton(
            painter = firstButtonPainter,
            text = firstButtonText
        ) {
            onFirstButtonClick()
        }

        Spacer(modifier = Modifier.height(8.dp))

        LostAndFoundFABButton(
            painter = secondButtonPainter,
            text = secondButtonText
        ) {
            onSecondButtonClick()
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    LostAndFoundFABButton(
        painter = dialogExpandButtonPainter,
        text = dialogExpandButtonText,
    ) {
        onDialogExpandedChange(!isDialogExpanded)
    }
}

@Composable
fun LostAndFoundFABButton(
    painter: Painter,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) = Box(
    modifier = modifier
        .border(width = 1.dp, color = KoinTheme.colors.neutral300, shape = RoundedCornerShape(50))
        .background(color = KoinTheme.colors.neutral0, shape = RoundedCornerShape(50))
        .padding(vertical = 8.dp, horizontal = 12.dp)
        .noRippleClickable { onClick() }
) {
    Row {
        Image(
            painter = painter,
            contentDescription = text
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = KoinTheme.typography.regular16
        )
    }
}