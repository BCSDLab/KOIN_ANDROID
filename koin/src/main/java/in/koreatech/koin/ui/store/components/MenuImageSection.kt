package `in`.koreatech.koin.ui.store.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.R

@Composable
fun MenuImageSection(
    @DrawableRes imageResInt: Int
) {
    Image(
        painter = painterResource(id = imageResInt),
        contentDescription = "메뉴 이미지",
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        contentScale = ContentScale.Fit
    )
}

@Preview
@Composable
fun MenuImageSectionPreview() {
    MenuImageSection(imageResInt = R.drawable.no_image)
}
