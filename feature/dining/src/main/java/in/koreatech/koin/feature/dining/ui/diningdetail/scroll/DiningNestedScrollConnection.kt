package `in`.koreatech.koin.feature.dining.ui.diningdetail.scroll

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.State
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import `in`.koreatech.koin.core.nestedscroll.KoinNestedScrollConnection
import `in`.koreatech.koin.core.nestedscroll.KoinNestedScrollHeaderState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

class DiningNestedScrollConnection(
    private val headerState: KoinNestedScrollHeaderState,
    private val coroutineScope: CoroutineScope,
    private val currentScrollState: State<ScrollState>
) : KoinNestedScrollConnection(state = headerState, scope = coroutineScope) {

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        var delta = available.y
        val currentOffset = headerState.headerOffsetPx
        val collapsedOffset = -headerState.range

        if (delta > 0) {
            // scroll up: 현재 구현과 동일 — 리스트 남은 스크롤보다 delta가 크면 차액만큼 toolbar expand
            val scrollState = currentScrollState.value
            val currentScrollPos = scrollState.value
            if (currentScrollPos <= delta) {
                delta -= currentScrollPos
            } else {
                return Offset.Zero
            }
        }

        // scroll down (delta < 0) 또는 scroll up 조건 통과 시 toolbar 이동
        val newOffset = (currentOffset + delta).coerceIn(collapsedOffset, 0f)
        val consumed = newOffset - currentOffset

        if (consumed != 0f) {
            coroutineScope.launch(start = CoroutineStart.UNDISPATCHED) { headerState.snapOffset(newOffset) }
            return Offset(0f, consumed)
        }
        return Offset.Zero
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset = Offset.Zero // onPreScroll에서 모든 방향의 toolbar 이동을 처리하므로 중복 방지

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        // 현재 구현에 onPostFling이 없으므로 snap 동작 비활성화 (동작 보존)
        return Velocity.Zero
    }
}
