package `in`.koreatech.koin.feature.setting.ui.term

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.term.TermArticle
import `in`.koreatech.koin.feature.setting.R
import `in`.koreatech.koin.feature.setting.constant.TermConstant
import `in`.koreatech.koin.feature.setting.ui.term.component.TermDescriptionItem
import `in`.koreatech.koin.feature.setting.ui.term.component.TermMenuItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermScreen(
    termType: TermConstant,
    modifier: Modifier = Modifier,
    viewModel: TermViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val termUnknownMessage = stringResource(R.string.term_unknown_message)
    LaunchedEffect(termType) {
        when (termType) {
            TermConstant.TERM_UNKNOWN -> {
                snackbarHostState.showSnackbar(message = termUnknownMessage)
            }
            TermConstant.TERM_KOIN -> {
                viewModel.loadKoinTerm()
            }
            TermConstant.TERM_PRIVACY_POLICY -> {
                viewModel.loadPrivacyTerm()
            }
            TermConstant.TERM_MARKETING -> {
                viewModel.loadMarketingTerm()
            }
        }
    }
    val termState by viewModel.term.collectAsState()
    LaunchedEffect(termState) {
        if (termState is TermState.Failure) { // smartcast not working
            snackbarHostState.showSnackbar(message = (termState as TermState.Failure).message)
        }
    }
    Scaffold(
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.term_appbar_title),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = KoinTheme.colors.primary500,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                onNavigationIconClick = onTopbarBackClick
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.systemBarsPadding()
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { contentPadding ->
        if (termState is TermState.Success) { // smartcast not working
            TermScreenImpl(
                modifier = modifier
                    .systemBarsPadding()
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding),
                title = (termState as TermState.Success).term.header,
                articles = (termState as TermState.Success).term.articles
            )
        }
    }
}

@Composable
fun TermScreenImpl(
    title: String,
    articles: List<TermArticle>,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    val termLazyState = rememberLazyListState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = KoinTheme.colors.neutral0),
        state = termLazyState
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 13.dp, horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicText(
                    text = title,
                    style = KoinTheme.typography.regular16
                )
            }
            HorizontalDivider(color = KoinTheme.colors.neutral100)
        }
        item {
            articles.forEachIndexed { index, article ->
                TermMenuItem(
                    text = article.article,
                    onClick = {
                        scope.launch {
                            termLazyState.animateScrollToItem(index + 2)
                        }
                    }
                )
            }
            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 28.dp, horizontal = 24.dp),
                color = KoinTheme.colors.neutral800
            )
        }
        items(items = articles) {
            TermDescriptionItem(
                title = it.article,
                description = it.content
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun TermScreenPreview() {
    TermScreenImpl(
        title = "코인 이용약관",
        articles = listOf(
            TermArticle("제 1조 ---", listOf("1조 내용")),
            TermArticle("제 2조 ---", listOf("2조 내용1", "2조 내용2")),
            TermArticle("제 3조 ---", listOf("3조 내용1", "3조 내용2", "3조 내용3"))
        )
    )
}
