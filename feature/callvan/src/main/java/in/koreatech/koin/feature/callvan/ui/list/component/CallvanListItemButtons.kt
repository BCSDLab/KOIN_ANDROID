package `in`.koreatech.koin.feature.callvan.ui.list.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.enums.CallvanRouteState
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListItemClickListener

@Composable
fun CallvanListItemButtons(
    state: CallvanRouteState,
    modifier: Modifier = Modifier,
    clickListener: CallvanListItemClickListener = object : CallvanListItemClickListener {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (state) {
            CallvanRouteState.JOINED -> {
                IconButton(onClick = { clickListener.onChat() }, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_callvan_chat),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                }
            }
            CallvanRouteState.OWNER_ACTIVE,
            CallvanRouteState.OWNER_CLOSED -> {
                IconButton(onClick = { clickListener.onCall() }, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_callvan_call),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                }
            }
            else -> Spacer(modifier = Modifier.size(24.dp))
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (state) {
                CallvanRouteState.JOINED -> CallvanOutlinedButton(
                    text = stringResource(R.string.callvan_btn_cancel_join),
                    onClick = { clickListener.onCancelJoin() }
                )
                CallvanRouteState.CLOSED -> CallvanOutlinedButton(
                    text = stringResource(R.string.callvan_btn_closed),
                    onClick = {},
                    enabled = false,
                    contentColor = KoinTheme.colors.neutral400,
                    borderColor = KoinTheme.colors.neutral300,
                    borderWidth = 1.dp
                )
                CallvanRouteState.OWNER_ACTIVE -> CallvanOutlinedButton(
                    text = stringResource(R.string.callvan_btn_close),
                    onClick = { clickListener.onClose() }
                )
                CallvanRouteState.OWNER_CLOSED -> CallvanOutlinedButton(
                    text = stringResource(R.string.callvan_btn_re_recruit),
                    onClick = { clickListener.onReRecruit() },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                )
                else -> {}
            }

            if (state == CallvanRouteState.DEFAULT || state == CallvanRouteState.OWNER_CLOSED) {
                CallvanFilledButton(
                    text = stringResource(
                        if (state == CallvanRouteState.DEFAULT) {
                            R.string.callvan_btn_join
                        } else {
                            R.string.callvan_btn_complete
                        }
                    ),
                    onClick = {
                        if (state == CallvanRouteState.DEFAULT) {
                            clickListener.onJoin()
                        } else {
                            clickListener.onComplete()
                        }
                    },
                    contentPadding = if (state == CallvanRouteState.OWNER_CLOSED) {
                        PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    } else {
                        PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    }
                )
            }
        }
    }
}

@Composable
private fun CallvanFilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
) {
    Button(
        modifier = modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
        onClick = onClick,
        shape = KoinTheme.shapes.extraSmall,
        contentPadding = contentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = RebrandKoinTheme.colors.primary500
        )
    ) {
        Text(style = KoinTheme.typography.regular12, text = text)
    }
}

@Composable
private fun CallvanOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
    contentColor: Color = RebrandKoinTheme.colors.primary500,
    borderColor: Color = RebrandKoinTheme.colors.primary500,
    borderWidth: Dp = 0.5.dp
) {
    OutlinedButton(
        modifier = modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
        onClick = onClick,
        enabled = enabled,
        shape = KoinTheme.shapes.extraSmall,
        contentPadding = contentPadding,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = contentColor,
            disabledContentColor = contentColor
        ),
        border = BorderStroke(borderWidth, borderColor)
    ) {
        Text(style = KoinTheme.typography.regular12, text = text)
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemButtonsDefaultPreview() {
    RebrandKoinTheme {
        CallvanListItemButtons(state = CallvanRouteState.DEFAULT)
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemButtonsJoinedPreview() {
    RebrandKoinTheme {
        CallvanListItemButtons(state = CallvanRouteState.JOINED)
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemButtonsClosedPreview() {
    RebrandKoinTheme {
        CallvanListItemButtons(state = CallvanRouteState.CLOSED)
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemButtonsOwnerActivePreview() {
    RebrandKoinTheme {
        CallvanListItemButtons(state = CallvanRouteState.OWNER_ACTIVE)
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemButtonsOwnerClosedPreview() {
    RebrandKoinTheme {
        CallvanListItemButtons(state = CallvanRouteState.OWNER_CLOSED)
    }
}
