package `in`.koreatech.koin.feature.lostandfound.component

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
fun LostAndFoundKeywordChip(
    title: String,
    backgroundColor: Color,
    textColor: Color,
    iconVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Article keyword screen Material Chip 기본 동등 매칭
    //  - chip height 32dp (Material chipMinHeight)
    //  - start 12dp / end 6dp horizontal padding
    //  - text→icon 간격 8dp / closeIconSize 12dp
    //  - Text 의 lineHeight 1.6× 가 단일 라인에서 vertical padding 처럼 누적되는 것을 막기 위해
    //    명시적 height + LineHeightStyle 로 vertical alignment 처리
    Row(
        modifier = modifier
            .requiredHeight(32.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .noRippleClickable(onClick = onClick)
            .padding(start = 12.dp, end = 6.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f, fill = false),
            text = title,
            style = KoinTheme.typography.medium14.copy(
                lineHeight = 14.sp,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Icon(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(12.dp),
            imageVector = iconVector,
            contentDescription = null,
            tint = textColor
        )
    }
}

@Composable
fun LostAndFoundDeletableKeywordChip(
    title: String,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LostAndFoundKeywordChip(
        title = title,
        backgroundColor = KoinTheme.colors.neutral100,
        textColor = KoinTheme.colors.neutral500,
        iconVector = Icons.Default.Close,
        onClick = { onDelete(title) },
        modifier = modifier
    )
}

@Composable
fun LostAndFoundAddableKeywordChip(
    title: String,
    onAdd: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LostAndFoundKeywordChip(
        title = title,
        backgroundColor = KoinTheme.colors.neutral100,
        textColor = KoinTheme.colors.neutral500,
        iconVector = Icons.Default.Add,
        onClick = { onAdd(title) },
        modifier = modifier
    )
}

@Composable
fun LostAndFoundDeletableChipFlowGroup(
    keywords: ImmutableList<String>,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedKeywordChipFlowGroup(
        items = keywords,
        modifier = modifier
    ) { keyword ->
        LostAndFoundDeletableKeywordChip(title = keyword, onDelete = onDelete)
    }
}

@Composable
fun LostAndFoundAddableChipFlowGroup(
    keywords: ImmutableList<String>,
    onAdd: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedKeywordChipFlowGroup(
        items = keywords,
        modifier = modifier
    ) { keyword ->
        LostAndFoundAddableKeywordChip(title = keyword, onAdd = onAdd)
    }
}

// Article 키워드 화면의 ChipGroup `android:animateLayoutChanges="true"` 와 동일한 Layout
// Transition 타이밍을 재현한다.
//  - APPEARING:           새 chip 페이드인 300ms (start delay 300ms — 형제 위치 이동 후 등장)
//  - DISAPPEARING:        제거되는 chip 페이드아웃 300ms (start delay 0)
//  - CHANGE_APPEARING:    형제 chip 위치 이동 300ms (start delay 0, 즉시)
//  - CHANGE_DISAPPEARING: 형제 chip 위치 이동 300ms (start delay 300ms — 페이드아웃 후 합치기)
//
// Exit fade 를 위해 [items] 에서 빠진 키워드도 fade-out 이 끝날 때까지 [displayedItems] 에
// 남겨둔 뒤, 알파 애니메이션 종료 시점에 컴포지션에서 제거한다. 컴포지션 제거 직후 형제 chip 의
// LookaheadScope 측정값이 변경되며 [Modifier.animateBounds] 가 위치 이동을 보간한다. 결과적으로
// 형제 chip 의 위치 이동은 페이드아웃 종료 시점부터 시작되어 LayoutTransition 의 기본
// CHANGE_DISAPPEARING delay (= 300ms) 와 동일하게 동작한다.
@OptIn(ExperimentalLayoutApi::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun AnimatedKeywordChipFlowGroup(
    items: ImmutableList<String>,
    modifier: Modifier = Modifier,
    chip: @Composable (String) -> Unit
) {
    // 첫 컴포지션 시점에 이미 존재하는 키워드는 entry 애니메이션 없이 즉시 보이도록
    // displayedItems / visibility 를 미리 채워 두고, 첫 프레임이 끝났는지 추적한다.
    // 첫 프레임 이후에 새로 등장하는 칩(또는 제거 후 재추가된 칩)만 fade-in 한다.
    val displayedItems = remember { mutableStateListOf<String>().apply { addAll(items) } }
    val visibility = remember {
        mutableStateMapOf<String, Boolean>().apply { items.forEach { put(it, true) } }
    }
    var hasInitialFrameRendered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { hasInitialFrameRendered = true }

    LaunchedEffect(items) {
        syncDisplayedItems(items, displayedItems, visibility)
    }

    LookaheadScope {
        FlowRow(
            modifier = modifier.animateContentSize(animationSpec = tween(durationMillis = 300)),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            displayedItems.forEach { keyword ->
                key(keyword) {
                    AnimatedKeywordChipSlot(
                        keyword = keyword,
                        visibility = visibility,
                        displayedItems = displayedItems,
                        startVisible = !hasInitialFrameRendered,
                        lookaheadScope = this@LookaheadScope,
                        chip = chip
                    )
                }
            }
        }
    }
}

/**
 * displayedItems 를 새 [items] 와 동기화한다. 새 키워드는 [items] 순서를 유지하도록 삽입하고,
 * [items] 에서 빠진 키워드는 visibility=false 로 표시해 fade-out 을 트리거한다.
 *
 * 단순 append 는 한 번 fade-out 후 사라졌다 되돌아올 때 원래 위치를 잃어버려 추천 키워드의 캐논
 * 순서가 어긋나는 문제를 일으키므로, 인접한 기존 키워드를 기준으로 삽입 위치를 계산한다.
 */
private fun syncDisplayedItems(
    items: ImmutableList<String>,
    displayedItems: MutableList<String>,
    visibility: MutableMap<String, Boolean>
) {
    items.forEachIndexed { itemIdx, key ->
        if (key !in displayedItems) {
            val insertAt = (itemIdx - 1 downTo 0)
                .asSequence()
                .map { displayedItems.indexOf(items[it]) }
                .firstOrNull { it >= 0 }
                ?.let { it + 1 }
                ?: 0
            displayedItems.add(insertAt, key)
        }
        visibility[key] = true
    }
    displayedItems.toList().forEach { key ->
        if (key !in items && visibility[key] != false) visibility[key] = false
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun AnimatedKeywordChipSlot(
    keyword: String,
    visibility: MutableMap<String, Boolean>,
    displayedItems: MutableList<String>,
    startVisible: Boolean,
    lookaheadScope: LookaheadScope,
    chip: @Composable (String) -> Unit
) {
    var hasStartedEntry by remember { mutableStateOf(startVisible) }
    LaunchedEffect(Unit) { hasStartedEntry = true }
    val isVisible = visibility[keyword] ?: true
    val targetAlpha = if (hasStartedEntry && isVisible) 1f else 0f
    val alphaSpec = if (isVisible) {
        tween<Float>(durationMillis = 300, delayMillis = 300)
    } else {
        tween<Float>(durationMillis = 300)
    }
    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = alphaSpec,
        label = "keywordChipAlpha",
        finishedListener = { finalValue ->
            if (finalValue == 0f && visibility[keyword] == false) {
                displayedItems.remove(keyword)
                visibility.remove(keyword)
            }
        }
    )
    Box(
        modifier = Modifier
            .animateBounds(
                lookaheadScope = lookaheadScope,
                boundsTransform = { _, _ -> tween(durationMillis = 300) }
            )
            .graphicsLayer { this.alpha = alpha }
    ) {
        chip(keyword)
    }
}
