package `in`.koreatech.koin.core.camera.ui

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class KoinCameraState {
    val fileName = "koin_${System.currentTimeMillis()}.jpg"

    var cameraSelector by mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA)
        private set

    internal val imageCapture = ImageCapture.Builder().build()

    internal val preview = Preview.Builder().build().apply {
        setSurfaceProvider { request -> surfaceRequest = request }
    }

    internal var surfaceRequest: SurfaceRequest? by mutableStateOf(null)
        private set

    fun changeCameraSelector(cameraSelector: CameraSelector) {
        this.cameraSelector = cameraSelector
    }
}
