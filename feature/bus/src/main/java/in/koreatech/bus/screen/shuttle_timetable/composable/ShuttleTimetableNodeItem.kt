package `in`.koreatech.bus.screen.shuttle_timetable.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import `in`.koreatech.bus.mock.shuttleTimetableNodeInfoMock1
import `in`.koreatech.bus.mock.shuttleTimetableNodeInfoMock2
import `in`.koreatech.bus.mock.shuttleTimetableNodeInfoMock3
import `in`.koreatech.bus.mock.shuttleTimetableNodeInfoMock4
import `in`.koreatech.bus.mock.shuttleTimetableNodeInfoMock5
import `in`.koreatech.bus.state.ShuttleTimetableNodeInfoState
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun ShuttleTimetableNodeItem(
    nodes: ImmutableList<ShuttleTimetableNodeInfoState>,
    nodeItemHeightDp: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(IntrinsicSize.Max)
    ) {
        Text(
            text = stringResource(R.string.node_name),
            style = KoinTheme.typography.regular14,
            color = KoinTheme.colors.neutral600,
            modifier =
            Modifier
                .fillMaxWidth()
                .background(color = KoinTheme.colors.neutral100)
                .padding(horizontal = 24.dp, vertical = 8.dp)
        )

        nodes.fastForEach { node ->
            NodeItem(
                nodeTitle = node.name,
                nodeDescription = node.detail,
                modifier =
                Modifier
                    .padding(horizontal = 24.dp, vertical = 4.dp)
                    .height(nodeItemHeightDp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ShuttleTimetableNodeItemPreview() {
    ShuttleTimetableNodeItem(
        persistentListOf(
            shuttleTimetableNodeInfoMock1,
            shuttleTimetableNodeInfoMock2,
            shuttleTimetableNodeInfoMock3,
            shuttleTimetableNodeInfoMock4,
            shuttleTimetableNodeInfoMock5
        ),
        nodeItemHeightDp = 40.dp
    )
}
