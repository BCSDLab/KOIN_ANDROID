package `in`.koreatech.koin.feature.lostandfound.ui.keyword

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.component.LostAndFoundAddableChipFlowGroup
import `in`.koreatech.koin.feature.lostandfound.component.LostAndFoundDeletableChipFlowGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

private val ButtonShape = RoundedCornerShape(4.dp)

@Composable
fun LostAndFoundKeyword(
    modifier: Modifier = Modifier,
    onBackClick: (keywords: ImmutableList<String>) -> Unit,
    viewModel: LostAndFoundKeywordViewModel = hiltViewModel()
) {
    val uiState by viewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val keywords = uiState.keywords

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LostAndFoundKeywordSideEffect.ShowSnackbar -> {
                coroutineScope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(context.getString(sideEffect.messageResId))
                }
            }
        }
    }

    BackHandler {
        onBackClick(keywords)
    }

    // Article keyword 화면처럼, 이미 등록된 키워드는 추천 키워드에서 제거
    val availableSuggestions = remember(keywords) {
        (LostAndFoundKeywordViewModel.SUGGESTED_KEYWORDS - keywords.toSet()).toPersistentList()
    }
    LostAndFoundKeywordContent(
        modifier = modifier,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = { onBackClick(keywords) },
        onKeywordInputChanged = viewModel::onKeywordInputChanged,
        onAddKeyword = viewModel::addKeyword,
        onDeleteKeyword = viewModel::deleteKeyword,
        onAddSuggestedKeyword = viewModel::addSuggestedKeyword,
        onToggleNotification = viewModel::toggleNotification,
        suggestedKeywords = availableSuggestions
    )
}

@Suppress("LongParameterList")
@Composable
private fun LostAndFoundKeywordContent(
    modifier: Modifier = Modifier,
    uiState: LostAndFoundKeywordState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onKeywordInputChanged: (String) -> Unit,
    onAddKeyword: (String) -> Unit,
    onDeleteKeyword: (String) -> Unit,
    onAddSuggestedKeyword: (String) -> Unit,
    onToggleNotification: (Boolean) -> Unit,
    suggestedKeywords: ImmutableList<String>
) {
    Scaffold(
        modifier = modifier,
        containerColor = KoinTheme.colors.neutral0,
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
            MyKeywordSection(
                keywords = uiState.keywords,
                keywordInput = uiState.keywordInput,
                onKeywordInputChanged = onKeywordInputChanged,
                onAddKeyword = onAddKeyword,
                onDeleteKeyword = onDeleteKeyword,
                modifier = Modifier.fillMaxWidth()
            )

            SuggestedKeywordSection(
                suggestedKeywords = suggestedKeywords,
                onAdd = onAddSuggestedKeyword,
                modifier = Modifier.fillMaxWidth()
            )

            // Section divider (Figma: 6dp neutral100)
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = KoinTheme.colors.neutral100,
                thickness = 6.dp
            )

            KeywordNotificationSection(
                isEnabled = uiState.isNotificationEnabled,
                onToggle = onToggleNotification,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun MyKeywordSection(
    keywords: ImmutableList<String>,
    keywordInput: String,
    onKeywordInputChanged: (String) -> Unit,
    onAddKeyword: (String) -> Unit,
    onDeleteKeyword: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.padding(horizontal = 24.dp)
    ) {
        // 내 키워드 섹션 (Figma 51322:186002, SemiBold 18 + Regular 14 supplementary)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.lost_and_found_my_keyword_label),
                style = KoinTheme.typography.bold18,
                color = KoinTheme.colors.neutral800
            )
            Text(
                text = stringResource(R.string.lost_and_found_my_keyword_count, keywords.size),
                style = KoinTheme.typography.regular14,
                color = KoinTheme.colors.neutral500
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = stringResource(R.string.keyword_notice_description),
            style = KoinTheme.typography.medium12,
            color = KoinTheme.colors.neutral500
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 키워드 입력 + 추가 버튼 (수평 배치, Figma 51322:185997)
        val isAddButtonEnabled = keywordInput.isNotBlank()
        val interactionSource = remember { MutableInteractionSource() }
        val isFocused by interactionSource.collectIsFocusedAsState()
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BasicTextField(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 38.dp)
                    .background(KoinTheme.colors.neutral100, ButtonShape)
                    .border(
                        width = 1.dp,
                        color = if (isFocused) KoinTheme.colors.primary400 else KoinTheme.colors.neutral100,
                        shape = ButtonShape
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                value = keywordInput,
                onValueChange = onKeywordInputChanged,
                textStyle = KoinTheme.typography.medium14.copy(color = KoinTheme.colors.neutral800),
                singleLine = true,
                cursorBrush = SolidColor(KoinTheme.colors.primary400),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (keywordInput.isNotBlank()) {
                            onAddKeyword(keywordInput)
                        }
                        focusManager.clearFocus()
                    }
                ),
                interactionSource = interactionSource,
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (keywordInput.isEmpty()) {
                            Text(
                                text = stringResource(R.string.lost_and_found_keyword_input_hint),
                                style = KoinTheme.typography.medium14,
                                color = KoinTheme.colors.neutral500
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Button(
                modifier = Modifier.height(38.dp),
                onClick = {
                    onAddKeyword(keywordInput)
                    focusManager.clearFocus()
                },
                enabled = isAddButtonEnabled,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = KoinTheme.colors.primary500,
                    contentColor = KoinTheme.colors.neutral0,
                    disabledContainerColor = KoinTheme.colors.neutral100,
                    disabledContentColor = KoinTheme.colors.neutral500
                ),
                shape = ButtonShape
            ) {
                Text(
                    text = stringResource(R.string.add_keyword),
                    style = KoinTheme.typography.medium13
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 등록된 키워드 칩
        if (keywords.isNotEmpty()) {
            LostAndFoundDeletableChipFlowGroup(
                keywords = keywords,
                onDelete = onDeleteKeyword
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SuggestedKeywordSection(
    suggestedKeywords: ImmutableList<String>,
    onAdd: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // 추천 키워드 섹션 (Figma 51322:186023, Medium 16)
        Text(
            text = stringResource(R.string.suggestion_keywords),
            style = KoinTheme.typography.medium16,
            color = KoinTheme.colors.neutral800
        )

        Spacer(modifier = Modifier.height(8.dp))

        LostAndFoundAddableChipFlowGroup(
            keywords = suggestedKeywords,
            onAdd = onAdd
        )
    }
}

@Composable
private fun KeywordNotificationSection(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // "키워드 알림" 섹션 헤더 (Figma 51322:186042)
        Text(
            text = stringResource(R.string.lost_and_found_keyword_notification_section_title),
            style = KoinTheme.typography.bold18,
            color = KoinTheme.colors.neutral800,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        )

        // 알림 토글 행 (Figma 51322:186044: 양 끝 정렬, 24x/16y padding, bottom border)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = isEnabled,
                    role = Role.Switch,
                    onValueChange = onToggle
                )
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.lost_and_found_keyword_notification_title),
                    style = KoinTheme.typography.medium16,
                    color = KoinTheme.colors.neutral800
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = stringResource(R.string.lost_and_found_keyword_notification_description),
                    style = KoinTheme.typography.medium12,
                    color = KoinTheme.colors.neutral500
                )
            }

            KoinKeywordSwitch(checked = isEnabled)
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = KoinTheme.colors.neutral100,
            thickness = 1.dp
        )
    }
}

/**
 * Mirrors the article keyword screen's SwitchCompat style:
 * - Track 52x24dp, 50dp radius (gray3 OFF / primary500 ON)
 * - Thumb 24x24dp circle with 3dp ring (gray10 fill + gray3 ring OFF / white fill + primary500 ring ON)
 */
@Composable
private fun KoinKeywordSwitch(checked: Boolean) {
    val trackColor = if (checked) KoinTheme.colors.primary500 else KeywordSwitchTrackOff
    val thumbFill = if (checked) KoinTheme.colors.neutral0 else KeywordSwitchThumbOffFill
    val thumbRing = if (checked) KoinTheme.colors.primary500 else KeywordSwitchTrackOff
    val thumbOffsetX by animateDpAsState(
        targetValue = if (checked) 28.dp else 0.dp,
        label = "thumbOffsetX"
    )
    Box(
        modifier = Modifier
            .size(width = 52.dp, height = 24.dp)
            .clip(RoundedCornerShape(50))
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffsetX)
                .size(24.dp)
                .clip(CircleShape)
                .border(width = 3.dp, color = thumbRing, shape = CircleShape)
                .background(thumbFill, CircleShape)
        )
    }
}

private val KeywordSwitchTrackOff = Color(0xFFE3E3E3)
private val KeywordSwitchThumbOffFill = Color(0xFF828282)

@Composable
private fun PreviewLostAndFoundKeywordContent(
    uiState: LostAndFoundKeywordState
) {
    KoinTheme {
        LostAndFoundKeywordContent(
            uiState = uiState,
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {},
            onKeywordInputChanged = {},
            onAddKeyword = {},
            onDeleteKeyword = {},
            onAddSuggestedKeyword = {},
            onToggleNotification = {},
            suggestedKeywords = LostAndFoundKeywordViewModel.SUGGESTED_KEYWORDS
        )
    }
}

@Preview(name = "키워드 없음", showBackground = true)
@Composable
private fun LostAndFoundKeywordContentEmptyPreview() {
    PreviewLostAndFoundKeywordContent(LostAndFoundKeywordState())
}

@Preview(name = "키워드 있음", showBackground = true)
@Composable
private fun LostAndFoundKeywordContentWithKeywordsPreview() {
    PreviewLostAndFoundKeywordContent(
        LostAndFoundKeywordState(
            keywords = persistentListOf("지갑", "학생증", "에어팟", "태블릿"),
            isNotificationEnabled = true
        )
    )
}

@Preview(name = "입력 활성화", showBackground = true)
@Composable
private fun LostAndFoundKeywordContentInputActivePreview() {
    PreviewLostAndFoundKeywordContent(
        LostAndFoundKeywordState(
            keywordInput = "아이패드",
            keywords = persistentListOf("지갑")
        )
    )
}
