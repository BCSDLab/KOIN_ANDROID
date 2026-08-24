package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R

@Composable
fun ApplicantAvatar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(45.dp)
            .clip(CircleShape)
            .border(BorderStroke(0.5.dp, RebrandKoinTheme.colors.neutral300), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_recruitment_user),
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ApplicantAvatarPreview() {
    RebrandKoinTheme {
        ApplicantAvatar()
    }
}
