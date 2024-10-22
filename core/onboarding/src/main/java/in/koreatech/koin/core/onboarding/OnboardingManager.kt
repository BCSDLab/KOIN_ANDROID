package `in`.koreatech.koin.core.onboarding

import android.content.Context
import android.view.View
import androidx.annotation.FloatRange
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.ArrowOrientationRules
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.IconForm
import com.skydoves.balloon.IconGravity
import `in`.koreatech.koin.domain.repository.OnboardingRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class ArrowDirection {
    BOTTOM, TOP, LEFT, RIGHT
}

class OnboardingManager @Inject internal constructor(
    private val onboardingRepository: OnboardingRepository,
    private val context: Context,
    private val mainDispatcher: CoroutineDispatcher
) {

    private lateinit var tooltip: Balloon
    private val tooltipDismissObserver = object : DefaultLifecycleObserver {
        override fun onPause(owner: LifecycleOwner) {
            tooltip.dismiss()
            super.onPause(owner)
        }
    }

    /**
     * 앱 실행 최초 1회에만 툴팁 표시
     * @param type 툴팁 타입
     * @param view 툴팁을 위치시킬 뷰
     * @param arrowPosition 화살표 위치 (0.0 ~ 1.0)
     * @param arrowDirection 툴팁 화살표 방향 (ex. ArrowDirection.LEFT -> 화살표는 왼쪽방향, 툴팁은 오른쪽에 위치)
     * ```
     * // In Activity
     * @Inject
     * lateinit var onboardingManager: OnboardingManager
     * ...
     *
     * with(onboardingManager) {
     *     showOnboardingTooltipIfNeeded(
     *         type = OnboardingType.DINING_IMAGE,
     *         view = binding.textViewDiningTitle,
     *         arrowDirection = ArrowDirection.LEFT
     *     )
     * }
     * ```
     *
     * binding.textViewDiningTitle 오른쪽에 툴팁 표시됨.
     *
     * Fragment에서 사용할 때는 viewLifecycleOwner를 사용해야 함 !
     */
    fun LifecycleOwner.showOnboardingTooltipIfNeeded(
        type: OnboardingType,
        view: View,
        @FloatRange(from = 0.0, to = 1.0) arrowPosition: Float = 0.5f,
        arrowDirection: ArrowDirection,
    ) {
        lifecycle.addObserver(tooltipDismissObserver)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val shouldShow = onboardingRepository.getShouldShowTooltip(type.name)
                delay(500)
                withContext(mainDispatcher) {
                    if (shouldShow) {
                        tooltip = createTooltip(type, arrowDirection, arrowPosition)
                        tooltip.showAlign(view, arrowDirection)
                        onboardingRepository.updateShouldShowTooltip(type.name, false)
                    }
                }
            }
        }
    }

    private fun createTooltip(
        type: OnboardingType,
        arrowDirection: ArrowDirection,
        arrowPosition: Float
    ): Balloon {
        val iconForm = IconForm.Builder(context)
            .setDrawable(AppCompatResources.getDrawable(context, R.drawable.round_close_24))
            .setIconSize(32)
            .setDrawableGravity(IconGravity.END)
            .build()

        return Balloon.Builder(context)
            .setHeight(BalloonSizeSpec.WRAP)
            .setWidth(BalloonSizeSpec.WRAP)
            .setText(context.getString(type.descriptionResId))
            .setTextColorResource(R.color.white)
            .setBackgroundColorResource(R.color.neutral_600)
            .setTextSize(12f)
            .setArrowOrientationRules(ArrowOrientationRules.ALIGN_FIXED)
            .setArrowOrientation(arrowDirection.toArrowOrientation())
            .setArrowPositionRules(ArrowPositionRules.ALIGN_BALLOON)
            .setArrowSize(10)
            .setArrowPosition(arrowPosition)
            .setPaddingVertical(8)
            .setPaddingHorizontal(10)
            .setIconForm(iconForm)
            .setMargin(10)
            .setDismissWhenTouchOutside(false)
            .setDismissWhenClicked(true)
            .setCornerRadius(8f)
            .setBalloonAnimation(BalloonAnimation.FADE)
            .build()
    }

    private fun ArrowDirection.toArrowOrientation(): ArrowOrientation {
        return when (this) {
            ArrowDirection.BOTTOM -> ArrowOrientation.BOTTOM
            ArrowDirection.TOP -> ArrowOrientation.TOP
            ArrowDirection.LEFT -> ArrowOrientation.START
            ArrowDirection.RIGHT -> ArrowOrientation.END
        }
    }

    private fun Balloon.showAlign(view: View, arrowDirection: ArrowDirection) {
        when (arrowDirection) {
            ArrowDirection.BOTTOM -> showAlignTop(view)
            ArrowDirection.TOP -> showAlignBottom(view)
            ArrowDirection.LEFT -> showAlignRight(view)
            ArrowDirection.RIGHT -> showAlignLeft(view)
        }
    }
}