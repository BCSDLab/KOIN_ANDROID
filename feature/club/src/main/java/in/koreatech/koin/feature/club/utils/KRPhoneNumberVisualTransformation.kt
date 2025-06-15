package `in`.koreatech.koin.feature.club.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class KRPhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 11) text.text.substring(0..10) else text.text

        var out = ""
        for (i in trimmed.indices) {
            if (i == 3 || i == 7) out += "-"
            out += trimmed[i]
        }
        return TransformedText(AnnotatedString(out), phoneNumberOffsetTranslator)
    }

    private val phoneNumberOffsetTranslator = object : OffsetMapping {

        override fun originalToTransformed(offset: Int): Int =
            when (offset) {
                in 0..3 -> offset
                in 4..7 -> offset + 1
                else -> offset + 2
            }

        override fun transformedToOriginal(offset: Int): Int =
            when (offset) {
                in 0..2 -> offset
                in 3..6 -> offset - 1
                else -> offset - 2
            }
    }
}