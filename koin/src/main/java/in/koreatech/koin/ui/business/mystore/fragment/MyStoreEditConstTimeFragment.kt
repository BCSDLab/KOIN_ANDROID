package `in`.koreatech.koin.ui.business.mystore.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.MystoreEditConstTimeFragmentBinding
import `in`.koreatech.koin.ui.business.mystore.viewmodel.MyStoreViewModel
import `in`.koreatech.koin.util.ext.observeLiveData

class MyStoreEditConstTimeFragment : Fragment(R.layout.mystore_edit_const_time_fragment) {
    private val binding by dataBinding<MystoreEditConstTimeFragmentBinding>()
    private val viewModel by activityViewModels<MyStoreViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        onClickCheckBox()
    }

    private fun onClickCheckBox(){
        binding.mondayTimeTextview.setOnClickListener { MyStorePickerDialogFragment().show(parentFragmentManager, CONST_TIME_DIALOG_TAG) }
    }

    private fun observeData(){
        observeLiveData(viewModel.stores){
            when (it.dayOfHoliday){
                MONDAY -> {
                    with(binding){
                        mondayClickCheckBox.isChecked = true
                        mondayTimeTextview.text = "${it.openTime} ~ ${it.closeTime}"
                    }
                }
            }
        }
    }

    companion object{
        const val MONDAY = "MONDAY"
        const val CONST_TIME_DIALOG_TAG = "constTimeDialogTag"
    }
}

