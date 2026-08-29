package `in`.koreatech.koin.feature.recruitment.ui.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentOutlinedActionButton

private val CardShape = RoundedCornerShape(16.dp)

@Composable
fun ProfileEmptyState(
    onCreateProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(RebrandKoinTheme.colors.neutral0, CardShape)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_recruitment_empty),
                contentDescription = null,
                modifier = Modifier.size(width = 85.dp, height = 66.dp)
            )
            Text(
                text = stringResource(R.string.recruitment_profile_empty_title),
                style = RebrandKoinTheme.typography.medium16,
                color = RebrandKoinTheme.colors.neutral700,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.recruitment_profile_empty_subtitle),
                style = RebrandKoinTheme.typography.regular14,
                color = RebrandKoinTheme.colors.neutral500,
                textAlign = TextAlign.Center
            )
        }
        RecruitmentOutlinedActionButton(
            text = stringResource(R.string.recruitment_profile_create),
            onClick = onCreateProfileClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileEmptyStatePreview() {
    RebrandKoinTheme {
        ProfileEmptyState(onCreateProfileClick = {})
    }
}
