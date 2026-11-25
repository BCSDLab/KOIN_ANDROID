package `in`.koreatech.koin.feature.chat.ui.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.chat.R

@Composable
fun ChatUserIcon(modifier: Modifier = Modifier, imageUri: Uri? = null) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(RebrandKoinTheme.shapes.small)
            .border(1.dp, RebrandKoinTheme.colors.primary600, RebrandKoinTheme.shapes.small),
        contentAlignment = Alignment.Center
    ) {
        if (imageUri == null || imageUri == Uri.EMPTY) {
            ChatProfilePlaceholder()
        } else {
            SubcomposeAsyncImage(
                model = imageUri,
                contentDescription = stringResource(R.string.chat_partner_profile_image),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = { ChatProfilePlaceholder() },
                error = { ChatProfilePlaceholder() }
            )
        }
    }
}

@Composable
private fun ChatProfilePlaceholder() {
    Image(
        painter = painterResource(R.drawable.ic_chat_profile),
        contentDescription = stringResource(R.string.chat_partner_profile_image),
        modifier = Modifier.size(24.dp)
    )
}
