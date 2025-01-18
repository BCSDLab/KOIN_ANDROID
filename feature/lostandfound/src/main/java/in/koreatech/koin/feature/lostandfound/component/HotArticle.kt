package `in`.koreatech.koin.feature.lostandfound.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.enums.ArticleBoardType
import `in`.koreatech.koin.feature.lostandfound.model.ArticleHeaderState

@Composable
fun HotArticle(
    hotArticleList: List<ArticleHeaderState>,
    modifier: Modifier = Modifier,
    navigateToHotArticle: (articleTitle: String, articleId: Int, boardId: Int) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 24.dp),
            style = KoinTheme.typography.bold16,
            text = stringResource(R.string.hot_article_title),
        )

        hotArticleList.forEach { hotArticle ->
            HotArticleItem(
                id = hotArticle.id,
                board = hotArticle.board,
                title = hotArticle.title,
                navigateToHotArticle = navigateToHotArticle
            )
            HorizontalDivider(color = KoinTheme.colors.neutral100)
        }
    }
}

@Composable
fun HotArticleItem(
    id: Int,
    board: ArticleBoardType,
    title: String,
    modifier: Modifier = Modifier,
    navigateToHotArticle: (articleTitle: String, articleId: Int, boardId: Int) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable { navigateToHotArticle(title, id, board.id) }
            .padding(vertical = 12.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(board.koreanName),
            style = KoinTheme.typography.bold12.copy(
                fontWeight = FontWeight.SemiBold,
                color = KoinTheme.colors.primary600
            ),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = KoinTheme.typography.bold14.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        )
    }
}
