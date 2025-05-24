package `in`.koreatech.koin.feature.club.ui.detail

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.type.DetailIntroType
import `in`.koreatech.koin.feature.club.type.DetailTabType
import `in`.koreatech.koin.feature.club.ui.detail.component.button.DetailButton
import `in`.koreatech.koin.feature.club.ui.detail.component.qnabox.DetailQnaBox
import `in`.koreatech.koin.feature.club.ui.detail.component.tabrow.DetailTabRow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ClubDetail(
    initialPage: Int = 0,
    viewModel: ClubDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    when {
        state.clubDetails != null -> Log.e("MYLOG", "${state.clubDetails}")
    }

    val introList = listOf(
        Pair(stringResource(DetailIntroType.DETAIL_CATEGORY.strResId),state.clubDetails?.category),
        Pair(stringResource(DetailIntroType.DETAIL_LOCATION.strResId),state.clubDetails?.location),
        Pair(stringResource(DetailIntroType.DETAIL_INTRODUCTION.strResId),state.clubDetails?.introduction),
        Pair(stringResource(DetailIntroType.DETAIL_INSTAGRAM.strResId),state.clubDetails?.instagram),
        Pair(stringResource(DetailIntroType.DETAIL_GOOGLE_FORM.strResId),state.clubDetails?.googleForm),
        Pair(stringResource(DetailIntroType.DETAIL_OPEN_CHAT.strResId),state.clubDetails?.openChat),
        Pair(stringResource(DetailIntroType.DETAIL_PHONE_NUMBER.strResId),state.clubDetails?.phoneNumber)
    )
    val qnaList = state.clubQnasInfo?.qnas
    val tabList = DetailTabType.entries.map { it.strResId }
    val pagerState = rememberPagerState(initialPage = initialPage) { tabList.size }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = state.clubDetails?.name ?: "",
                onNavigationIconClick = {
                    (context as Activity).finish()
                }
            )
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
                .systemBarsPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.icon_likes),
                    contentDescription = "",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(top = 3.dp)
                )
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(
                            horizontal = 24.dp,
                            vertical = 16.dp
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = state.clubDetails?.name ?: "",
                                style = KoinTheme.typography.bold20,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )
                            Image(
                                painter = painterResource(id = R.drawable.icon_likes),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 4.dp)
                            )
                            Text(
                                text = "${state.clubDetails?.likes}",
                                style = KoinTheme.typography.medium14
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DetailButton(
                                text = stringResource(R.string.club_detail_fix_button),
                                onClick = {}
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            DetailButton(
                                text = stringResource(R.string.club_detail_empowerment_button),
                                onClick = {}
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        introList.forEach { intro ->
                            Row {
                                Text(
                                    text = intro.first,
                                    style = KoinTheme.typography.medium18,
                                    color = KoinTheme.colors.neutral800
                                )
                                Text(
                                    text = intro.second ?: "",
                                    style = KoinTheme.typography.medium18,
                                    color = KoinTheme.colors.neutral800
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_club_info),
                            contentDescription = "picture",
                            modifier = Modifier
                                .size(17.dp)
                                .padding(end = 4.dp)
                        )
                        Text(
                            text = "0000.00.00",
                            style = KoinTheme.typography.regular12,
                            color = KoinTheme.colors.neutral600
                        )
                        Text(
                            text = stringResource(R.string.club_detail_date_text),
                            style = KoinTheme.typography.regular12,
                            color = KoinTheme.colors.neutral600
                        )
                    }
                }
            }
            stickyHeader {
                DetailTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    onTabSelected = {
                        scope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    },
                    titles = tabList.map { stringResource(it) }
                )
            }
            item {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    state = pagerState,
                    verticalAlignment = Alignment.Top
                ) { page ->
                    val scrollState = rememberScrollState()

                    // 페이지가 바뀔 때마다 스크롤 상태 초기화
                    LaunchedEffect(pagerState.currentPage) {
                        scrollState.scrollTo(0)
                    }
                    when (tabList[page]) {
                        DetailTabType.DETAIL_INTRO.strResId -> {
                            // 아직 요소 없음
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 24.dp,
                                        vertical = 16.dp
                                    ),
                                Alignment.TopEnd
                            ) {
                                DetailButton(
                                    text = "상세 소개 수정",
                                    onClick = {}
                                )
                            }
                        }
                        DetailTabType.QNA.strResId -> {
                            Column(
                                modifier = Modifier
                                    .padding(top = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                qnaList?.forEach {
                                    DetailQnaBox(
                                        questionText = it.content,
                                        createdDate = it.createdAt,
                                        answerText = it.children[0],
                                        onQuestionDeleteClick = {},
                                        onAnswerDeleteClick = {},
                                        onAnswerSendClick = {},
                                        isQnaEditable = state.clubDetails?.manager ?: false,
                                        isAnswerEditable = state.clubDetails?.manager ?: false
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ClubDetailPreView() {

}