package `in`.koreatech.koin.ui.business.mystore

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import `in`.koreatech.koin.R
import `in`.koreatech.koin.constant.MyStoreEnum
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityMyStoreBinding
import `in`.koreatech.koin.ui.business.mystore.fragment.MyStoreConstTimeEditFragment
import `in`.koreatech.koin.ui.business.mystore.fragment.MyStoreEditFragment
import `in`.koreatech.koin.ui.business.mystore.fragment.MyStorePickerDialogFragment
import `in`.koreatech.koin.ui.business.mystore.viewmodel.MyStoreViewModel
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.withLoading

class MyStoreActivity : ActivityBase() {
    private val binding by dataBinding<ActivityMyStoreBinding>(R.layout.activity_my_store)
    private val viewModel by viewModels<MyStoreViewModel>()
    private val myStoreEditFragment: MyStoreEditFragment by lazy {
        MyStoreEditFragment.newInstance()
    }
    private val myStoreConstTimeEditFragment: MyStoreConstTimeEditFragment by lazy {
        MyStoreConstTimeEditFragment.newInstance()
    }
    private val myStorePickerDialogFragment: MyStorePickerDialogFragment by lazy {
        MyStorePickerDialogFragment.newInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}
