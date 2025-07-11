package `in`.koreatech.koin.feature.club.ui.clubdetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.domain.constant.HTTPS_URL
import `in`.koreatech.koin.domain.constant.KOIN_WEB_STAGE_URL
import `in`.koreatech.koin.domain.constant.KOIN_WEB_URL
import `in`.koreatech.koin.domain.constant.LOGIN_ACTIVITY_URL
import `in`.koreatech.koin.domain.util.ext.formatInstagramLinkForm
import `in`.koreatech.koin.domain.util.ext.formatInstagramUrlForm
import `in`.koreatech.koin.domain.util.ext.formatPhoneNumber
import `in`.koreatech.koin.domain.util.ext.isValidGoogleFormUrl
import `in`.koreatech.koin.domain.util.ext.isValidInstagramUrl
import `in`.koreatech.koin.domain.util.ext.isValidOpenChatUrl
import `in`.koreatech.koin.domain.util.ext.isValidPhoneNumber
import `in`.koreatech.koin.feature.club.BuildConfig
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.component.DetailDialog
import `in`.koreatech.koin.feature.club.component.DetailLoginDialog
import `in`.koreatech.koin.feature.club.component.KoinClubExtraSmallDialog
import `in`.koreatech.koin.feature.club.component.getKoinClubCancelButtonColor
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_CATEGORY
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_DESCRIPTION
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_GOOGLE_FORM
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_INSTAGRAM
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_LOCATION
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_OPEN_CHAT
import `in`.koreatech.koin.feature.club.type.DetailIntroType.DETAIL_PHONE_NUMBER
import `in`.koreatech.koin.feature.club.type.DetailTabType
import `in`.koreatech.koin.feature.club.ui.clubdetail.component.dialog.DetailImageDialog
import `in`.koreatech.koin.feature.club.ui.clubdetail.component.dialog.content.DetailDialogAddQnaContent
import `in`.koreatech.koin.feature.club.ui.clubdetail.component.dialog.content.DetailDialogEmpowermentContent
import `in`.koreatech.koin.feature.club.ui.clubdetail.component.snackbar.DetailSnackBar
import `in`.koreatech.koin.feature.club.ui.clubdetail.component.tabrow.DetailTabRow
import `in`.koreatech.koin.feature.club.ui.clubdetail.intro.ClubDetailIntro
import `in`.koreatech.koin.feature.club.ui.clubdetail.qna.ClubDetailQna
import `in`.koreatech.koin.feature.club.ui.clubdetail.recruit.ClubDetailRecruit
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ClubDetail(
    isClubModified: Boolean = false,
    initialPage: Int = 0,
    viewModel: ClubDetailViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {},
    onModifyClick: (Int) -> Unit = {},
    onRecruitCreateClick: (Int) -> Unit = {},
    resetClubModifiedState: () -> Unit = {}
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

    val qnaScrollState = rememberScrollState()
    val isQnaScrollable = remember { derivedStateOf { !listState.canScrollForward || qnaScrollState.value != 0 } }

    viewModel.collectSideEffect { sideEffect ->
        handleSideEffect(sideEffect, context, snackbarHostState)
    }

    LaunchedEffect(Unit, state.clubId) {
        if (state.clubId != -1) {
            viewModel.fetchAllData()
        }
    }

    LaunchedEffect(isClubModified) {
        if (isClubModified) {
            snackbarHostState.showSnackbar(
                message = context.getString(R.string.club_modify_success_snackbar),
                duration = SnackbarDuration.Short
            )
            resetClubModifiedState()
        }
    }

    Scaffold(
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                modifier = Modifier,
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
                        qnaScrollState.scrollTo(0)
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

        if (state.clubDetails == null) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

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
                title = stringResource(R.string.detail_add_qna_button),
                onPositive = {
                    EventLogger.logCampusClickEvent(
                        AnalyticsConstant.Label.Club.CLUB_QNA_ADD_CONFIRM,
                        "Q&A"
                    )
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
                        managerId = state.userLoginId ?: "",
                        text = newManagerText,
                        onValueChange = { newManagerText = it.trim() },
                        isError = state.textFieldErrorMessageResId != null,
                        errorMessage = state.textFieldErrorMessageResId?.let { stringResource(it) } ?: ""
                    )
                }
            )
        }

        if (state.showImageDialog) {
            DetailImageDialog(
                imageModel = ImageRequest.Builder(context)
                    .data(state.imageDialogUrl)
                    .size(400)
                    .build(),
                onDismiss = { viewModel.dismissImageDialog() }
            )
        }

        if (state.showRecruitDeleteDialog) {
            KoinClubExtraSmallDialog(
                description = "모집을 삭제하시겠어요?\n모집이 삭제되며 되돌릴 수 없어요.",
                descriptionStyle = KoinTheme.typography.medium15,
                descriptionColor = KoinTheme.colors.neutral600,
                positiveButtonText = stringResource(R.string.detail_recruit_delete_dialog_positive),
                negativeButtonText = stringResource(R.string.detail_recruit_delete_dialog_negative),
                positiveButtonColors = getKoinClubCancelButtonColor(),
                onPositive = {
                    viewModel.dismissRecruitDeleteDialog()
                },
                onNegative = viewModel::dismissRecruitDeleteDialog,
                onDismiss = viewModel::dismissRecruitDeleteDialog
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
            userScrollEnabled = qnaScrollState.value == 0
        ) {
            item {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .size(200.dp)
                        .clickable {
                            viewModel.showImageDialog(state.clubDetails?.imageUrl ?: "")
                        },
                    model = ImageRequest.Builder(context)
                        .data(state.clubDetails?.imageUrl)
                        .size(400)
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
                                        onClick = {
                                            viewModel.showEmpowermentDialog()
                                            EventLogger.logCampusClickEvent(
                                                AnalyticsConstant.Label.Club.CLUB_DELEGATION_AUTHORITY,
                                                "권한위임"
                                            )
                                        },
                                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 5.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    FilledButton(
                                        text = stringResource(R.string.detail_fix_button),
                                        onClick = {
                                            EventLogger.logCampusClickEvent(
                                                AnalyticsConstant.Label.Club.CLUB_CORRECTION,
                                                "수정하기"
                                            )
                                            onModifyClick(state.clubId)
                                        }, // 동아리 정보 수정 버튼 클릭
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
                            state.clubDetails?.hotStatus?.let {
                                Row(
                                    modifier = Modifier
                                        .background(
                                            color = KoinTheme.colors.primary100,
                                            shape = KoinTheme.shapes.extraSmall
                                        )
                                        .padding(vertical = 7.dp, horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(
                                            R.string.detail_hotStatus_club_intro,
                                            state.clubDetails?.hotStatus?.month ?: 0,
                                            state.clubDetails?.hotStatus?.weekOfMonth ?: 0
                                        ),
                                        style = KoinTheme.typography.regular10.copy(fontSize = 11.sp),
                                        color = KoinTheme.colors.neutral800
                                    )
                                }
                            }
                            Spacer(Modifier.width(8.dp))
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
                            if (state.clubDetails?.isLikeHidden != true) {
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
                            if (intro.second.isNullOrBlank()) return@forEach
                            var outputText = ""
                            var linkUrl = ""
                            val showMore = remember { mutableStateOf(false) }
                            var icon = -1
                            var onClick = {}
                            var onIconClick = {}
                            intro.second?.let {
                                when (intro.first) {
                                    DETAIL_DESCRIPTION -> {
                                        outputText = "${stringResource(intro.first.strResId)}$it"
                                        onClick = { showMore.value = !showMore.value }
                                    }
                                    DETAIL_INSTAGRAM -> {
                                        linkUrl = if (it.isValidInstagramUrl()) it else it.formatInstagramUrlForm()
                                        onClick = { viewModel.openUrl(linkUrl) }
                                        outputText = it.formatInstagramLinkForm()
                                    }
                                    DETAIL_GOOGLE_FORM -> outputText = it.removePrefix(HTTPS_URL)
                                    DETAIL_OPEN_CHAT -> outputText = it.removePrefix(HTTPS_URL)
                                    DETAIL_PHONE_NUMBER -> {
                                        outputText = if (it.isValidPhoneNumber) it.formatPhoneNumber() else it
                                        icon = R.drawable.icon_phonenumber_copy
                                        val clipboard = LocalClipboardManager.current
                                        onIconClick = { clipboard.setText(AnnotatedString(outputText)) }
                                    }
                                    else -> outputText = it
                                }
                                if (
                                    it.isValidGoogleFormUrl() ||
                                    it.isValidOpenChatUrl()
                                ) {
                                    linkUrl = it
                                    onClick = { if (linkUrl.isNotEmpty()) viewModel.openUrl(linkUrl) }
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (intro.first != DETAIL_DESCRIPTION) stringResource(intro.first.strResId) else "",
                                    style = KoinTheme.typography.medium18,
                                    color = KoinTheme.colors.neutral800
                                )
                                Text(
                                    text = outputText,
                                    maxLines = if (intro.first == DETAIL_DESCRIPTION) if (showMore.value) 10 else 2 else 1,
                                    style = KoinTheme.typography.medium18,
                                    color = if (linkUrl.isEmpty()) KoinTheme.colors.neutral800 else KoinTheme.colors.info700,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.clickable { onClick() }
                                )
                                if (icon != -1) {
                                    Spacer(Modifier.width(8.dp))
                                    Image(
                                        painter = painterResource(id = icon),
                                        contentDescription = "Phone Number Copy Icon",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(end = 4.dp)
                                            .clickable {
                                                onIconClick()
                                            }
                                    )
                                }
                            }
                        }
                        if (state.clubDetails?.manager == false) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.detail_intro_notification),
                                    style = KoinTheme.typography.medium18,
                                    color = KoinTheme.colors.neutral800
                                )
                                Spacer(Modifier.width(8.dp))
                                Image(
                                    painter = if (state.clubDetails?.isRecruitSubscribed == true) {
                                        painterResource(R.drawable.icon_notification_true)
                                    } else {
                                        painterResource(R.drawable.icon_notification_false)
                                    },
                                    contentDescription = "Notification Icon",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(end = 4.dp)
                                        .clickable { } // TODO club notification
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
                        EventLogger.logCampusClickEvent(
                            AnalyticsConstant.Label.Club.CLUB_TAB_SELECT,
                            context.getString(tabList[it])
                        )
                        scope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    },
                    titles = tabList.map { stringResource(it) }
                )
            }
            item {
                val deviceHeightDp = LocalConfiguration.current.screenHeightDp.dp - (contentPadding.calculateTopPadding().value.dp + 24.dp)
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(deviceHeightDp),
                    state = pagerState,
                    verticalAlignment = Alignment.Top
                ) { page ->
                    when (tabList[page]) {
                        DetailTabType.DETAIL_INTRO.strResId -> {
                            val snackbarMessage = stringResource(R.string.detail_snackbar_detail_intro_text)
                            val snackbarActionLabel = stringResource(R.string.detail_snackbar_detail_intro_button)
                            ClubDetailIntro(
                                modifier = Modifier
                                    .fillMaxSize(),
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
                        DetailTabType.RECRUIT.strResId -> {
                            ClubDetailRecruit(
                                recruitment = state.clubRecruitment,
                                showProgressBar = state.showRecruitProgressBar,
                                onImageClick = viewModel::showImageDialog,
                                onRecruitCreateClick = { onRecruitCreateClick(state.clubId) },
                                showRecruitDeleteDialog = viewModel::showRecruitDeleteDialog,
                                isManager = state.clubDetails?.manager ?: false
                            )
                        }
                        DetailTabType.EVENT.strResId -> {
                        }
                        DetailTabType.QNA.strResId -> {
                            ClubDetailQna(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(qnaScrollState, enabled = isQnaScrollable.value),
                                qnaList = qnaList,
                                isManager = state.clubDetails?.manager ?: false,
                                userId = state.userId,
                                showProgressBar = state.showQnasProgressBar,
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
