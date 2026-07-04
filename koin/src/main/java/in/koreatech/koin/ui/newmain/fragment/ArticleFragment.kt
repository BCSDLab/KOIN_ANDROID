package `in`.koreatech.koin.ui.newmain.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.article.ArticleActivity
import `in`.koreatech.koin.feature.article.ui.ArticleScreen

@AndroidEntryPoint
class ArticleFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<View>(R.id.nav_host_fragment_main)
            ?.setBackgroundColor(Color.WHITE)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().findViewById<View>(R.id.nav_host_fragment_main)
            ?.background = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                RebrandKoinTheme {
                    ArticleScreen(
                        onOldVersionClick = {
                            startActivity(Intent(requireContext(), ArticleActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}
