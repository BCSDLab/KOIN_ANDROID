package `in`.koreatech.koin.feature.department.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.text.LeadingIconText
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.department.R
import `in`.koreatech.koin.feature.department.util.openUrl

@Composable
internal fun DepartmentFooter(
    updatedAt: String,
    modifier: Modifier = Modifier,
    loggingEventValue: String = ""
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (updatedAt.isNotEmpty()) {
            Text(
                text = stringResource(R.string.department_updated_at, updatedAt),
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral500
            )
        }

        LeadingIconText(
            modifier = Modifier
                .noRippleClickable {
                    EventLogger.logCampusClickEvent(
                        "department_error_feedback_button",
                        loggingEventValue
                    )
                    context.openUrl(DEPARTMENT_FEEDBACK_FORM_URL)
                }
                .padding(top = 2.dp),
            text = stringResource(R.string.department_wrong_information),
            iconRes = R.drawable.ic_department_caution,
            iconTint = RebrandKoinTheme.colors.neutral500,
            textStyle = RebrandKoinTheme.typography.regular12.copy(
                color = RebrandKoinTheme.colors.neutral500
            )
        )
    }
}

private const val DEPARTMENT_FEEDBACK_FORM_URL =
    "https://docs.google.com/forms/d/1GR4t8IfTOrYY4jxq5YAS7YiCS8QIFtHaWu_kE-SdDKY"

@Preview(showBackground = true)
@Composable
private fun DepartmentFooterPreview() {
    RebrandKoinTheme {
        DepartmentFooter(
            modifier = Modifier.padding(16.dp),
            updatedAt = "2026.06.24"
        )
    }
}
