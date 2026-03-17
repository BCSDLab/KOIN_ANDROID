package `in`.koreatech.koin.feature.callvan.ui.list.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListItemClickListener

@Composable
fun CallvanListItemButtons(
    state: CallvanItemState,
    modifier: Modifier = Modifier,
    clickListener: CallvanListItemClickListener = object : CallvanListItemClickListener {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(26.dp)
    ) {
        when (state) {
            CallvanItemState.JOINED -> {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false)
                        ) { clickListener.onChat() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_callvan_chat),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                }
            }
            CallvanItemState.OWNER_ACTIVE,
            CallvanItemState.OWNER_CLOSED -> {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false)
                        ) { clickListener.onCall() },
                    contentAlignment = Alignment.Center
                ) {
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
                CallvanItemState.JOINED -> CallvanFilledButton(
                    text = stringResource(R.string.callvan_btn_cancel_join),
                    onClick = { clickListener.onCancelJoin() },
                    color = ButtonDefaults.buttonColors(
                        contentColor = RebrandKoinTheme.colors.primary800,
                        containerColor = RebrandKoinTheme.colors.primary100.copy(alpha = 0.6f)
                    )
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
                    onClick = { clickListener.onClose() }
                )
                CallvanItemState.OWNER_CLOSED -> CallvanOutlinedButton(
                    text = stringResource(R.string.callvan_btn_re_recruit),
                    onClick = { clickListener.onReRecruit() },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                )
                else -> {}
            }

            if (state == CallvanItemState.DEFAULT || state == CallvanItemState.OWNER_CLOSED) {
                CallvanFilledButton(
                    text = stringResource(
                        if (state == CallvanItemState.DEFAULT) {
                            R.string.callvan_btn_join
                        } else {
                            R.string.callvan_btn_complete
                        }
                    ),
                    onClick = {
                        if (state == CallvanItemState.DEFAULT) {
                            clickListener.onJoin()
                        } else {
                            clickListener.onComplete()
                        }
                    },
                    contentPadding = if (state == CallvanItemState.OWNER_CLOSED) {
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
    color: ButtonColors = ButtonDefaults.buttonColors(
        contentColor = RebrandKoinTheme.colors.neutral0,
        containerColor = RebrandKoinTheme.colors.primary500
    ),
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
        Button(
            modifier = modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
            onClick = onClick,
            shape = KoinTheme.shapes.extraSmall,
            contentPadding = contentPadding,
            colors = color
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
                if(enabled) borderColor else disabledContentColor
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
        CallvanListItemButtons(state = CallvanItemState.DEFAULT)
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemButtonsJoinedPreview() {
    RebrandKoinTheme {
        CallvanListItemButtons(state = CallvanItemState.JOINED)
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemButtonsClosedPreview() {
    RebrandKoinTheme {
        CallvanListItemButtons(state = CallvanItemState.CLOSED)
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemButtonsOwnerActivePreview() {
    RebrandKoinTheme {
        CallvanListItemButtons(state = CallvanItemState.OWNER_ACTIVE)
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemButtonsOwnerClosedPreview() {
    RebrandKoinTheme {
        CallvanListItemButtons(state = CallvanItemState.OWNER_CLOSED)
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemButtonsOwnerClosesdPreview() {
    RebrandKoinTheme {
        CallvanFilledButton(
            text = stringResource(R.string.callvan_btn_close),
            onClick = {}
        )
    }
}