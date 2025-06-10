package `in`.koreatech.koin.feature.club.ui.clubdetail.component.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.feature.club.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailImageDialog(
    imageModel: ImageRequest,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    BasicAlertDialog(
        modifier =
        modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onDismissRequest = { onDismiss() }
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = imageModel,
            contentDescription = "Club Image",
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.Center,
            loading = {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            error = {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.detail_club_image_error))
                }
            }
        )
    }
}
