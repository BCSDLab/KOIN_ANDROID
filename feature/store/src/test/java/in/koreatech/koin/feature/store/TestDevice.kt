package `in`.koreatech.koin.feature.store


import app.cash.paparazzi.DeviceConfig

object TestDevice {
    val FLIP_DEVICE = DeviceConfig(
        screenWidth = 1080,
        screenHeight = 2640,
        xdpi = 426,
        ydpi = 426,
        fontScale = 1.0f,
        softButtons = false,
    )

    val FOLDABLE_DEVICE = DeviceConfig(
        screenWidth = 1800,
        screenHeight = 2200,
        xdpi = 480,
        ydpi = 480,
        fontScale = 1.0f,
        softButtons = false,
    )
}