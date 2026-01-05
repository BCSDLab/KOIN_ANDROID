package `in`.koreatech.koin.feature.article.ui.lostandfound.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.article.R

@Composable
fun LostAndFoundPagination(
    currentPage: Int,
    totalPage: Int,
    modifier: Modifier = Modifier,
    onPageChange: (Int) -> Unit = {}
) {
    val showPrevButton = currentPage > 1
    val showNextButton = currentPage < totalPage
    val startPage = 1 + ((currentPage - 1) / 5) * 5
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
    ) {
        LostAndFoundPaginationButton(
            text = stringResource(R.string.pagination_prev),
            isClickable = showPrevButton
        ) {
            onPageChange(currentPage - 1)
        }

        (startPage until startPage + 5).forEach { i ->
            if (i <= totalPage) {
                LostAndFoundPaginationPageButton(
                    page = i,
                    isSelected = i == currentPage
                ) {
                    onPageChange(i)
                }
            }
        }

        LostAndFoundPaginationButton(
            text = stringResource(R.string.pagination_next),
            isClickable = showNextButton
        ) {
            onPageChange(currentPage + 1)
        }
    }
}

@Composable
fun LostAndFoundPaginationButton(
    text: String,
    modifier: Modifier = Modifier,
    isClickable: Boolean = true,
    onClick: () -> Unit = {}
) {
    Text(
        modifier =
        modifier
            .noRippleClickable {
                if (isClickable) {
                    onClick()
                }
            }
            .padding(vertical = 6.dp, horizontal = 16.dp),
        text = text,
        color = if (isClickable) KoinTheme.colors.neutral600 else Color.Transparent
    )
}

@Composable
fun LostAndFoundPaginationPageButton(
    page: Int,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit = {}
) {
    Box(
        modifier =
        modifier
            .size(32.dp)
            .noRippleClickable(onClick = { onClick(page) })
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) KoinTheme.colors.primary500 else KoinTheme.colors.neutral300),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$page",
            color = if (isSelected) KoinTheme.colors.neutral0 else KoinTheme.colors.neutral600
        )
    }
}

@Preview
@Composable
fun LostAndFoundPaginationPageButtonPreview() {
    KoinTheme {
        LostAndFoundPaginationPageButton(page = 1) {}
    }
}

@Preview
@Composable
fun LostAndFoundPaginationButtonPreview() {
    KoinTheme {
        LostAndFoundPaginationButton(text = "이전") {}
    }
}
