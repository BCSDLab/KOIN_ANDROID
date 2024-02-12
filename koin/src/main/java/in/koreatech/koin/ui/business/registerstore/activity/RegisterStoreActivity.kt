package `in`.koreatech.koin.ui.business.registerstore.activity

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityRegisterMyStoreBinding
import `in`.koreatech.koin.ui.business.registerstore.fragment.RegisterStoreCategoryFragment
import `in`.koreatech.koin.ui.business.registerstore.fragment.RegisterStoreFragment
import `in`.koreatech.koin.ui.business.registerstore.viewmodel.RegisterStoreViewModel
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterStoreActivity : ActivityBase() {
    private val binding by dataBinding<ActivityRegisterMyStoreBinding>(R.layout.activity_register_my_store)
    private val viewModel by viewModels<RegisterStoreViewModel>()
    private val registerStoreFragent = RegisterStoreFragment()
    private val registerStoreCategoryFragment = RegisterStoreCategoryFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }


    fun goToCategory(){
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container, registerStoreCategoryFragment)
            commit()
        }
    }
}