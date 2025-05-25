package `in`.koreatech.koin.feature.club.ui.detail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.BuildConfig
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.intent.detail.ClubDetailIntent
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_CATEGORY
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_GOOGLE_FORM
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_INSTAGRAM
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_INTRODUCTION
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_LOCATION
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_OPEN_CHAT
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_PHONE_NUMBER
import `in`.koreatech.koin.feature.club.type.DetailTabType
import `in`.koreatech.koin.feature.club.ui.detail.component.button.DetailButton
import `in`.koreatech.koin.feature.club.ui.detail.component.dialog.DetailLoginDialog
import `in`.koreatech.koin.feature.club.ui.detail.component.tabrow.DetailTabRow
import `in`.koreatech.koin.feature.club.ui.detail.intro.ClubDetailIntro
import `in`.koreatech.koin.feature.club.ui.detail.qna.ClubDetailQna
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ClubDetail(
    initialPage: Int = 0,
    viewModel: ClubDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val detailList = listOf(
        Pair(DETAIL_CATEGORY, state.clubDetails?.category),
        Pair(DETAIL_LOCATION, state.clubDetails?.location),
        Pair(DETAIL_INTRODUCTION, state.clubDetails?.introduction),
        Pair(DETAIL_INSTAGRAM, state.clubDetails?.instagram),
        Pair(DETAIL_GOOGLE_FORM, state.clubDetails?.googleForm),
        Pair(DETAIL_OPEN_CHAT, state.clubDetails?.openChat),
        Pair(DETAIL_PHONE_NUMBER, state.clubDetails?.phoneNumber)
    )
    val qnaList = state.clubQnasInfo?.qnas
    val tabList = DetailTabType.entries.map { it.strResId }

    val pagerState = rememberPagerState(initialPage = initialPage) { tabList.size }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scrollStates = remember { mutableStateMapOf<Int, Int>() }
    val listState = rememberLazyListState()

    Scaffold(
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = state.clubDetails?.name ?: "",
                onNavigationIconClick = {
                    (context as Activity).finish()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .size(40.dp),
                shape = CircleShape,
                onClick = {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    focusedElevation = 0.dp,
                    hoveredElevation = 0.dp
                ),
                containerColor = KoinTheme.colors.primary500
            ) {
                Icon(
                    modifier = Modifier
                        .size(36.dp),
                    painter = painterResource(R.drawable.icon_floating_up),
                    tint = KoinTheme.colors.neutral0,
                    contentDescription = "up"
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        modifier = Modifier
                            .padding(horizontal = 24.dp),
                        content = {
                            Text(
                                text = data.visuals.message,
                                style = KoinTheme.typography.regular12,
                                color = KoinTheme.colors.neutral0
                            )
                        },
                        action = {
                            data.visuals.actionLabel?.let { label ->
                                Box(
                                    modifier = Modifier
                                        .padding(end = 12.dp)
                                        .clickable { data.performAction() }
                                ) {
                                    Text(
                                        text = label,
                                        style = KoinTheme.typography.regular12,
                                        color = KoinTheme.colors.info700
                                    )
                                }
                            }
                        },
                        containerColor = Color(0xCC041A44),
                        contentColor = KoinTheme.colors.neutral0,
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            )
        }
    ) { contentPadding ->

        var showLoginDialog by remember { mutableStateOf(false) }

        if (showLoginDialog) {
            DetailLoginDialog(
                title = stringResource(R.string.detail_dialog_login_title),
                description = stringResource(R.string.detail_dialog_login_description),
                onPositive = {
                    showLoginDialog = false
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("koin://login/login"))
                    context.startActivity(intent)
                },
                onNegative = { showLoginDialog = false }
            )
        }

        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
                .systemBarsPadding()
                .fillMaxSize(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .size(200.dp),
                    model = ImageRequest.Builder(context)
                        .data(state.clubDetails?.imageUrl)
                        .size(400, 400)
                        .build(),
                    contentDescription = "Club Image",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    loading = {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    },
                    error = {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text("이미지를 불러올 수 없습니다.")
                        }
                    }
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
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
                                painter = if (state.clubDetails?.isLiked ?: false) painterResource(id = R.drawable.icon_like_true) else painterResource(id = R.drawable.icon_like_false),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 4.dp)
                                    .clickable {
                                        if (state.userId == null) {
                                            showLoginDialog = true
                                        } else {
                                            viewModel.handleIntent(ClubDetailIntent.ChangeClubLike)
                                        }
                                    }
                            )
                            Text(
                                text = "${state.clubDetails?.likes}",
                                style = KoinTheme.typography.medium14
                            )
                        }
                        state.userId?.let {
                            if (state.clubDetails?.manager ?: false) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    DetailButton(
                                        text = stringResource(R.string.detail_fix_button),
                                        onClick = {},
                                        contentPadding = PaddingValues(horizontal = 11.dp, vertical = 5.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    DetailButton(
                                        text = stringResource(R.string.detail_empowerment_button),
                                        onClick = {},
                                        contentPadding = PaddingValues(horizontal = 9.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        detailList.forEach { intro ->
                            Row {
                                Text(
                                    text = stringResource(intro.first.strResId),
                                    style = KoinTheme.typography.medium18,
                                    color = KoinTheme.colors.neutral800
                                )
                                Text(
                                    text = intro.second ?: "",
                                    maxLines = when (intro.first) {
                                        DETAIL_INTRODUCTION -> 2
                                        else -> 1
                                    },
                                    style = KoinTheme.typography.medium18,
                                    color = KoinTheme.colors.neutral800,
                                    overflow = TextOverflow.Ellipsis
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
                            text = state.clubDetails?.updatedAt ?: "",
                            style = KoinTheme.typography.regular12,
                            color = KoinTheme.colors.neutral600
                        )
                        Text(
                            text = stringResource(R.string.detail_date_text),
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
                    when (tabList[page]) {
                        DetailTabType.DETAIL_INTRO.strResId -> {
                            val snackbarMessage = stringResource(R.string.detail_snackbar_detail_intro_text)
                            val snackbarActionLabel = stringResource(R.string.detail_snackbar_detail_intro_button)
                            ClubDetailIntro(
                                onFixIntroClick = {
                                    scope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message =  snackbarMessage,
                                            actionLabel = snackbarActionLabel,
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            var intent: Intent
                                            if (BuildConfig.DEBUG){
                                                intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://stage.koreatech.in/"))
                                            }else {
                                                intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://koreatech.in/"))
                                            }
                                            context.startActivity(intent)
                                        }
                                    }
                                },
                                isManager = state.clubDetails?.manager ?: false,
                                userId = state.userId
                            )
                        }
                        DetailTabType.QNA.strResId -> {
                            ClubDetailQna(
                                qnaList = qnaList,
                                isManager = state.clubDetails?.manager ?: false,
                                userId = state.userId,
                                onAddQuestionClick = { content ->
                                    viewModel.handleIntent(ClubDetailIntent.AddClubQna(null, content))
                                },
                                onDeleteQnaClick = { qnaId ->
                                    viewModel.handleIntent(ClubDetailIntent.DeleteClubQna(qnaId))
                                },
                                onAddAnswerClick = { qnaId, content ->
                                    viewModel.handleIntent(ClubDetailIntent.AddClubQna(qnaId, content))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
