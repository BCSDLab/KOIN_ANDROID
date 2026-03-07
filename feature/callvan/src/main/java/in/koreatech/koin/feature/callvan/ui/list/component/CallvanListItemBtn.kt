package `in`.koreatech.koin.feature.callvan.ui.list.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R

@Composable
fun CallvanRouteListItemButtons(
    state: CallvanRouteState,
    modifier: Modifier = Modifier,
    onJoin: () -> Unit = {},
    onCancelJoin: () -> Unit = {},
    onClose: () -> Unit = {},
    onReRecruit: () -> Unit = {},
    onComplete: () -> Unit = {},
    onCall: () -> Unit = {},
    onChat: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (state == CallvanRouteState.JOINED) {
            IconButton(onClick = onChat, modifier = Modifier.size(24.dp)) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_callvan_chat),
                    contentDescription = "",
                    tint = RebrandKoinTheme.colors.primary500
                )
            }
        }
        if (state == CallvanRouteState.OWNER_ACTIVE || state == CallvanRouteState.OWNER_CLOSED) {
            IconButton(onClick = onCall, modifier = Modifier.size(24.dp)) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_callvan_call),
                    contentDescription = "",
                    tint = RebrandKoinTheme.colors.primary500
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (state) {
                CallvanRouteState.DEFAULT -> {
                    Button(
                        modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                        onClick = onJoin,
                        shape = KoinTheme.shapes.extraSmall,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RebrandKoinTheme.colors.primary500
                        )
                    ) {
                        Text(
                            style = KoinTheme.typography.regular12,
                            text = "참여하기"
                        )
                    }
                }

                CallvanRouteState.JOINED -> {
                    OutlinedButton(
                        modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                        onClick = onCancelJoin,
                        shape = KoinTheme.shapes.extraSmall,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = RebrandKoinTheme.colors.primary500
                        ),
                        border = BorderStroke(0.5.dp, RebrandKoinTheme.colors.primary500)
                    ) {
                        Text(
                            style = KoinTheme.typography.regular12,
                            text = "참여취소"
                        )
                    }
                }

                CallvanRouteState.CLOSED -> {
                    OutlinedButton(
                        modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                        onClick = {},
                        enabled = false,
                        shape = KoinTheme.shapes.extraSmall,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = KoinTheme.colors.neutral400,
                            disabledContentColor = KoinTheme.colors.neutral400
                        ),
                        border = BorderStroke(1.dp, KoinTheme.colors.neutral300)
                    ) {
                        Text(
                            style = KoinTheme.typography.regular12,
                            text = "모집마감"
                        )
                    }
                }

                CallvanRouteState.OWNER_ACTIVE -> {
                    OutlinedButton(
                        modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                        onClick = onClose,
                        shape = KoinTheme.shapes.extraSmall,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = RebrandKoinTheme.colors.primary500
                        ),
                        border = BorderStroke(0.5.dp, RebrandKoinTheme.colors.primary500)
                    ) {
                        Text(
                            style = KoinTheme.typography.regular12,
                            text = "마감하기"
                        )
                    }
                }

                CallvanRouteState.OWNER_CLOSED -> {
                    OutlinedButton(
                        modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                        onClick = onReRecruit,
                        shape = KoinTheme.shapes.extraSmall,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = RebrandKoinTheme.colors.primary500
                        ),
                        border = BorderStroke(0.5.dp, RebrandKoinTheme.colors.primary500)
                    ) {
                        Text(
                            style = KoinTheme.typography.regular12,
                            text = "재모집"
                        )
                    }
                    Button(
                        modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                        onClick = onComplete,
                        shape = KoinTheme.shapes.extraSmall,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RebrandKoinTheme.colors.primary500
                        )
                    ) {
                        Text(
                            style = KoinTheme.typography.regular12,
                            text = "이용완료"
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanRouteListItemButtonsDefaultPreview() {
    RebrandKoinTheme {
        CallvanRouteListItemButtons(state = CallvanRouteState.DEFAULT)
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanRouteListItemButtonsJoinedPreview() {
    RebrandKoinTheme {
        CallvanRouteListItemButtons(state = CallvanRouteState.JOINED)
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanRouteListItemButtonsClosedPreview() {
    RebrandKoinTheme {
        CallvanRouteListItemButtons(state = CallvanRouteState.CLOSED)
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanRouteListItemButtonsOwnerActivePreview() {
    RebrandKoinTheme {
        CallvanRouteListItemButtons(state = CallvanRouteState.OWNER_ACTIVE)
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanRouteListItemButtonsOwnerClosedPreview() {
    RebrandKoinTheme {
        CallvanRouteListItemButtons(state = CallvanRouteState.OWNER_CLOSED)
    }
}
