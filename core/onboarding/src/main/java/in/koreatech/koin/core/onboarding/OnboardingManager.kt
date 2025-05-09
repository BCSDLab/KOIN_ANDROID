package `in`.koreatech.koin.core.onboarding

import android.content.Context
import android.view.View
import androidx.annotation.FloatRange
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.skydoves.balloon.ArrowOrientationRules
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.IconForm
import com.skydoves.balloon.IconGravity
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.BalloonWindow
import `in`.koreatech.koin.domain.repository.OnboardingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class OnboardingManager @Inject internal constructor(
    private val onboardingRepository: OnboardingRepository,
    private val context: Context
) {
    private lateinit var tooltip: Balloon
    private val tooltipDismissObserver =
        object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                if (::tooltip.isInitialized) {
                    tooltip.dismiss()
                }
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
        arrowDirection: ArrowDirection
    ) {
        lifecycle.addObserver(tooltipDismissObserver)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val shouldShow = onboardingRepository.getShouldOnboarding(type.name)
                delay(200)
                if (shouldShow) {
                    tooltip = createTooltip(type, arrowDirection, arrowPosition)
                    tooltip.showAlign(view, arrowDirection)
                    onboardingRepository.updateShouldOnboarding(type.name, false)
                }
            }
        }
    }

    /**
     * 앱 최초 실행 시에 실행시키고 싶은 액션이 있는 경우 사용
     * @param action 실행시킬 액션. ex) BottomSheetDialogFragment.show()
     */
    fun LifecycleOwner.showOnboardingIfNeeded(
        type: OnboardingType,
        action: () -> Unit
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val shouldShow = onboardingRepository.getShouldOnboarding(type.name)
                if (shouldShow) {
                    action()
                    onboardingRepository.updateShouldOnboarding(type.name, false)
                }
            }
        }
    }

    fun getShouldOnboardFlow(type: OnboardingType) = onboardingRepository.getShouldOnboardingFlow(type.name)

    suspend fun getShouldOnboard(type: OnboardingType): Boolean {
        return onboardingRepository.getShouldOnboarding(type.name)
    }

    suspend fun updateShouldOnboard(
        type: OnboardingType,
        shouldShow: Boolean
    ) {
        onboardingRepository.updateShouldOnboarding(type.name, shouldShow)
    }

    fun dismissTooltip() {
        if (::tooltip.isInitialized) {
            tooltip.dismiss()
        }
    }

    private fun createBuilder(
        type: OnboardingType,
        arrowDirection: ArrowDirection,
        arrowPosition: Float
    ): Balloon.Builder {
        val iconForm =
            IconForm.Builder(context)
                .setDrawable(AppCompatResources.getDrawable(context, R.drawable.round_close_24))
                .setIconSize(32)
                .setDrawableGravity(IconGravity.END)
                .build()

        return Balloon.Builder(context)
            .setHeight(BalloonSizeSpec.WRAP)
            .setWidth(BalloonSizeSpec.WRAP)
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
            .apply {
                if (type.descriptionResId != 0) {
                    setText(context.getString(type.descriptionResId))
                }
            }
    }

    private fun createTooltip(
        type: OnboardingType,
        arrowDirection: ArrowDirection,
        arrowPosition: Float
    ): Balloon {
        return createBuilder(
            type = type,
            arrowDirection = arrowDirection,
            arrowPosition = arrowPosition
        ).build()
    }

    private fun Balloon.showAlign(
        view: View,
        arrowDirection: ArrowDirection
    ) {
        when (arrowDirection) {
            ArrowDirection.BOTTOM -> showAlignTop(view)
            ArrowDirection.TOP -> showAlignBottom(view)
            ArrowDirection.LEFT -> showAlignRight(view)
            ArrowDirection.RIGHT -> showAlignLeft(view)
        }
    }

    /**
     * 앱 실행 최초 1회에만 툴팁 표시
     * showOnboardingTooltipIfNeeded의 Compose 버전
     * @param type 툴팁 타입
     * @param arrowPosition 화살표 위치 (0.0 ~ 1.0)
     * @param arrowDirection 툴팁 화살표 방향 (ex. ArrowDirection.LEFT -> 화살표는 왼쪽방향, 툴팁은 오른쪽에 위치)
     * @param content 툴팁이 표시될 content
     *
     * ```
     * val onboardingManager = rememberOnboardingManager()
     *
     * with(onboardingManager) {
     *     ShowOnboardingTooltipIfNeeded(
     *         type = OnboardingType.DINING_IMAGE,
     *         arrowDirection = ArrowDirection.LEFT
     *     ) {
     *         // put content here
     *       }
     *  }
     *  ```
     */
    @Composable
    fun ShowOnboardingTooltipIfNeeded(
        type: OnboardingType,
        @FloatRange(from = 0.0, to = 1.0) arrowPosition: Float = 0.5f,
        arrowDirection: ArrowDirection,
        content: @Composable () -> Unit
    ) {
        var balloonWindow: BalloonWindow? by remember { mutableStateOf(null) }

        val builder = remember { createBuilder(type = type, arrowDirection = arrowDirection, arrowPosition = arrowPosition) }

        Balloon(
            builder = builder,
            onBalloonWindowInitialized = { balloonWindow = it }
        ) {
            content()
        }

        LaunchedEffect(Unit) {
            val shouldShow = onboardingRepository.getShouldOnboarding(type.name)
            delay(200)
            if (shouldShow) {
                balloonWindow?.apply {
                    when (arrowDirection) {
                        ArrowDirection.BOTTOM -> showAlignTop()
                        ArrowDirection.TOP -> showAlignBottom()
                        ArrowDirection.LEFT -> showAlignEnd()
                        ArrowDirection.RIGHT -> showAlignStart()
                    }
                }
                onboardingRepository.updateShouldOnboarding(type.name, false)
            }
        }
    }
}
