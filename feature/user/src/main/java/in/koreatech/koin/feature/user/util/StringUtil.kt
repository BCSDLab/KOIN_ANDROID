package `in`.koreatech.koin.feature.user.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

/**
 * Load a string resource with formatting and span style.
 *
 * @param id the resource id of the string
 * @param formatArgs vararg pairs of [String] and [SpanStyle]
 * @return the [AnnotatedString] with style applied
 */
@Composable
@ReadOnlyComposable
fun stringResourceWithStyle(@StringRes id: Int, vararg formatArgs: Pair<String, SpanStyle>): AnnotatedString {
    val resources = LocalContext.current.resources

    val string = resources.getString(id, *formatArgs.map { it.first }.toTypedArray())

    var index = 0

    return buildAnnotatedString {
        append(string)
        for (arg in formatArgs) {
            index = string.indexOf(arg.first, startIndex = index)
            addStyle(arg.second, index, index + arg.first.length)
        }
    }
}
