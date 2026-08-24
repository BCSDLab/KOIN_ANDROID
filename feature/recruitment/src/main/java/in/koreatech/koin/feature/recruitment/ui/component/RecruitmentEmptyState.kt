package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R

@Composable
fun RecruitmentEmptyState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_recruitment_empty),
            contentDescription = null,
            modifier = Modifier.size(width = 98.dp, height = 75.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = RebrandKoinTheme.typography.medium16,
            color = RebrandKoinTheme.colors.neutral700,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = subtitle,
            style = RebrandKoinTheme.typography.regular14,
            color = RebrandKoinTheme.colors.neutral500,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun RecruitmentEmptyStatePreview() {
    RebrandKoinTheme {
        RecruitmentEmptyState(
            title = "작성한 모집글이 없어요.",
            subtitle = "직접 모집글을 작성하여 팀원을 모집해보세요."
        )
    }
}
