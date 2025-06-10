package `in`.koreatech.koin.feature.store

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.owner.MenuCategory
import `in`.koreatech.koin.feature.store.TestDevice.FLIP_DEVICE
import `in`.koreatech.koin.feature.store.TestDevice.FOLDABLE_DEVICE
import `in`.koreatech.koin.feature.store.view.StoreDetailScreen
import org.junit.Rule
import org.junit.Test

class StoreDetailSnapshotTestPixel6Pro {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6_PRO.copy(softButtons = false)
    )

    @Test
    fun snapshot_store_detailPIXEL_6_PRO() {
        paparazzi.snapshot(name = "store_detail_PIXEL_6_PRO") {
            KoinTheme {
                SnapshotContent()
            }
        }
    }
}

class StoreDetailSnapshotTestPixel4 {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_4.copy(softButtons = false)
    )

    @Test
    fun snapshot_store_detail_Pixel4() {
        paparazzi.snapshot(name = "store_detail_PIXEL_4") {
            KoinTheme {
                SnapshotContent()
            }
        }
    }
}

class StoreDetailSnapshotTestPixel2XL {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_2_XL.copy(softButtons = false)
    )

    @Test
    fun snapshot_store_detail_Pixel2_XL() {
        paparazzi.snapshot(name = "store_detail_PIXEL2_XL") {
            KoinTheme {
                SnapshotContent()
            }
        }
    }
}

class StoreDetailSnapshotTestFLIP {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = FLIP_DEVICE.copy(softButtons = false)
    )

    @Test
    fun snapshot_store_detail_FFLIP() {
        paparazzi.snapshot(name = "store_detail_FLIP") {
            KoinTheme {
                SnapshotContent()
            }
        }
    }
}

class StoreDetailSnapshotTestFOLDABLE {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = FOLDABLE_DEVICE.copy(softButtons = false)
    )

    @Test
    fun snapshot_store_detail_FOLDABLE() {
        paparazzi.snapshot(name = "store_detail_FOLDABLE") {
            KoinTheme {
                SnapshotContent()
            }
        }
    }
}

@Composable
private fun SnapshotContent() {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    StoreDetailScreen(
        storeInfo = dummyStoreWithMenu,
        categories = listOf(MenuCategory("카테고리1", false), MenuCategory("카테고리2", false)),
        menus = listOf(dummyStoreMenuCategories),
        pagerState = pagerState
    )
}
