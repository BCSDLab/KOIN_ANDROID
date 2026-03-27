package `in`.koreatech.koin.feature.callvan

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun CallvanEntry(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.callvan_entry_title),
            style = KoinTheme.typography.bold18.copy(fontSize = 15.sp),
            color = KoinTheme.colors.primary500
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            CallvanEntryCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.callvan_entry_find_title),
                description = stringResource(R.string.callvan_entry_find_description),
                onClick = {
                    EventLogger.logCampusClickEvent(
                        AnalyticsConstant.Label.Callvan.MAIN_CALLVAN_VIEW,
                        ""
                    )
                    context.startActivity(Intent(context, CallvanActivity::class.java))
                }
            )

            Spacer(modifier = Modifier.width(16.dp))

            CallvanEntryCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.callvan_entry_recruit_title),
                description = stringResource(R.string.callvan_entry_recruit_description),
                onClick = {
                    EventLogger.logCampusClickEvent(
                        AnalyticsConstant.Label.Callvan.MAIN_CALLVAN_WRITE,
                        ""
                    )
                    context.startActivity(
                        Intent(context, CallvanActivity::class.java).apply {
                            putExtra(CallvanActivity.EXTRA_NAVIGATE_TO_CREATE, true)
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun CallvanEntryCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    Row(
        modifier = modifier
            .clip(KoinTheme.shapes.small)
            .clickable(onClick = onClick)
            .background(color = KoinTheme.colors.neutral50)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = KoinTheme.typography.medium14
            )
            Text(
                text = description,
                style = KoinTheme.typography.regular12
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = title
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanEntryPreview() {
    KoinTheme {
        CallvanEntry()
    }
}
