package `in`.koreatech.koin.feature.club.ui.clubdetail

import android.content.Context
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.domain.constant.HTTPS_URL
import `in`.koreatech.koin.domain.constant.KOIN_WEB_STAGE_URL
import `in`.koreatech.koin.domain.constant.KOIN_WEB_URL
import `in`.koreatech.koin.domain.constant.LOGIN_ACTIVITY_URL
import `in`.koreatech.koin.domain.util.ext.formatInstagramLinkForm
import `in`.koreatech.koin.domain.util.ext.formatPhoneNumber
import `in`.koreatech.koin.domain.util.ext.isGoogleFormUrl
import `in`.koreatech.koin.domain.util.ext.isInstagramUrl
import `in`.koreatech.koin.domain.util.ext.isOpenChatUrl
import `in`.koreatech.koin.domain.util.ext.isValidPhoneNumber
import `in`.koreatech.koin.feature.club.BuildConfig
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.component.DetailDialog
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_CATEGORY
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_DESCRIPTION
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_GOOGLE_FORM
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_INSTAGRAM
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_LOCATION
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_OPEN_CHAT
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_PHONE_NUMBER
import `in`.koreatech.koin.feature.club.type.DetailTabType
import `in`.koreatech.koin.feature.club.ui.clubdetail.component.dialog.DetailLoginDialog
import `in`.koreatech.koin.feature.club.ui.clubdetail.component.dialog.content.DetailDialogAddQnaContent
import `in`.koreatech.koin.feature.club.ui.clubdetail.component.dialog.content.DetailDialogEmpowermentContent
import `in`.koreatech.koin.feature.club.ui.clubdetail.component.snackbar.DetailSnackBar
import `in`.koreatech.koin.feature.club.ui.clubdetail.component.tabrow.DetailTabRow
import `in`.koreatech.koin.feature.club.ui.clubdetail.intro.ClubDetailIntro
import `in`.koreatech.koin.feature.club.ui.clubdetail.qna.ClubDetailQna
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ClubDetail(
    initialPage: Int = 0,
    onTopbarBackClick: () -> Unit = {},
    viewModel: ClubDetailViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()

    val detailList = listOf(
        Pair(DETAIL_CATEGORY, state.clubDetails?.category),
        Pair(DETAIL_LOCATION, state.clubDetails?.location),
        Pair(DETAIL_DESCRIPTION, state.clubDetails?.description),
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

    val listState = rememberLazyListState()

    viewModel.collectSideEffect { sideEffect ->
        handleSideEffect(sideEffect, context, snackbarHostState)
    }

    Scaffold(
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = state.clubDetails?.name ?: "",
                onNavigationIconClick = onTopbarBackClick
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
                    DetailSnackBar(
                        message = data.visuals.message,
                        label = data.visuals.actionLabel,
                        onLabelClick = { data.performAction() }
                    )
                }
            )
        }
    ) { contentPadding ->

        if (state.showLoginDialog) {
            DetailLoginDialog(
                title = stringResource(R.string.detail_dialog_login_title),
                description = stringResource(R.string.detail_dialog_login_description),
                onPositive = {
                    viewModel.dismissLoginDialog()
                    viewModel.openUrl(LOGIN_ACTIVITY_URL)
                },
                onNegative = { viewModel.dismissLoginDialog() }
            )
        }

        if (state.showAddQnaDialog) {
            var addQnaText by remember { mutableStateOf("") }
            DetailDialog(
                modifier = Modifier,
                title = stringResource(R.string.detail_add_qna_button),
                onPositive = {
                    viewModel.addClubQna(
                        parentId = null,
                        content = addQnaText
                    )
                },
                onNegative = { viewModel.dismissAddQnaDialog() },
                content = {
                    DetailDialogAddQnaContent(
                        text = addQnaText,
                        onValueChange = { addQnaText = it },
                        isError = state.textFieldErrorMessageResId != null,
                        errorMessage = state.textFieldErrorMessageResId?.let { stringResource(it) } ?: ""
                    )
                }
            )
        }

        if (state.showEmpowermentDialog) {
            var newManagerText by remember { mutableStateOf("") }
            DetailDialog(
                modifier = Modifier,
                title = stringResource(R.string.detail_dialog_empowerment_title),
                onPositive = {
                    viewModel.setManagerEmpowerment(
                        newUserId = newManagerText
                    )
                },
                onNegative = { viewModel.dismissEmpowermentDialog() },
                content = {
                    DetailDialogEmpowermentContent(
                        clubName = state.clubDetails?.name ?: "",
                        managerId = state.userId ?: -1,
                        text = newManagerText,
                        onValueChange = { newManagerText = it },
                        isError = state.textFieldErrorMessageResId != null,
                        errorMessage = state.textFieldErrorMessageResId?.let { stringResource(it) } ?: ""
                    )
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
                .systemBarsPadding()
                .fillMaxSize(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            userScrollEnabled = true
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
                            Text(stringResource(R.string.detail_club_image_error))
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
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        state.userId?.let {
                            if (state.clubDetails?.manager == true) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    FilledButton(
                                        text = stringResource(R.string.detail_empowerment_button),
                                        onClick = { viewModel.showEmpowermentDialog() },
                                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 5.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    FilledButton(
                                        text = stringResource(R.string.detail_fix_button),
                                        onClick = {}, // 동아리 정보 수정 버튼 클릭
                                        contentPadding = PaddingValues(horizontal = 25.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = state.clubDetails?.name ?: "",
                            style = KoinTheme.typography.bold20,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .weight(1f, fill = false),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = if (state.clubDetails?.isLiked == true) painterResource(id = R.drawable.icon_like_true) else painterResource(id = R.drawable.icon_like_false),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 4.dp)
                                    .clickable {
                                        state.userId?.let {
                                            viewModel.changeClubLike()
                                        } ?: viewModel.showLoginDialog()
                                    }
                            )
                            if (state.clubDetails?.isLikedHidden != true) {
                                Text(
                                    text = "${state.clubDetails?.likes}",
                                    style = KoinTheme.typography.medium14
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        detailList.forEach { intro ->
                            var outputText = ""
                            var maxLines = 1
                            var linkUrl = ""
                            intro.second?.let {
                                when (intro.first) {
                                    DETAIL_DESCRIPTION -> {
                                        maxLines = 2
                                        outputText = "${stringResource(intro.first.strResId)}$it"
                                    }
                                    DETAIL_INSTAGRAM -> outputText = it.formatInstagramLinkForm()
                                    DETAIL_GOOGLE_FORM -> outputText = it.removePrefix(HTTPS_URL)
                                    DETAIL_OPEN_CHAT -> outputText = it.removePrefix(HTTPS_URL)
                                    DETAIL_PHONE_NUMBER -> outputText = if (it.isValidPhoneNumber) it.formatPhoneNumber() else it
                                    else -> outputText = it
                                }
                                if (
                                    it.isInstagramUrl() ||
                                    it.isGoogleFormUrl() ||
                                    it.isOpenChatUrl()
                                ) {
                                    linkUrl = it
                                }
                            }
                            Row {
                                Text(
                                    text = if (intro.first != DETAIL_DESCRIPTION) stringResource(intro.first.strResId) else "",
                                    style = KoinTheme.typography.medium18,
                                    color = KoinTheme.colors.neutral800
                                )
                                Text(
                                    text = outputText,
                                    maxLines = maxLines,
                                    style = KoinTheme.typography.medium18,
                                    color = if (linkUrl.isEmpty()) KoinTheme.colors.neutral800 else KoinTheme.colors.info700,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .clickable {
                                            if (linkUrl.isNotEmpty()) viewModel.openUrl(linkUrl)
                                        }
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
                val deviceHeightDp = LocalConfiguration.current.screenHeightDp.dp
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = pagerState,
                    verticalAlignment = Alignment.Top
                ) { page ->
                    when (tabList[page]) {
                        DetailTabType.DETAIL_INTRO.strResId -> {
                            val snackbarMessage = stringResource(R.string.detail_snackbar_detail_intro_text)
                            val snackbarActionLabel = stringResource(R.string.detail_snackbar_detail_intro_button)
                            ClubDetailIntro(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .heightIn(min = deviceHeightDp),
                                onFixIntroClick = {
                                    scope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = snackbarMessage,
                                            actionLabel = snackbarActionLabel,
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            if (BuildConfig.DEBUG) {
                                                viewModel.openUrl(KOIN_WEB_STAGE_URL)
                                            } else {
                                                viewModel.openUrl(KOIN_WEB_URL)
                                            }
                                        }
                                    }
                                },
                                isManager = state.clubDetails?.manager ?: false,
                                userId = state.userId
                            )
                        }
                        DetailTabType.QNA.strResId -> {
                            Box {
                                if (state.showQnasProgressBar) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .heightIn(min = deviceHeightDp)
                                            .zIndex(1f)
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .size(100.dp)
                                                .align(Alignment.Center)
                                        )
                                    }
                                }
                                ClubDetailQna(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .heightIn(min = deviceHeightDp),
                                    qnaList = qnaList,
                                    isManager = state.clubDetails?.manager ?: false,
                                    userId = state.userId,
                                    onAddQnaClick = {
                                        viewModel.showAddQnaDialog()
                                    },
                                    onDeleteQnaClick = { qnaId ->
                                        viewModel.deleteClubQna(qnaId)
                                    },
                                    onAddAnswerClick = { qnaId, content ->
                                        viewModel.addClubQnaAnswer(qnaId, content)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

suspend fun handleSideEffect(
    sideEffect: ClubDetailSideEffect,
    context: Context,
    snackbarHostState: SnackbarHostState
) {
    when (sideEffect) {
        is ClubDetailSideEffect.ShowEmpowermentSnackBar -> {
            val empowermentSuccessMessage = context.getString(R.string.detail_snackbar_empowerment_success)
            snackbarHostState.showSnackbar(
                message = empowermentSuccessMessage,
                duration = SnackbarDuration.Short
            )
        }
        is ClubDetailSideEffect.OpenUrl -> {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(sideEffect.url))
            context.startActivity(intent)
        }
        is ClubDetailSideEffect.UnauthorizedError -> {
            ToastUtil.getInstance().makeShort(sideEffect.messageResId)
        }
        is ClubDetailSideEffect.DeletePermissionDeniedError -> {
            ToastUtil.getInstance().makeShort(sideEffect.messageResId)
        }
        is ClubDetailSideEffect.ClubNotFoundError -> {
            ToastUtil.getInstance().makeShort(sideEffect.messageResId)
        }
        is ClubDetailSideEffect.NotClubManagerError -> {
            ToastUtil.getInstance().makeShort(sideEffect.messageResId)
        }
        is ClubDetailSideEffect.QnaNotFoundError -> {
            ToastUtil.getInstance().makeShort(sideEffect.messageResId)
        }
        is ClubDetailSideEffect.AlreadyLikedError -> {
            ToastUtil.getInstance().makeShort(sideEffect.messageResId)
        }
        is ClubDetailSideEffect.AlreadyNotLikedError -> {
            ToastUtil.getInstance().makeShort(sideEffect.messageResId)
        }
    }
}
