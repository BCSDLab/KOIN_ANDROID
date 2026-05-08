package `in`.koreatech.koin.feature.lostandfound.ui.keyword

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.component.LostAndFoundAddableChipFlowGroup
import `in`.koreatech.koin.feature.lostandfound.component.LostAndFoundDeletableChipFlowGroup
import `in`.koreatech.koin.feature.lostandfound.ui.list.LostAndFoundListSideEffect
import `in`.koreatech.koin.feature.lostandfound.ui.list.LostAndFoundListState
import `in`.koreatech.koin.feature.lostandfound.ui.list.LostAndFoundListViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LostAndFoundKeyword(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: LostAndFoundListViewModel
) {
    BackHandler(onBack = onBackClick)

    val uiState by viewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LostAndFoundListSideEffect.ShowSnackbar -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(context.getString(sideEffect.messageResId))
                }
            }
            LostAndFoundListSideEffect.FetchData -> { /* 목록 화면 전용 */ }
            is LostAndFoundListSideEffect.UpdateSignInDialog -> { /* 목록 화면 전용 */ }
        }
    }

    LostAndFoundKeywordContent(
        modifier = modifier,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onKeywordInputChanged = viewModel::onKeywordInputChanged,
        onAddKeyword = viewModel::addKeyword,
        onDeleteKeyword = viewModel::deleteKeyword,
        onAddSuggestedKeyword = viewModel::addSuggestedKeyword,
        onToggleNotification = viewModel::toggleNotification,
        suggestedKeywords = LostAndFoundListViewModel.SUGGESTED_KEYWORDS
    )
}

@Composable
private fun LostAndFoundKeywordContent(
    modifier: Modifier = Modifier,
    uiState: LostAndFoundListState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onKeywordInputChanged: (String) -> Unit,
    onAddKeyword: (String) -> Unit,
    onDeleteKeyword: (String) -> Unit,
    onAddSuggestedKeyword: (String) -> Unit,
    onToggleNotification: () -> Unit,
    suggestedKeywords: ImmutableList<String>
) {
    val focusManager = LocalFocusManager.current
    val buttonShape = remember { androidx.compose.foundation.shape.RoundedCornerShape(8.dp) }

    Scaffold(
        modifier = modifier,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.lost_and_found_keyword_management),
                onNavigationIconClick = onBackClick
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                // 내 키워드 섹션
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.lost_and_found_my_keyword_count, uiState.keywords.size),
                    style = KoinTheme.typography.bold16,
                    color = KoinTheme.colors.neutral800
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.keyword_notice_description),
                    style = KoinTheme.typography.medium14,
                    color = KoinTheme.colors.neutral600
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 키워드 입력 필드
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.keywordInput,
                    onValueChange = onKeywordInputChanged,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.lost_and_found_keyword_input_hint),
                            style = KoinTheme.typography.medium14,
                            color = KoinTheme.colors.neutral400
                        )
                    },
                    shape = buttonShape,
                    textStyle = KoinTheme.typography.medium14,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (uiState.keywordInput.isNotBlank()) {
                                onAddKeyword(uiState.keywordInput)
                            }
                            focusManager.clearFocus()
                        }
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 추가 버튼
                val isAddButtonEnabled = uiState.keywordInput.isNotBlank()
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = {
                        onAddKeyword(uiState.keywordInput)
                        focusManager.clearFocus()
                    },
                    enabled = isAddButtonEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = KoinTheme.colors.primary500,
                        contentColor = KoinTheme.colors.neutral0,
                        disabledContainerColor = KoinTheme.colors.neutral100,
                        disabledContentColor = KoinTheme.colors.neutral500
                    ),
                    shape = buttonShape
                ) {
                    Text(
                        text = stringResource(R.string.add_keyword),
                        style = KoinTheme.typography.bold14
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 등록된 키워드 칩
                if (uiState.keywords.isNotEmpty()) {
                    LostAndFoundDeletableChipFlowGroup(
                        keywords = uiState.keywords,
                        onDelete = onDeleteKeyword
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 추천 키워드 섹션
                Text(
                    text = stringResource(R.string.suggestion_keywords),
                    style = KoinTheme.typography.bold16,
                    color = KoinTheme.colors.neutral800
                )

                Spacer(modifier = Modifier.height(16.dp))

                LostAndFoundAddableChipFlowGroup(
                    keywords = suggestedKeywords,
                    onAdd = onAddSuggestedKeyword
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Divider
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = KoinTheme.colors.neutral200,
                thickness = 1.dp
            )

            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // 키워드 알림 섹션
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = uiState.isNotificationEnabled,
                            role = Role.Switch,
                            onValueChange = { onToggleNotification() }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.keyword_notification),
                            style = KoinTheme.typography.bold16,
                            color = KoinTheme.colors.neutral800
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(R.string.lost_and_found_keyword_notification_description),
                            style = KoinTheme.typography.medium12,
                            color = KoinTheme.colors.neutral600
                        )
                    }

                    Switch(
                        checked = uiState.isNotificationEnabled,
                        onCheckedChange = null
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview(name = "키워드 없음", showBackground = true)
@Composable
private fun LostAndFoundKeywordContentEmptyPreview() {
    KoinTheme {
        LostAndFoundKeywordContent(
            uiState = LostAndFoundListState(),
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {},
            onKeywordInputChanged = {},
            onAddKeyword = {},
            onDeleteKeyword = {},
            onAddSuggestedKeyword = {},
            onToggleNotification = {},
            suggestedKeywords = LostAndFoundListViewModel.SUGGESTED_KEYWORDS
        )
    }
}

@Preview(name = "키워드 있음", showBackground = true)
@Composable
private fun LostAndFoundKeywordContentWithKeywordsPreview() {
    KoinTheme {
        LostAndFoundKeywordContent(
            uiState = LostAndFoundListState(
                keywords = persistentListOf("지갑", "학생증", "에어팟", "태블릿"),
                isNotificationEnabled = true
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {},
            onKeywordInputChanged = {},
            onAddKeyword = {},
            onDeleteKeyword = {},
            onAddSuggestedKeyword = {},
            onToggleNotification = {},
            suggestedKeywords = LostAndFoundListViewModel.SUGGESTED_KEYWORDS
        )
    }
}
