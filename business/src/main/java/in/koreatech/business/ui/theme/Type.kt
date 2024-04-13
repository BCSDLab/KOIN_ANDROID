package `in`.koreatech.business.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R

val Rubik = FontFamily(
    Font(R.font.noto_sans_kr, FontWeight.W500),
    Font(R.font.noto_sans_kr, FontWeight.W400),
)

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = FontFamily.Default,
    h1 = TextStyle(
        color = Color.White,
        fontFamily = Rubik,
        fontWeight = FontWeight.W500,
        fontSize = 24.sp,
    ),
    h2 = TextStyle(
        color = Color.White,
        fontFamily = Rubik,
        fontWeight = FontWeight.W500,
        fontSize = 20.sp,
    ),
    h3 = TextStyle(
        color = Color.White,
        fontFamily = Rubik,
        fontWeight = FontWeight.W500,
        fontSize = 18.sp,
    ),
    h4 = TextStyle(
        color = Color(0xFF4590BB),
        fontFamily = Rubik,
        fontWeight = FontWeight.W500,
        fontSize = 18.sp,
    ),
    h5 = TextStyle(
        color = ColorPrimary,
        fontFamily = Rubik,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
    ),
    h6 = TextStyle(
        color = Color.Black,
        fontFamily = Rubik,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
    ),
    body1 = TextStyle(
        color = Color.Black,
        fontFamily = Rubik,
        fontWeight = FontWeight.W400,
        fontSize = 15.sp,
    ),
    body2 = TextStyle(
        color = Color.Black,
        fontFamily = Rubik,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
    ),
    button = TextStyle(
        color = Color.White,
        fontFamily = Rubik,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        background = ColorPrimary,
    ),
    caption = TextStyle(
        color = Color(0xFFD2DAE2),
        fontFamily = Rubik,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
    ),
    overline = TextStyle(
        color = Color(0xFFCACACA),
        fontFamily = Rubik,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
    )
)
