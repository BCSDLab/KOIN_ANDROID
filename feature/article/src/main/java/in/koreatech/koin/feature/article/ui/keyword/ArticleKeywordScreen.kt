package `in`.koreatech.koin.feature.article.ui.keyword

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.feature.article.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ArticleKeywordScreen(
    onNavigateBack: () -> Unit,
    viewModel: ArticleKeywordViewModel = hiltViewModel()
) {
    val myKeywords by viewModel.myKeywords.collectAsStateWithLifecycle()
    val suggestedKeywords by viewModel.suggestedKeywords.collectAsStateWithLifecycle()
    val keywordInputUiState by viewModel.keywordInputUiState.collectAsStateWithLifecycle()
    val notificationUiState by viewModel.notificationUiState.collectAsStateWithLifecycle()

    val inputKeyword = when (val state = keywordInputUiState) {
        is KeywordInputUiState.Valid -> state.keyword
        else -> ""
    }
    val isInputValid = keywordInputUiState is KeywordInputUiState.Valid

    var isNotificationEnabled by remember { mutableStateOf(false) }
    LaunchedEffect(notificationUiState) {
        isNotificationEnabled = (notificationUiState as? NotificationUiState.Success)
            ?.notificationPermissionInfo
            ?.subscribes
            ?.find { it.type == SubscribesType.ARTICLE_KEYWORD }
            ?.isPermit == true
    }

    LaunchedEffect(Unit) {
        viewModel.getPermissionInfo()
    }

    Scaffold(
        topBar = { ArticleKeywordTopBar(onNavigateBack = onNavigateBack) },
        containerColor = RebrandKoinTheme.colors.neutral0,
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.my_keyword),
                        style = RebrandKoinTheme.typography.bold18,
                        color = RebrandKoinTheme.colors.neutral800
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${myKeywords.size}/${ArticleKeywordViewModel.MAX_KEYWORD_COUNT}",
                        style = RebrandKoinTheme.typography.regular14,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                }
            }

            item {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = 4.dp),
                    text = stringResource(R.string.keyword_notice_description),
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral500
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 36.dp, top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = inputKeyword,
                        onValueChange = viewModel::onKeywordInputChanged,
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, RebrandKoinTheme.colors.neutral300, RoundedCornerShape(50))
                            .clip(RoundedCornerShape(50))
                            .background(RebrandKoinTheme.colors.neutral50)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        textStyle = RebrandKoinTheme.typography.regular14.copy(
                            color = RebrandKoinTheme.colors.neutral800
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { viewModel.addKeyword(inputKeyword) }
                        ),
                        decorationBox = { innerTextField ->
                            Box {
                                if (inputKeyword.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.hint_input_keyword),
                                        style = RebrandKoinTheme.typography.regular14,
                                        color = RebrandKoinTheme.colors.neutral400
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                    Button(
                        onClick = { viewModel.addKeyword(inputKeyword) },
                        enabled = isInputValid,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RebrandKoinTheme.colors.primary600,
                            disabledContainerColor = RebrandKoinTheme.colors.neutral200
                        ),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.add_keyword),
                            style = RebrandKoinTheme.typography.medium14
                        )
                    }
                }
            }

            if (myKeywords.isNotEmpty()) {
                item {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        myKeywords.forEach { keyword ->
                            MyKeywordChip(
                                keyword = keyword,
                                onDelete = { viewModel.deleteKeyword(keyword) }
                            )
                        }
                    }
                }
            }

            if (suggestedKeywords.isNotEmpty()) {
                item {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp),
                        text = stringResource(R.string.suggestion_keywords),
                        style = RebrandKoinTheme.typography.bold16,
                        color = RebrandKoinTheme.colors.neutral800
                    )
                }
                item {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        suggestedKeywords
                            .filter { !myKeywords.contains(it) }
                            .forEach { keyword ->
                                SuggestedKeywordChip(
                                    keyword = keyword,
                                    onAdd = { viewModel.addKeyword(keyword) }
                                )
                            }
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(RebrandKoinTheme.colors.neutral100)
                )
            }

            item {
                KeywordNotificationSection(
                    isEnabled = isNotificationEnabled,
                    onToggle = { enabled ->
                        isNotificationEnabled = enabled
                        if (enabled) {
                            viewModel.updateSubscription(SubscribesType.ARTICLE_KEYWORD)
                        } else {
                            viewModel.deleteSubscription(SubscribesType.ARTICLE_KEYWORD)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ArticleKeywordTopBar(onNavigateBack: () -> Unit) {
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
            text = stringResource(R.string.navigation_title_article_keyword),
            style = RebrandKoinTheme.typography.bold18
        )
    }
}

@Composable
private fun KeywordNotificationSection(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp),
            text = stringResource(R.string.keyword_notification),
            style = RebrandKoinTheme.typography.bold18,
            color = RebrandKoinTheme.colors.neutral800
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.get_keyword_notification),
                style = RebrandKoinTheme.typography.medium16,
                color = RebrandKoinTheme.colors.neutral700
            )
            ArticleKeywordSwitch(
                checked = isEnabled,
                onCheckedChange = { onToggle(!isEnabled) }
            )
        }
        Text(
            text = stringResource(R.string.keyword_notification_notice_description),
            style = RebrandKoinTheme.typography.regular12,
            color = RebrandKoinTheme.colors.neutral500
        )
    }
}

@Composable
private fun ArticleKeywordSwitch(
    checked: Boolean,
    onCheckedChange: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (checked) RebrandKoinTheme.colors.primary600 else RebrandKoinTheme.colors.neutral300,
        label = "switchBackground"
    )
    val thumbStart by animateDpAsState(
        targetValue = if (checked) 24.dp else 0.dp,
        label = "thumbStart"
    )
    val thumbEnd by animateDpAsState(
        targetValue = if (checked) 0.dp else 24.dp,
        label = "thumbEnd"
    )

    Row(
        modifier = Modifier
            .width(46.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onCheckedChange
            )
            .background(backgroundColor, RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
            .padding(3.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(start = thumbStart, end = thumbEnd)
                .size(16.dp)
                .clip(RoundedCornerShape(50))
                .background(RebrandKoinTheme.colors.neutral0)
        )
    }
}

@Composable
private fun MyKeywordChip(keyword: String, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(RebrandKoinTheme.colors.neutral100)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = keyword,
            style = RebrandKoinTheme.typography.regular14,
            color = RebrandKoinTheme.colors.neutral500
        )
        Icon(
            painter = painterResource(R.drawable.ic_keyword_delete),
            contentDescription = null,
            modifier = Modifier
                .size(18.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onDelete
                ),
            tint = RebrandKoinTheme.colors.neutral500
        )
    }
}

@Composable
private fun SuggestedKeywordChip(keyword: String, onAdd: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(RebrandKoinTheme.colors.neutral100)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onAdd
            )
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = keyword,
            style = RebrandKoinTheme.typography.regular14,
            color = RebrandKoinTheme.colors.neutral500
        )
        Icon(
            painter = painterResource(R.drawable.ic_keyword_add),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = RebrandKoinTheme.colors.neutral500
        )
    }
}
