package `in`.koreatech.koin.ui.business.mystore

import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityMyStoreBinding
import `in`.koreatech.koin.ui.business.mystore.fragment.MyStoreMainFragment
import `in`.koreatech.koin.ui.business.mystore.viewmodel.MyStoreViewModel

class MyStoreActivity : ActivityBase() {
    private val binding by dataBinding<ActivityMyStoreBinding>(R.layout.activity_my_store)
    private val viewModel by viewModels<MyStoreViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
