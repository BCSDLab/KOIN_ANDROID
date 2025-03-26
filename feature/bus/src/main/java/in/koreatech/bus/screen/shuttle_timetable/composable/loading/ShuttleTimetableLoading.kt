package `in`.koreatech.bus.screen.shuttle_timetable.composable.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.animation.skeleton

@Composable
internal fun ShuttleTimetableLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
        ) {
            Spacer(
                modifier =
                Modifier
                    .width(30.dp)
                    .height(16.dp)
                    .skeleton()
            )

            Spacer(
                modifier =
                Modifier
                    .padding(top = 6.dp)
                    .width(120.dp)
                    .height(24.dp)
                    .skeleton()
            )
        }

        Spacer(
            modifier =
            Modifier
                .fillMaxWidth()
                .height(24.dp)
        )

        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier.padding(end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                repeat(25) {
                    Spacer(
                        modifier =
                        Modifier
                            .width(90.dp)
                            .height(30.dp)
                            .skeleton()
                    )
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                repeat(25) {
                    Spacer(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .skeleton()
                    )
                }
            }
        }

        Spacer(
            modifier =
            Modifier.fillMaxWidth()
                .height(120.dp)
        )
    }
}
