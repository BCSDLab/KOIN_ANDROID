package `in`.koreatech.koin.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.core.designsystem.R

@Immutable
data class KoinTypography(
    val regular10: TextStyle,
    val regular12: TextStyle,
    val regular13: TextStyle,
    val regular14: TextStyle,
    val regular15: TextStyle,
    val regular16: TextStyle,
    val regular18: TextStyle,
    val medium12: TextStyle,
    val medium13: TextStyle,
    val medium14: TextStyle,
    val medium15: TextStyle,
    val medium16: TextStyle,
    val medium18: TextStyle,
    val medium20: TextStyle,
    val bold12: TextStyle,
    val bold13: TextStyle,
    val bold14: TextStyle,
    val bold15: TextStyle,
    val bold16: TextStyle,
    val bold18: TextStyle,
    val bold20: TextStyle
)

internal val Pretendard = FontFamily(
    Font(R.font.pretendard_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.pretendard_bold, FontWeight.W600, FontStyle.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal),
)

internal val DefaultTextStyle: TextStyle = TextStyle(
    fontStyle = FontStyle.Normal,
    fontFamily = Pretendard,
    platformStyle = PlatformTextStyle(
        includeFontPadding = false
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    letterSpacing = 0.sp
)

internal val RegularStyle1 = DefaultTextStyle.copy(
    fontSize = 10.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 16.sp
)
internal val RegularStyle2 = DefaultTextStyle.copy(
    fontSize = 12.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 19.2.sp
)
internal val RegularStyle3 = DefaultTextStyle.copy(
    fontSize = 13.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 20.8.sp
)
internal val RegularStyle4 = DefaultTextStyle.copy(
    fontSize = 14.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 22.4.sp
)
internal val RegularStyle5 = DefaultTextStyle.copy(
    fontSize = 15.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 24.sp
)
internal val RegularStyle6 = DefaultTextStyle.copy(
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 25.6.sp
)
internal val RegularStyle7 = DefaultTextStyle.copy(
    fontSize = 18.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 28.8.sp
)


internal val MediumStyle1 = DefaultTextStyle.copy(
    fontSize = 12.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 19.2.sp
)
internal val MediumStyle2 = DefaultTextStyle.copy(
    fontSize = 13.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 20.8.sp
)
internal val MediumStyle3 = DefaultTextStyle.copy(
    fontSize = 14.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 22.4.sp
)
internal val MediumStyle4 = DefaultTextStyle.copy(
    fontSize = 15.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 24.sp
)
internal val MediumStyle5 = DefaultTextStyle.copy(
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 25.6.sp
)
internal val MediumStyle6 = DefaultTextStyle.copy(
    fontSize = 18.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 28.8.sp
)

internal val MediumStyle7 = DefaultTextStyle.copy(
    fontSize = 20.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 30.sp
)

internal val BoldStyle1 = DefaultTextStyle.copy(
    fontSize = 12.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 19.2.sp
)
internal val BoldStyle2 = DefaultTextStyle.copy(
    fontSize = 13.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 20.8.sp
)
internal val BoldStyle3 = DefaultTextStyle.copy(
    fontSize = 14.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 22.4.sp
)
internal val BoldStyle4 = DefaultTextStyle.copy(
    fontSize = 15.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 24.sp
)
internal val BoldStyle5 = DefaultTextStyle.copy(
    fontSize = 16.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 25.6.sp
)
internal val BoldStyle6 = DefaultTextStyle.copy(
    fontSize = 18.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 28.8.sp
)
internal val BoldStyle7 = DefaultTextStyle.copy(
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 30.sp
)