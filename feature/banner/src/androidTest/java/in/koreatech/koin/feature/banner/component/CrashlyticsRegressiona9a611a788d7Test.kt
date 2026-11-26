package `in`.koreatech.koin.feature.banner.component

import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewRootForTest
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import `in`.koreatech.koin.feature.banner.model.LocalBanner
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CrashlyticsRegressiona9a611a788d7Test {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val banners = persistentListOf(LocalBanner(id = 1), LocalBanner(id = 2), LocalBanner(id = 3))

    @Test
    fun finitePagerWrapsBothDirectionsAndAutoAdvances() {
        ActivityScenario.launch(ComponentActivity::class.java).use { scenario ->
            lateinit var view: ComposeView
            val indices = mutableListOf<Int>()
            scenario.onActivity { activity ->
                view = ComposeView(activity).apply {
                    setContent { BannerImage(banners, 0, onBannerIndexChange = { indices.add(it) }) }
                }
                activity.setContentView(view, ViewGroup.LayoutParams(600, 240))
            }
            await { pager(view) != null && indices.isNotEmpty() }
            onMain {
                assertEquals(5, pager(view)!!.config[SemanticsProperties.CollectionInfo].columnCount)
                assertEquals(0, indices.last())
                assertTrue(pager(view)!!.config[SemanticsActions.ScrollToIndex].action!!.invoke(0))
            }
            await { indices.last() == 2 && pagePosition(view) > 2.5f }
            onMain { pager(view)!!.config[SemanticsActions.ScrollToIndex].action!!.invoke(4) }
            await { indices.last() == 0 && pagePosition(view) < 1.5f }

            // Observe more than a complete cycle; reaching the sentinel must not stop the timer.
            for (expected in listOf(1, 2, 0, 1)) {
                await(timeout = 8000) { indices.last() == expected }
            }
            onMain { assertTrue(indices.all { it in banners.indices }) }
        }
    }

    @Test
    fun zeroWidthDoesNotCreatePagesAndCanRecover() {
        ActivityScenario.launch(ComponentActivity::class.java).use { scenario ->
            lateinit var view: ComposeView
            val indices = mutableListOf<Int>()
            scenario.onActivity { activity ->
                val host = FrameLayout(activity)
                view = ComposeView(activity).apply {
                    setContent { BannerImage(banners, 0, onBannerIndexChange = { indices.add(it) }) }
                }
                host.addView(view, FrameLayout.LayoutParams(0, 240))
                activity.setContentView(host)
            }
            await { view.isLaidOut }
            onMain {
                assertEquals(0, view.width)
                assertTrue(indices.isEmpty())
                assertEquals(null, pager(view))
                view.layoutParams = view.layoutParams.apply { width = 600 }
            }
            await { pager(view) != null && indices.isNotEmpty() }
            onMain { view.layoutParams = view.layoutParams.apply { width = 0 } }
            await { view.width == 0 && pager(view) == null }
            onMain { view.layoutParams = view.layoutParams.apply { width = 600 } }
            await { pager(view) != null }
            await(timeout = 8000) { indices.last() == 1 }
        }
    }

    @Test
    fun emptySingletonAndShrinkingListsStayInBounds() {
        ActivityScenario.launch(ComponentActivity::class.java).use { scenario ->
            lateinit var view: ComposeView
            val list = mutableStateOf(banners)
            val indices = mutableListOf<Int>()
            scenario.onActivity { activity ->
                view = ComposeView(activity).apply {
                    setContent { BannerImage(list.value, 0, onBannerIndexChange = { indices.add(it) }) }
                }
                activity.setContentView(view, ViewGroup.LayoutParams(600, 240))
            }
            await { pager(view) != null }
            onMain { pager(view)!!.config[SemanticsActions.ScrollToIndex].action!!.invoke(3) }
            await { indices.lastOrNull() == 2 }
            onMain { list.value = persistentListOf(LocalBanner(id = 4)) }
            await {
                pager(view)?.config?.getOrNull(SemanticsProperties.CollectionInfo)?.columnCount == 1 &&
                    indices.lastOrNull() == 0
            }
            onMain { list.value = persistentListOf() }
            await { pager(view) == null }
            onMain { list.value = banners }
            await { pager(view) != null && indices.lastOrNull() == 0 }
        }
    }

    private fun pagePosition(view: ComposeView): Float =
        pager(view)!!.config[SemanticsProperties.HorizontalScrollAxisRange].value() / view.width

    private fun pager(view: View): SemanticsNode? {
        if (view is ViewRootForTest) return findPager(view.semanticsOwner.rootSemanticsNode)
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) pager(view.getChildAt(i))?.let { return it }
        }
        return null
    }

    private fun findPager(node: SemanticsNode): SemanticsNode? {
        if (node.config.contains(SemanticsProperties.CollectionInfo)) return node
        for (child in node.children) findPager(child)?.let { return it }
        return null
    }

    private fun onMain(block: () -> Unit) = instrumentation.runOnMainSync(block)

    private fun await(timeout: Long = 5000, condition: () -> Boolean) {
        val deadline = SystemClock.uptimeMillis() + timeout
        do {
            var satisfied = false
            onMain { satisfied = condition() }
            if (satisfied) return
            SystemClock.sleep(20)
        } while (SystemClock.uptimeMillis() < deadline)
        throw AssertionError("Banner condition did not become true within $timeout ms")
    }
}
