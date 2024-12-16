package `in`.koreatech.bus.screen.searchresult.composable.loading

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.animation.skeleton

@Composable
internal fun BusSearchResultLoadingItem(
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Spacer(
                modifier = Modifier.width(30.dp)
                    .height(16.dp)
                    .skeleton()
            )
            Spacer(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .width(100.dp)
                    .height(20.dp)
                    .skeleton(),
            )
        }
        Spacer(
            modifier = Modifier
                .width(80.dp)
                .height(30.dp)
                .skeleton()
        )
    }
}