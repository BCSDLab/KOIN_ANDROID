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
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanItemState
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListItemActions

@Composable
fun CallvanListItemButtons(
    state: CallvanItemState,
    actions: CallvanListItemActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(26.dp)
    ) {
        when (state) {
            CallvanItemState.JOINED -> {
                IconButton(onClick = actions.onChat, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_callvan_chat),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                }
            }
            CallvanItemState.OWNER_ACTIVE,
            CallvanItemState.OWNER_CLOSED -> {
                IconButton(onClick = actions.onCall, modifier = Modifier.size(24.dp)) {
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
                CallvanItemState.JOINED -> CallvanOutlinedButton(
                    text = stringResource(R.string.callvan_btn_cancel_join),
                    onClick = actions.onCancelJoin
                )
                CallvanItemState.CLOSED -> CallvanOutlinedButton(
                    text = stringResource(R.string.callvan_btn_closed),
                    onClick = {},
                    enabled = false,
                    contentColor = KoinTheme.colors.neutral400,
                    borderColor = KoinTheme.colors.neutral300,
                    borderWidth = 1.dp
                )
                CallvanItemState.OWNER_ACTIVE -> CallvanOutlinedButton(
                    text = stringResource(R.string.callvan_btn_close),
                    onClick = actions.onClose
                )
                CallvanItemState.OWNER_CLOSED -> CallvanOutlinedButton(
                    text = stringResource(R.string.callvan_btn_re_recruit),
                    onClick = actions.onReRecruit,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                )
                else -> Unit
            }

            when (state) {
                CallvanItemState.DEFAULT -> CallvanFilledButton(
                    text = stringResource(R.string.callvan_btn_join),
                    onClick = actions.onJoin,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                )
                CallvanItemState.OWNER_CLOSED -> CallvanFilledButton(
                    text = stringResource(R.string.callvan_btn_complete),
                    onClick = actions.onComplete,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                )
                else -> Unit
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
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
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
}

@Composable
private fun CallvanOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
    contentColor: Color = RebrandKoinTheme.colors.primary500,
    disabledContentColor: Color = RebrandKoinTheme.colors.neutral500,
    borderColor: Color = RebrandKoinTheme.colors.primary500,
    borderWidth: Dp = 0.5.dp
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
        OutlinedButton(
            modifier = modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
            onClick = onClick,
            enabled = enabled,
            shape = KoinTheme.shapes.extraSmall,
            contentPadding = contentPadding,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = contentColor,
                disabledContentColor = disabledContentColor
            ),
            border = BorderStroke(
                borderWidth,
                if (enabled) borderColor else disabledContentColor
            )
        ) {
            Text(style = KoinTheme.typography.regular12, text = text)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemButtonsDefaultPreview() {
    RebrandKoinTheme {
        CallvanListItemButtons(
            state = CallvanItemState.DEFAULT,
            actions = CallvanListItemActions()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemButtonsJoinedPreview() {
    RebrandKoinTheme {
        CallvanListItemButtons(
            state = CallvanItemState.JOINED,
            actions = CallvanListItemActions()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemButtonsClosedPreview() {
    RebrandKoinTheme {
        CallvanListItemButtons(
            state = CallvanItemState.CLOSED,
            actions = CallvanListItemActions()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemButtonsOwnerActivePreview() {
    RebrandKoinTheme {
        CallvanListItemButtons(
            state = CallvanItemState.OWNER_ACTIVE,
            actions = CallvanListItemActions()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemButtonsOwnerClosedPreview() {
    RebrandKoinTheme {
        CallvanListItemButtons(
            state = CallvanItemState.OWNER_CLOSED,
            actions = CallvanListItemActions()
        )
    }
}
