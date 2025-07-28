package `in`.koreatech.koin.feature.club.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize

@Composable
fun KoinClubInputGrid(
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.Top
) {
    SubcomposeLayout(modifier) { constraints ->
        val leftMainPlaceables =
            subcompose(SlotsEnum.LeftMain, leftContent).map {
                it.measure(Constraints())
            }

        val rightMainPlaceables =
            subcompose(SlotsEnum.RightMain, rightContent).map {
                it.measure(Constraints())
            }

        require(leftMainPlaceables.size == rightMainPlaceables.size) { "leftContent and rightContent should have same amount of content" }

        val leftMaxSize = leftMainPlaceables.fold(IntSize.Zero) { currentMax, placeable ->
            IntSize(
                width = maxOf(currentMax.width, placeable.width),
                height = maxOf(currentMax.height, placeable.height)
            )
        }

        val rightMaxSize = rightMainPlaceables.fold(IntSize.Zero) { currentMax, placeable ->
            IntSize(
                width = maxOf(currentMax.width, placeable.width),
                height = maxOf(currentMax.height, placeable.height)
            )
        }

        val leftResizedPlaceables: List<Placeable> =
            subcompose(SlotsEnum.LeftDependent, leftContent).map {
                it.measure(
                    Constraints(
                        minWidth = leftMaxSize.width
                    )
                )
            }

        val rightResizedPlaceables: List<Placeable> =
            subcompose(SlotsEnum.RightDependent, rightContent).map {
                it.measure(
                    Constraints(
                        minWidth = minOf(
                            rightMaxSize.width,
                            constraints.maxWidth - leftMaxSize.width
                        ),
                        maxWidth = constraints.maxWidth - leftMaxSize.width
                    )
                )
            }

        val contentMaxHeight = leftMainPlaceables.zip(rightResizedPlaceables).sumOf { maxOf(it.first.height, it.second.height) }

        layout(constraints.maxWidth, constraints.minHeight + contentMaxHeight) {
            leftResizedPlaceables.zip(rightResizedPlaceables).forEachIndexed { index, placeable ->
                val heightStart = maxOf(
                    leftResizedPlaceables.take(index).sumOf { it.measuredHeight },
                    rightResizedPlaceables.take(index).sumOf { it.measuredHeight }
                )

                val leftHeightStart = if (placeable.first.height > placeable.second.height) {
                    heightStart
                } else {
                    heightStart + when (verticalAlignment) {
                        Alignment.Top -> 0
                        Alignment.CenterVertically -> (placeable.second.height - placeable.first.height) / 2
                        Alignment.Bottom -> placeable.second.height - placeable.first.height
                        else -> 0
                    }
                }

                val rightHeightStart = if (placeable.second.height > placeable.first.height) {
                    heightStart
                } else {
                    heightStart + when (verticalAlignment) {
                        Alignment.Top -> 0
                        Alignment.CenterVertically -> (placeable.first.height - placeable.second.height) / 2
                        Alignment.Bottom -> placeable.first.height - placeable.second.height
                        else -> 0
                    }
                }

                placeable.first.place(0, leftHeightStart)
                placeable.second.place(
                    x = constraints.maxWidth - minOf(
                        rightMaxSize.width,
                        constraints.maxWidth - leftMaxSize.width
                    ),
                    y = rightHeightStart
                )
            }
        }
    }
}

enum class SlotsEnum {
    LeftMain,
    RightMain,
    LeftDependent,
    RightDependent
}
