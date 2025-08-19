package `in`.koreatech.koin.ui.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.article.ArticleNotiType
import `in`.koreatech.koin.ui.main.state.ArticleMainState
import kotlinx.coroutines.delay

@Composable
fun HotArticlePager(
    modifier: Modifier = Modifier,
    articles: List<ArticleMainState>,
    onNotiClick: (ArticleMainState.Noti) -> Unit = {},
    onArticleClick: (ArticleMainState.Content) -> Unit = {}
) {
    val pagerState = rememberPagerState { articles.size }

    LaunchedEffect(pagerState.settledPage, articles.size) {
        if (articles.isNotEmpty()) {
            delay(5000)
            val nextPage = (pagerState.currentPage + 1) % articles.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(95.dp)
        ) { page ->
            when (val article = articles[page]) {
                is ArticleMainState.Noti -> {
                    HotNotiCard(
                        noti = article,
                        onClick = { onNotiClick(article) }
                    )
                }
                is ArticleMainState.Content -> {
                    HotArticleCard(
                        article = article,
                        onClick = { onArticleClick(article) }
                    )
                }
            }
        }

        PagerIndicator(
            pagerState = pagerState,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Composable
fun PagerIndicator(
    pagerState: androidx.compose.foundation.pager.PagerState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { index ->
            val isSelected = pagerState.currentPage == index
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(if (isSelected) 6.dp else 4.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) KoinTheme.colors.primary400 else KoinTheme.colors.neutral300)
            )
        }
    }
}

@Composable
fun HotNotiCard(
    noti: ArticleMainState.Noti,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = KoinTheme.colors.neutral100
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = noti.title,
                    color = KoinTheme.colors.sub600,
                    style = KoinTheme.typography.bold14
                )
                Text(
                    text = noti.sub,
                    color = KoinTheme.colors.neutral600,
                    style = KoinTheme.typography.medium12
                )
            }
            Spacer(Modifier.weight(1f))
            Icon(
                painter = painterResource(
                    when (noti.type) {
                        ArticleNotiType.KEYWORD -> R.drawable.ic_main_article_bell
                        ArticleNotiType.LOST_AND_FOUND -> R.drawable.ic_main_lost_and_found_icon
                    }
                ),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun HotArticleCard(
    article: ArticleMainState.Content,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = KoinTheme.colors.neutral100
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color.Unspecified
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = "지금 인기있는 글",
                    color = KoinTheme.colors.sub500,
                    style = KoinTheme.typography.bold12
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = article.title,
                    color = KoinTheme.colors.neutral600,
                    style = KoinTheme.typography.bold16,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HotNotiCardPreview() {
    KoinTheme {
        HotNotiCard(
            noti = ArticleMainState.Noti(
                title = "분실물은 여기 다 모아뒀어요",
                sub = "바로가기 >>",
                value = "123",
                type = ArticleNotiType.LOST_AND_FOUND
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HotNotiCardPreview2() {
    KoinTheme {
        HotNotiCard(
            noti = ArticleMainState.Noti(
                title = "키워드가 포함된 공지가 업로드 되면 \n가장 먼저 알림을 보내드려요!",
                sub = "키워드 알림설정 바로가기",
                value = "123",
                type = ArticleNotiType.KEYWORD
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HotArticleCardPreview() {
    KoinTheme {
        HotArticleCard(
            article = ArticleMainState.Content(
                title = "[교내장학] 교내장학 미래학습관 회계팀 학기근로장학봉사 선발 학기기간 절차 봉사 근로장학 식단 사진 업로드",
                id = 1,
                boardId = 10
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HotArticlePagerPreview() {
    val sampleArticles = listOf(
        ArticleMainState.Noti(
            title = "분실물은 여기 다 모아뒀어요",
            sub = "바로가기 >>",
            value = "123",
            type = ArticleNotiType.LOST_AND_FOUND
        ),
        ArticleMainState.Noti(
            title = "TEST",
            sub = "TEST",
            value = "TEST",
            type = ArticleNotiType.KEYWORD
        ),
        ArticleMainState.Content(
            title = "TEST",
            id = 1,
            boardId = 10
        )
    )

    KoinTheme {
        HotArticlePager(
            articles = sampleArticles,
            onNotiClick = {},
            onArticleClick = {}
        )
    }
}
