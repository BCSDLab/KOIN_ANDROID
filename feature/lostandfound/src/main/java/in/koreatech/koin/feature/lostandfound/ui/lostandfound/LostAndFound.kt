package `in`.koreatech.koin.feature.lostandfound.ui.lostandfound

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.progressdialog.IProgressDialog
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.component.LoadingDialog
import `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component.LostAndFoundDialog
import `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component.LostAndFoundDropdownGroup
import `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component.LostAndFoundFAB
import `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component.LostAndFoundItemList
import `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component.LostAndFoundKeywordGroup
import `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component.LostAndFoundPagination
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import timber.log.Timber

@Composable
fun LostAndFoundList(
    viewModel: LostAndFoundViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navigateToWriteFoundItem: () -> Unit,
    navigateToLostAndFoundDetail: (articleId: Int) -> Unit,
    navigateToKeywordFragment: () -> Unit,
    navigateToLoginActivity: () -> Unit
) {
    val uiState by viewModel.collectAsState()
    viewModel.collectSideEffect { sideEffect ->
        handleSideEffect(sideEffect, viewModel)
    }
    val isLoading = uiState.isLoading
    var showLoginRequestDialog by remember { mutableStateOf(false) }
    var isDialogExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchMyKeyword()
        viewModel.getUserType()
    }

    LaunchedEffect(uiState.selectedKeyword) {
        viewModel.fetchLostAndFoundList()
    }

    KoinTheme {
        Scaffold(
            containerColor = if (isDialogExpanded) Color(0xB2000000) else KoinTheme.colors.neutral0,
            floatingActionButton = {
                val fabWrite = stringResource(R.string.fab_write)

                when (uiState.userType) {
                    "COUNCIL" -> { // If userType is COUNCIL, show FAB for write article
                        LostAndFoundFAB(
                            mainOnClick = {
                                EventLogger.logCampusClickEvent(
                                    AnalyticsConstant.Label.LOST_AND_FOUND.ITEM_WRITE,
                                    fabWrite
                                )
                                navigateToWriteFoundItem()
                            }
                        )
                    }
                    else -> {
                        // Do nothing
                    }
                }

                /* TODO: 2차 배포
                val fabFoundText = stringResource(R.string.fab_found)
                val fabLostText = stringResource(R.string.fab_lost)
                LostAndFoundFAB(
                    modifier = Modifier,
                    isDialogExpanded = isDialogExpanded,
                    dialogExpandButtonText = fabWrite,
                    dialogExpandButtonPainter = painterResource(id = R.drawable.ic_fab_write),
                    firstButtonText = fabFoundText,
                    firstButtonPainter = painterResource(id = R.drawable.ic_found),
                    secondButtonText = fabLostText,
                    secondButtonPainter = painterResource(id = R.drawable.ic_lost),
                    onFirstButtonClick = {
                        if (isAnonymous) {
                            showLoginRequestDialog = true
                        } else {
                            EventLogger.logCampusClickEvent(
                                AnalyticsConstant.Label.LOST_AND_FOUND.FOUND_WRITE,
                                fabFoundText
                            )
                            navigateToWriteFoundItem()
                        }
                    },
                    onSecondButtonClick = {
                        if (isAnonymous) {
                            showLoginRequestDialog = true
                        } else {
                            EventLogger.logCampusClickEvent(
                                AnalyticsConstant.Label.LOST_AND_FOUND.LOST_WRITE,
                                fabLostText
                            )
                            navigateToWriteFoundItem()
                        }
                    },
                    onDialogExpandedChange = {
                        EventLogger.logCampusClickEvent(
                            AnalyticsConstant.Label.LOST_AND_FOUND.ITEM_WRITE,
                            fabWrite
                        )
                        isDialogExpanded = it
                    }
                )
                 */
            }
        ) { contentPadding ->
            val correctedPadding = PaddingValues(
                // Hardcode top padding value to 0.dp because contentPadding returns wrong top padding value
                top = 0.dp,
                bottom = contentPadding.calculateBottomPadding(),
                // TODO: Get correct layout direction from device settings
                start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                end = contentPadding.calculateEndPadding(LayoutDirection.Ltr)
            )

            val myKeywords = uiState.myKeywords
            Column(modifier = modifier.padding(correctedPadding)) {
                LostAndFoundKeywordGroup(
                    keyWords = myKeywords,
                    navigateToKeywordFragment = navigateToKeywordFragment
                ) {
                    viewModel.selectKeyword(it)
                }
                /* TODO: 2차 배포
                LostAndFoundDropdownGroup {}
                */
                LostAndFoundItemList(data = uiState.lostAndFoundList) {
                    navigateToLostAndFoundDetail(it.id)
                }
                LostAndFoundPagination(
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                        .fillMaxWidth(),
                    currentPage = uiState.currentPage,
                    totalPage = uiState.totalPage
                ) {
                    viewModel.changePage(it)
                }

                if (isLoading) {
                    LoadingDialog()
                }

                if (showLoginRequestDialog) {
                    LostAndFoundDialog(
                        title = stringResource(R.string.request_login_dialog_title),
                        titleStyle = KoinTheme.typography.medium18.copy(textAlign = TextAlign.Center),
                        description = stringResource(R.string.request_login_dialog_description),
                        descriptionStyle = KoinTheme.typography.regular14.copy(textAlign = TextAlign.Center),
                        onPositive = {
                            navigateToLoginActivity()
                            showLoginRequestDialog = false
                        },
                        onNegative = {
                            showLoginRequestDialog = false
                        },
                    )
                }
            }
        }

    }
}

fun handleSideEffect(
    sideEffect: LostAndFoundSideEffect,
    viewModel: LostAndFoundViewModel
) {
    when (sideEffect) {
        is LostAndFoundSideEffect.PageChanged -> {
            viewModel.fetchLostAndFoundList()
        }
    }
}

