package `in`.koreatech.koin.core.navigation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.core.navigation.Navigator

@EntryPoint
@InstallIn(SingletonComponent::class)
interface NavigatorEntryPoint {
    fun navigator(): Navigator
}

@Composable
fun rememberNavigator(): Navigator {
    val context = LocalContext.current

    return remember {
        val entrypoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            NavigatorEntryPoint::class.java
        )

        entrypoint.navigator()
    }
}
