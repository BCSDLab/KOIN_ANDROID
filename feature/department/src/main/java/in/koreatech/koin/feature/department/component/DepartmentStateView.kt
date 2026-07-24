package `in`.koreatech.koin.feature.department.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.department.R

@Composable
internal fun DepartmentEmptyView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.width(130.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_department_search_empty),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.department_search_empty_description),
            style = RebrandKoinTheme.typography.regular14,
            color = RebrandKoinTheme.colors.neutral500,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
internal fun DepartmentFailureView(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.department_load_failure_description),
            style = RebrandKoinTheme.typography.bold15,
            color = RebrandKoinTheme.colors.neutral800,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onRefresh,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = RebrandKoinTheme.colors.neutral800
            ),
            border = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral300),
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = stringResource(R.string.department_refresh)
            )
            Text(
                modifier = Modifier.padding(start = 6.dp),
                text = stringResource(R.string.department_refresh),
                style = RebrandKoinTheme.typography.regular14
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DepartmentEmptyViewPreview() {
    RebrandKoinTheme {
        DepartmentEmptyView(modifier = Modifier.padding(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun DepartmentFailureViewPreview() {
    RebrandKoinTheme {
        DepartmentFailureView(modifier = Modifier.padding(32.dp))
    }
}
