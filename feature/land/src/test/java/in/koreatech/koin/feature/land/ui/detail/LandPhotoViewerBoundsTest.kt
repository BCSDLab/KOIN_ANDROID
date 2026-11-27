package `in`.koreatech.koin.feature.land.ui.detail

import org.junit.Assert.assertEquals
import org.junit.Test

class LandPhotoViewerBoundsTest {
    @Test
    fun `fit size image cannot be dragged off screen`() {
        assertEquals(0f, clampLandPhotoOffset(500f, 400f, 400f, 1f), 0f)
        assertEquals(0f, clampLandPhotoOffset(-500f, 200f, 400f, 1f), 0f)
    }

    @Test
    fun `zoomed image stays within its visible edges`() {
        assertEquals(200f, clampLandPhotoOffset(1000f, 400f, 400f, 2f), 0f)
        assertEquals(-200f, clampLandPhotoOffset(-1000f, 400f, 400f, 2f), 0f)
        assertEquals(75f, clampLandPhotoOffset(75f, 400f, 400f, 2f), 0f)
    }

    @Test
    fun `zooming out recenters a previously panned photo`() {
        val panned = clampLandPhotoOffset(300f, 400f, 400f, 3f)
        assertEquals(0f, clampLandPhotoOffset(panned, 400f, 400f, 1f), 0f)
    }

    @Test
    fun `letterboxed axis remains centered until image fills viewport`() {
        assertEquals(0f, clampLandPhotoOffset(100f, 100f, 400f, 3f), 0f)
        assertEquals(50f, clampLandPhotoOffset(100f, 100f, 400f, 5f), 0f)
    }
}
