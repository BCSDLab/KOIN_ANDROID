package `in`.koreatech.koin.feature.callvan.ui.report.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember

@Composable
internal fun rememberImagePickerLauncher(
    currentImageCount: Int,
    maxImageCount: Int,
    onImagesSelected: (List<Uri>) -> Unit
): () -> Unit {
    val remaining = remember(maxImageCount, currentImageCount) { maxImageCount - currentImageCount }

    val singleImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { onImagesSelected(listOf(it)) }
    }

    val multipleImagePickerLauncher = key(remaining) {
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = remaining)
        ) { uris ->
            onImagesSelected(uris)
        }
    }

    return {
        if (remaining == 1) {
            singleImagePickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else if (remaining > 1) {
            multipleImagePickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }
}
