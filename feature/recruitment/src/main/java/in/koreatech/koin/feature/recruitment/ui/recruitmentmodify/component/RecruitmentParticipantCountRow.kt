package `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun RecruitmentParticipantCountRow(
    count: Int,
    onCountChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    RecruitmentCountStepper(
        count = count,
        onCountChange = onCountChange,
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentParticipantCountRowPreview() {
    RebrandKoinTheme {
        RecruitmentParticipantCountRow(count = 5, onCountChange = {})
    }
}
