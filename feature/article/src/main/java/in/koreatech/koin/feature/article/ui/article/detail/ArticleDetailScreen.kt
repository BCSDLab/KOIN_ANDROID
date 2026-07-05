package `in`.koreatech.koin.feature.article.ui.article.detail

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.webview.loadKoreatechHtml
import `in`.koreatech.koin.feature.article.R
import `in`.koreatech.koin.feature.article.model.ArticleHeaderState
import `in`.koreatech.koin.feature.article.model.ArticleState
import `in`.koreatech.koin.feature.article.model.AttachmentState
import javax.inject.Inject

@HiltViewModel
internal class ArticleDetailViewModelFactoryHolder @Inject constructor(
    val factory: ArticleDetailViewModel.Factory
) : ViewModel()

@Composable
fun ArticleDetailScreen(
    articleId: Int,
    boardId: Int,
    onNavigateBack: () -> Unit,
    onListClick: () -> Unit,
    onHotArticleClick: (articleId: Int, boardId: Int) -> Unit
) {
    val holder = hiltViewModel<ArticleDetailViewModelFactoryHolder>()
    val viewModel: ArticleDetailViewModel = viewModel(
        factory = ArticleDetailViewModel.provideFactory(holder.factory, articleId, boardId)
    )

    val article by viewModel.article.collectAsStateWithLifecycle()
    val hotArticles by viewModel.hotArticles.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            ArticleDetailTopBar(onNavigateBack = onNavigateBack)
        },
        containerColor = RebrandKoinTheme.colors.neutral0,
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            ArticleHeader(article = article)
            SectionDivider()

            ArticleContentWebView(content = article.content)

            if (article.attachments.isNotEmpty()) {
                HorizontalDivider(color = RebrandKoinTheme.colors.neutral300)
                ArticleAttachments(attachments = article.attachments)
            }

            ArticleNavigationButtons(
                prevArticleId = article.prevArticleId,
                nextArticleId = article.nextArticleId,
                boardId = boardId,
                onListClick = onListClick,
                onArticleClick = onHotArticleClick
            )

            SectionDivider()
            HotArticlesSection(
                hotArticles = hotArticles,
                onArticleClick = onHotArticleClick
            )
        }
    }
}

@Composable
private fun SectionDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .background(RebrandKoinTheme.colors.neutral100)
    )
}

@Composable
private fun ArticleDetailTopBar(onNavigateBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(48.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onNavigateBack
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_left),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.navigation_title_article),
            style = RebrandKoinTheme.typography.bold18
        )
    }
}

@Composable
private fun ArticleHeader(article: ArticleState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(article.header.board.koreanName),
            style = RebrandKoinTheme.typography.bold12,
            color = RebrandKoinTheme.colors.primary800
        )
        Text(
            text = article.header.title,
            style = RebrandKoinTheme.typography.bold16,
            color = RebrandKoinTheme.colors.neutral800
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = article.header.author,
                style = RebrandKoinTheme.typography.regular13,
                color = RebrandKoinTheme.colors.neutral600
            )
            Text(
                text = stringResource(R.string.divider_dot),
                style = RebrandKoinTheme.typography.regular13,
                color = RebrandKoinTheme.colors.neutral400
            )
            Text(
                text = article.header.registeredAt,
                style = RebrandKoinTheme.typography.regular13,
                color = RebrandKoinTheme.colors.neutral500
            )
            Text(
                text = stringResource(R.string.divider_dot),
                style = RebrandKoinTheme.typography.regular13,
                color = RebrandKoinTheme.colors.neutral400
            )
            Icon(
                painter = painterResource(R.drawable.ic_view),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = RebrandKoinTheme.colors.neutral500
            )
            Text(
                text = "${article.header.viewCount}",
                style = RebrandKoinTheme.typography.regular13,
                color = RebrandKoinTheme.colors.neutral500
            )
        }
    }
}

@Composable
private fun ArticleContentWebView(content: String) {
    var contentHeight by remember { mutableStateOf(500.dp) }
    val handler = remember { Handler(Looper.getMainLooper()) }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .height(contentHeight),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                isScrollContainer = false
                overScrollMode = android.view.View.OVER_SCROLL_NEVER
                addJavascriptInterface(
                    object {
                        @JavascriptInterface
                        fun updateHeight(height: Float) {
                            if (height > 0) {
                                handler.post { contentHeight = height.dp }
                            }
                        }
                    },
                    "ArticleHeight"
                )
            }
        },
        update = { webView ->
            if (content.isNotEmpty() && webView.tag != content) {
                webView.tag = content
                val heightScript = "<script>window.addEventListener('load',function(){ArticleHeight.updateHeight(document.body.scrollHeight);});</script>"
                webView.loadKoreatechHtml(webView.context, heightScript + content)
            }
        }
    )
}

@Composable
private fun ArticleAttachments(attachments: List<AttachmentState>) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        attachments.forEach { attachment ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(RebrandKoinTheme.colors.neutral200)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = attachment.name,
                        style = RebrandKoinTheme.typography.bold14,
                        color = RebrandKoinTheme.colors.neutral600,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = attachment.size,
                        style = RebrandKoinTheme.typography.regular12,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.ic_download),
                    contentDescription = null,
                    modifier = Modifier
                        .size(15.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(attachment.url))
                            context.startActivity(intent)
                        },
                    tint = RebrandKoinTheme.colors.neutral500
                )
            }
        }
    }
}

@Composable
private fun HotArticlesSection(
    hotArticles: List<ArticleHeaderState>,
    onArticleClick: (articleId: Int, boardId: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(R.string.hot_articles),
            style = RebrandKoinTheme.typography.bold14,
            color = RebrandKoinTheme.colors.neutral700
        )
        Spacer(modifier = Modifier.height(8.dp))
        hotArticles.forEach { hotArticle ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onArticleClick(hotArticle.id, hotArticle.board.id) }
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(hotArticle.board.simpleKoreanName),
                    style = RebrandKoinTheme.typography.bold12,
                    color = RebrandKoinTheme.colors.primary800
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = hotArticle.title,
                    style = RebrandKoinTheme.typography.regular13,
                    color = RebrandKoinTheme.colors.neutral700,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            HorizontalDivider(color = RebrandKoinTheme.colors.neutral200)
        }
    }
}

@Composable
private fun ArticleNavigationButtons(
    prevArticleId: Int?,
    nextArticleId: Int?,
    boardId: Int,
    onListClick: () -> Unit,
    onArticleClick: (articleId: Int, boardId: Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GrayButton(text = stringResource(R.string.list), onClick = onListClick)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GrayButton(
                text = stringResource(R.string.prev_article),
                onClick = { prevArticleId?.let { onArticleClick(it, boardId) } },
                enabled = prevArticleId != null
            )
            GrayButton(
                text = stringResource(R.string.next_article),
                onClick = { nextArticleId?.let { onArticleClick(it, boardId) } },
                enabled = nextArticleId != null
            )
        }
    }
}

@Composable
private fun GrayButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = RebrandKoinTheme.colors.neutral200,
            contentColor = RebrandKoinTheme.colors.neutral700,
            disabledContainerColor = RebrandKoinTheme.colors.neutral200,
            disabledContentColor = RebrandKoinTheme.colors.neutral400
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text = text, style = RebrandKoinTheme.typography.regular14)
    }
}
