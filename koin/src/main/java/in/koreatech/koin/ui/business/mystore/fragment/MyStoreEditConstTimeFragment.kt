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

class MyStoreEditConstTimeFragment : Fragment(R.layout.mystore_edit_const_time_fragment) {
    private val binding by dataBinding<MystoreEditConstTimeFragmentBinding>()
    private val viewModel by activityViewModels<MyStoreViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mondayTimeTextview.setOnClickListener { MyStorePickerDialogFragment().show(parentFragmentManager, CONST_TIME_DIALOG_TAG) }

        when(viewModel.stores.value[0].dayOfHoliday){
            MONDAY -> {
                binding.mondayClickCheckBox.isChecked = true
                binding.mondayTimeTextview.text = "${viewModel.stores.value[0].openTime} ~ ${viewModel.stores.value[0].closeTime}"
            }
        }
    }

    companion object{
        const val MONDAY = "MONDAY"
        const val CONST_TIME_DIALOG_TAG = "constTimeDialogTag"
    }
}

