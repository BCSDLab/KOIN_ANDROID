package `in`.koreatech.koin.ui.dining

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityDiningBinding
import `in`.koreatech.koin.ui.dining.adapter.DiningItemsViewPager2Adapter
import `in`.koreatech.koin.ui.dining.viewmodel.DiningViewModel
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.util.ext.withLoading

@AndroidEntryPoint
class DiningActivity : KoinNavigationDrawerActivity() {
    override val menuState: MenuState = MenuState.Dining
    val binding: ActivityDiningBinding by dataBinding<ActivityDiningBinding>(R.layout.activity_dining)
    override val screenTitle = "식단"
    private val viewModel by viewModels<DiningViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewPager()

        withLoading(this, viewModel)
        viewModel.getDining()
    }

    private fun initViewPager() {
        with(binding) {
            diningViewPager.apply {
                offscreenPageLimit = 3
                adapter = DiningItemsViewPager2Adapter(this@DiningActivity)
            }
            TabLayoutMediator(tabsDiningTime, diningViewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.dining_breakfast)
                    1 -> getString(R.string.dining_lunch)
                    2 -> getString(R.string.dining_dinner)
                    else -> throw IllegalArgumentException("Position must be lower than ${diningViewPager.offscreenPageLimit}")
                }
            }.attach()
        }
    }
}