package `in`.koreatech.koin.core.camera.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.compose.LocalLifecycleOwner
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import java.io.File
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch

object KoinCameraDefaults {
    val windowInsets: WindowInsets
        @Composable
        get() = WindowInsets.systemBars.only(
            WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
        )

    @Composable
    fun koinCameraStyle(
        shutterColor: Color = RebrandKoinTheme.colors.primary100,
        shutterShape: Shape = CircleShape,
        shutterSize: Dp = 72.dp
    ): KoinCameraStyle = KoinCameraStyle(
        shutterColor = shutterColor,
        shutterShape = shutterShape,
        shutterSize = shutterSize
    )
}

@Immutable
data class KoinCameraStyle(
    val shutterColor: Color,
    val shutterShape: Shape,
    val shutterSize: Dp
)

@Composable
fun rememberKoinCameraState() = remember { KoinCameraState() }

@Composable
fun KoinCamera(
    modifier: Modifier = Modifier,
    cameraState: KoinCameraState = rememberKoinCameraState(),
    style: KoinCameraStyle = KoinCameraDefaults.koinCameraStyle(),
    windowInsets: WindowInsets = KoinCameraDefaults.windowInsets,
    onError: (Exception) -> Unit = {},
    onPermissionRequired: () -> Unit = {},
    onCapture: (Uri?, Int) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    val isPermissionGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    LaunchedEffect(isPermissionGranted) {
        if (!isPermissionGranted) {
            onPermissionRequired()
            return@LaunchedEffect
        }
    }

    val takePicture: suspend () -> Unit = {
        val photoFile = File(context.cacheDir, "koin_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        cameraState.imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    onCapture(photoFile.toUri(), outputFileResults.imageFormat)
                }

                override fun onError(exception: ImageCaptureException) {
                    onError(exception)
                }
            }
        )
    }

    LaunchedEffect(cameraState.cameraSelector, cameraState.preview, cameraState.imageCapture) {
        val cameraProvider = ProcessCameraProvider.awaitInstance(context)
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraState.cameraSelector, cameraState.preview, cameraState.imageCapture)
        try {
            awaitCancellation()
        } finally {
            cameraProvider.unbindAll()
        }
    }

    Box(
        modifier = modifier
            .windowInsetsPadding(windowInsets)
            .fillMaxSize()
    ) {
        cameraState.surfaceRequest?.let { surfaceRequest ->
            CameraXViewfinder(
                surfaceRequest = surfaceRequest,
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomCenter)
                .size(style.shutterSize)
                .background(color = style.shutterColor, shape = style.shutterShape)
                .clip(style.shutterShape)
                .clickable {
                    coroutineScope.launch {
                        takePicture()
                    }
                }
        )
    }
}
