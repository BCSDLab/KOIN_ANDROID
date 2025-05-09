package `in`.koreatech.koin.core.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.android.EntryPointAccessors

@Composable
fun rememberOnboardingManager(): OnboardingManager {
    val context = LocalContext.current

    return remember {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            OnboardingModuleEntryPoint::class.java
        )

        entryPoint.onboardingManager()
    }
}