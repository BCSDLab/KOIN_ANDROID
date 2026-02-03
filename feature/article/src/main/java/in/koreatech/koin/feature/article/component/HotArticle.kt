package `in`.koreatech.koin.feature.article.component

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
import `in`.koreatech.koin.feature.article.R
import `in`.koreatech.koin.feature.article.enums.ArticleBoardType
import `in`.koreatech.koin.feature.article.model.ArticleHeaderState

@Composable
fun HotArticle(
    hotArticleList: List<ArticleHeaderState>,
    modifier: Modifier = Modifier,
    navigateToHotArticle: (HotArticleData) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 24.dp),
            style = KoinTheme.typography.bold16,
            text = stringResource(R.string.hot_article_title)
        )

        hotArticleList.forEach { hotArticle ->
            HotArticleItem(
                hotArticleData =
                HotArticleData(
                    articleId = hotArticle.id,
                    articleTitle = hotArticle.title,
                    board = hotArticle.board
                ),
                navigateToHotArticle = navigateToHotArticle
            )
            HorizontalDivider(color = KoinTheme.colors.neutral100)
        }
    }
}

@Composable
fun HotArticleItem(
    hotArticleData: HotArticleData,
    modifier: Modifier = Modifier,
    navigateToHotArticle: (HotArticleData) -> Unit
) {
    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .noRippleClickable { navigateToHotArticle(hotArticleData) }
            .padding(vertical = 12.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(hotArticleData.board.koreanName),
            style =
            KoinTheme.typography.bold12.copy(
                fontWeight = FontWeight.SemiBold,
                color = KoinTheme.colors.primary600
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = hotArticleData.articleTitle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style =
            KoinTheme.typography.bold14.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        )
    }
}

data class HotArticleData(
    val articleId: Int,
    val articleTitle: String,
    val board: ArticleBoardType
)
