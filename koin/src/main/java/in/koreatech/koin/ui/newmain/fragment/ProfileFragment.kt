package `in`.koreatech.koin.ui.newmain.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.home.profile.ProfileScreen
import `in`.koreatech.koin.feature.user.ui.userinfo.UserInfoActivity

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                RebrandKoinTheme {
                    ProfileScreen(
                        onNavigateToSetting = {
                            startActivity(Intent(requireContext(), UserInfoActivity::class.java))
                        },
                        onNavigateToNotification = {
                            findNavController().navigate(R.id.notification)
                        },
                        onRequestLogout = {
                        }
                    )
                }
            }
        }
    }
}
