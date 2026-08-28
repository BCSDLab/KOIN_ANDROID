package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentEmptyState

@Composable
fun MyRecruitmentEmptyState(modifier: Modifier = Modifier) {
    RecruitmentEmptyState(
        title = stringResource(R.string.recruitment_empty_title),
        subtitle = stringResource(R.string.recruitment_empty_subtitle),
        modifier = modifier
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun MyRecruitmentEmptyStatePreview() {
    RebrandKoinTheme {
        MyRecruitmentEmptyState()
    }
}
