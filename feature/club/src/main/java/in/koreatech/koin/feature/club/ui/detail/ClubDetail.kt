package `in`.koreatech.koin.feature.club.ui.detail

import android.app.Activity
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.ui.detail.component.button.DetailButton
import `in`.koreatech.koin.feature.club.ui.detail.component.qnabox.DetailQnaBox
import `in`.koreatech.koin.feature.club.ui.detail.component.tabrow.DetailTabRow
import `in`.koreatech.koin.feature.club.ui.detail.type.DetailTabType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ClubDetail(
    initialPage: Int = 0
) {
    val labels = listOf(
        "분과: ",
        "동아리방 위치: ",
        "동아리 소개: ",
        "인스타 : ",
        "구글폼 : ",
        "오픈채팅 : ",
        "전화번호 : "
    )
    val tabs = DetailTabType.entries.map { it.title }
    val pagerState = rememberPagerState(initialPage = initialPage) { tabs.size }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold (
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = "BCSD",
                onNavigationIconClick = {
                    (context as Activity).finish()
                }
            )
        }
    ) { contentPadding ->
        LazyColumn (
            modifier = Modifier
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
                .systemBarsPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.fi_heart),
                    contentDescription = "picture",
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
                        ){
                            Text(
                                text = "BCSD",
                                style = KoinTheme.typography.bold20,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )
                            Image(
                                painter = painterResource(id = R.drawable.fi_heart),
                                contentDescription = "picture",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 4.dp)
                            )
                            Text(
                                text = "000",
                                style = KoinTheme.typography.medium14
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DetailButton(
                                text = "수정하기",
                                onClick = {}
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            DetailButton(
                                text = "권한 위임",
                                onClick = {}
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        labels.forEach { label ->
                            Text(
                                text = label,
                                style = KoinTheme.typography.medium18,
                                color = KoinTheme.colors.neutral800
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                        ,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.fi_info),
                            contentDescription = "picture",
                            modifier = Modifier
                                .size(17.dp)
                                .padding(end = 4.dp)
                        )
                        Text(
                            text = "0000.00.00. 업데이트",
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
                    titles = tabs
                )
            }
            item {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize()
                    ,
                    state = pagerState,
                    verticalAlignment = Alignment.Top
                ) { page ->
                    val scrollState = rememberScrollState()

                    // 페이지가 바뀔 때마다 스크롤 상태 초기화
                    LaunchedEffect(pagerState.currentPage) {
                        scrollState.scrollTo(0)
                    }
                    when(tabs[page]){
                        DetailTabType.DETAIL_INTRO.title -> {
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
                        DetailTabType.QNA.title -> {
                            Column (
                                modifier = Modifier
                                    .padding(top = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                DetailQnaBox(
                                    questionText = "추가 모집 공고는 언제 올라오나요",
                                    createdDate = "2025.00.00. 00:00",
                                    onQuestionDeleteClick = {},
                                    onAnswerDeleteClick = {}
                                )
                                DetailQnaBox(
                                    questionText = "추가 모집 공고는 언제 올라오나요",
                                    createdDate = "2025.00.00. 00:00",
                                    onQuestionDeleteClick = {},
                                    onAnswerDeleteClick = {}
                                )
                                DetailQnaBox(
                                    questionText = "추가 모집 공고는 언제 올라오나요",
                                    createdDate = "2025.00.00. 00:00",
                                    onQuestionDeleteClick = {},
                                    onAnswerDeleteClick = {}
                                )
                                DetailQnaBox(
                                    questionText = "추가 모집 공고는 언제 올라오나요",
                                    createdDate = "2025.00.00. 00:00",
                                    onQuestionDeleteClick = {},
                                    onAnswerDeleteClick = {},
                                    answerText = "올렸습니다!"
                                )
                                DetailQnaBox(
                                    questionText = "추가 모집 공고는 언제 올라오나요",
                                    createdDate = "2025.00.00. 00:00",
                                    onQuestionDeleteClick = {},
                                    onAnswerDeleteClick = {},
                                    answerText = "올렸습니다!"
                                )
                                DetailQnaBox(
                                    questionText = "추가 모집 공고는 언제 올라오나요",
                                    createdDate = "2025.00.00. 00:00",
                                    onQuestionDeleteClick = {},
                                    onAnswerDeleteClick = {},
                                    answerText = "올렸습니다!"
                                )
                                DetailQnaBox(
                                    questionText = "추가 모집 공고는 언제 올라오나요",
                                    createdDate = "2025.00.00. 00:00",
                                    onQuestionDeleteClick = {},
                                    onAnswerDeleteClick = {},
                                    answerText = "올렸습니다!"
                                )
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
fun Page1Preview() {
    ClubDetail()
}

@Preview
@Composable
fun Page2Preview() {
    ClubDetail(1)
}