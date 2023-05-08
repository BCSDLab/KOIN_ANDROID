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
//    override val menuState: MenuState
//        get() = MenuState.BusinessMyStore

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
        initViewModel()

        onClickStoreEditButton()
    }

    private fun initViewModel(){
        withLoading(this@MyStoreActivity, viewModel)

        observeLiveData(viewModel.myStoreState){ state ->
            when (state){
                MyStoreEnum.STORE_EDIT -> {
                    createFragment(
                        myStoreEditFragment,
                        MyStoreTAG.MY_STORE_STORE_EDIT_TAG
                    )
                }
                MyStoreEnum.CONST_TIME_EDIT -> {
                    createFragment(
                        myStoreConstTimeEditFragment,
                        MyStoreTAG.MY_STORE_CONST_TIME_EDIT_TAG
                    )
                }

                MyStoreEnum.TIME_EDIT -> {
                    myStorePickerDialogFragment.show(supportFragmentManager, MyStoreTAG.MY_STORE_TIME_EDIT_TAG)
                }
            }
        }
    }

    private fun createFragment(fragment: Fragment, tag:String){
        supportFragmentManager.beginTransaction()
            .add(R.id.container_frame_layout_edit_content, fragment, tag)
            .commit()
    }

    private fun onClickStoreEditButton(){
        binding.myStoreEditContent.setOnClickListener {
            viewModel.changeMyStoreState(MyStoreEnum.STORE_EDIT)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    object MyStoreTAG{
        const val MY_STORE_STORE_EDIT_TAG ="MY_STORE_STORE_EDIT_TAG"
        const val MY_STORE_CONST_TIME_EDIT_TAG = "MY_STORE_CONST_TIME_EDIT_TAG"
        const val MY_STORE_TIME_EDIT_TAG = "MY_STORE_TIME_EDIT_TAG"
    }
}
