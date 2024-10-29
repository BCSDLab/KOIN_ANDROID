package `in`.koreatech.koin.feature.timetable.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.FontScalePreviews
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LectureDuplicationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = { onDismiss() },
        modifier = Modifier,
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = KoinTheme.shapes.extraSmall
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 32.dp,
                        vertical = 24.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.lecture_duplication_title),
                    color = KoinTheme.colors.neutral800,
                    textAlign = TextAlign.Center,
                    style = KoinTheme.typography.bold16,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildAnnotatedString {
                        pushStyle(
                            ParagraphStyle(
                                lineBreak = LineBreak(
                                    strategy = LineBreak.Strategy.Balanced,
                                    wordBreak = LineBreak.WordBreak.Phrase,
                                    strictness = LineBreak.Strictness.Normal
                                )
                            )
                        )
                        withStyle(
                            style = KoinTheme.typography.regular14.copy(
                                color = KoinTheme.colors.neutral600,
                            ).toSpanStyle()
                        ) {
                            append("추가하시려는 시간에 이미 다른 강의가 있어요.")
                        }
                        withStyle(
                            style = KoinTheme.typography.regular14.copy(
                                color = KoinTheme.colors.warning600,
                            ).toSpanStyle()
                        ) {
                            append(" 새로운 강의로 대체")
                        }
                        withStyle(
                            style = KoinTheme.typography.regular14.copy(
                                color = KoinTheme.colors.neutral600,
                            ).toSpanStyle()
                        ) {
                            append("하시겠어요?")
                        }
                    },
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.wrapContentHeight(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1.0F),
                        colors = ButtonColors(
                            containerColor = KoinTheme.colors.neutral0,
                            contentColor = KoinTheme.colors.neutral500,
                            disabledContainerColor = KoinTheme.colors.neutral400,
                            disabledContentColor = KoinTheme.colors.neutral500
                        ),
                        shape = MaterialTheme.shapes.extraSmall,
                        contentPadding = PaddingValues(0.dp),
                        border = BorderStroke(1.dp, KoinTheme.colors.neutral500),
                        onClick = { onDismiss() }
                    ) {
                        Text(text = stringResource(id = R.string.lecture_duplication_cancellation), style = KoinTheme.typography.medium15, color = KoinTheme.colors.neutral600)
                    }
                    Button(
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1.0F),
                        colors = ButtonColors(
                            containerColor = KoinTheme.colors.primary500,
                            contentColor = KoinTheme.colors.neutral0,
                            disabledContainerColor = KoinTheme.colors.neutral300,
                            disabledContentColor = KoinTheme.colors.neutral600
                        ),
                        shape = MaterialTheme.shapes.extraSmall,
                        contentPadding = PaddingValues(0.dp),
                        onClick = { onConfirm() }
                    ) {
                        Text(text = stringResource(id = R.string.lecture_duplication_confirmation), style = KoinTheme.typography.medium15, color = KoinTheme.colors.neutral0)
                    }
                }
            }
        }
    }

}

@FontScalePreviews
@Composable
fun LectureDuplicationDialogPreview(modifier: Modifier = Modifier) {
    KoinTheme {
        var isShowing by remember { mutableStateOf(true) }

        if (isShowing) {
            LectureDuplicationDialog(
                onConfirm = {},
                onDismiss = { isShowing = false }
            )
        }
    }
}