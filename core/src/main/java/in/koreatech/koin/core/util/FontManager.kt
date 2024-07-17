package `in`.koreatech.koin.core.util

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import androidx.core.content.res.ResourcesCompat
import `in`.koreatech.koin.core.R
import java.util.EnumMap


/**
 * 폰트를 불러오는 데 사용하는 클래스
 *
 * Sdk26 미만의 폰트를 로딩하는 데 발생하는 입출력 시간을 줄이기 위해 작성되었습니다.
 *
 * @author DohyeokKim
 */
object FontManager {
    private val cache: EnumMap<KoinFontType, Typeface> = EnumMap(KoinFontType::class.java)

    private val PRETENDARD_BOLD_ASSET = "fonts/pretendard_bold"
    private val PRETENDARD_MEDIUM_ASSET = "fonts/pretendard_medium"
    private val PRETENDARD_REGULAR_ASSET = "fonts/pretendard_regular"

    private val PRETENDARD_BOLD_ID = R.font.pretendard_bold
    private val PRETENDARD_MEDIUM_ID = R.font.pretendard_medium
    private val PRETENDARD_REGULAR_ID = R.font.pretendard_regular

    fun getTypeface(context: Context, fontType: KoinFontType): Typeface {
        return cache[fontType] ?: run {
            val font = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getFromResource(context, fontType)
            } else {
                getFromAsset(context, fontType)
            }
            cache[fontType] = font
            return@run font
        }
    }

    private fun getFromAsset(context: Context, font: KoinFontType): Typeface {
        return when (font) {
            KoinFontType.PRETENDARD_BOLD -> {
                Typeface.createFromAsset(context.assets, PRETENDARD_BOLD_ASSET) ?: Typeface.DEFAULT_BOLD
            }

            KoinFontType.PRETENDARD_MEDIUM -> {
                Typeface.createFromAsset(context.assets, PRETENDARD_MEDIUM_ASSET) ?: Typeface.DEFAULT
            }

            KoinFontType.PRETENDARD_REGULAR -> {
                Typeface.createFromAsset(context.assets, PRETENDARD_REGULAR_ASSET) ?: Typeface.DEFAULT
            }
        }
    }

    private fun getFromResource(context: Context, font: KoinFontType): Typeface {
        return when (font) {
            KoinFontType.PRETENDARD_BOLD -> {
                ResourcesCompat.getFont(context, PRETENDARD_BOLD_ID) ?: Typeface.DEFAULT_BOLD
            }

            KoinFontType.PRETENDARD_MEDIUM -> {
                ResourcesCompat.getFont(context, PRETENDARD_MEDIUM_ID) ?: Typeface.DEFAULT
            }

            KoinFontType.PRETENDARD_REGULAR -> {
                ResourcesCompat.getFont(context, PRETENDARD_REGULAR_ID) ?: Typeface.DEFAULT
            }
        }
    }

    enum class KoinFontType {
        PRETENDARD_BOLD,
        PRETENDARD_MEDIUM,
        PRETENDARD_REGULAR
    }
}