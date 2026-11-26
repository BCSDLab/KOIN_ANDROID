package `in`.koreatech.koin.feature.land.ui.main

import androidx.compose.ui.geometry.Rect
import org.junit.Assert.assertEquals
import org.junit.Test

class LandMapBottomPaddingTest {
    @Test
    fun `sheet extending below map only obscures the intersecting height`() {
        val map = Rect(0f, 100f, 500f, 1000f)
        val sheet = Rect(0f, 600f, 500f, 1200f)

        assertEquals(400, calculateLandMapBottomPadding(map, sheet))
    }

    @Test
    fun `expanded sheet cannot obscure more than the map height`() {
        val map = Rect(0f, 100f, 500f, 1000f)
        val sheet = Rect(0f, 0f, 500f, 1200f)

        assertEquals(900, calculateLandMapBottomPadding(map, sheet))
    }

    @Test
    fun `nonoverlapping sheets do not move map controls`() {
        val map = Rect(0f, 100f, 500f, 1000f)

        assertEquals(0, calculateLandMapBottomPadding(map, Rect(0f, 1000f, 500f, 1200f)))
        assertEquals(0, calculateLandMapBottomPadding(map, Rect(500f, 600f, 1000f, 1200f)))
    }
}
