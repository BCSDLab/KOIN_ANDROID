package `in`.koreatech.koin.feature.recruitment.ui.recruitmentapply.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun RecruitmentStepIndicator(
    currentStep: Int,
    totalSteps: Int,
    stepLabels: ImmutableList<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        for (step in 1..totalSteps) {
            val isCurrent = step == currentStep
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = if (isCurrent) RebrandKoinTheme.colors.primary400 else RebrandKoinTheme.colors.neutral300,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = step.toString(),
                        style = RebrandKoinTheme.typography.medium13,
                        color = if (isCurrent) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.neutral700,
                        textAlign = TextAlign.Center
                    )
                }
                if (step <= stepLabels.size) {
                    Text(
                        text = stepLabels[step - 1],
                        style = RebrandKoinTheme.typography.regular12.copy(
                            fontSize = 10.sp,
                            lineHeight = 16.sp
                        ),
                        color = if (isCurrent) RebrandKoinTheme.colors.primary400 else RebrandKoinTheme.colors.neutral400,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            if (step != totalSteps) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 15.dp)
                        .size(width = 60.dp, height = 2.dp)
                        .background(RebrandKoinTheme.colors.neutral300)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentStepIndicatorPreview() {
    RebrandKoinTheme {
        RecruitmentStepIndicator(
            currentStep = 1,
            totalSteps = 2,
            stepLabels = persistentListOf("기본 정보", "지원서 작성")
        )
    }
}
