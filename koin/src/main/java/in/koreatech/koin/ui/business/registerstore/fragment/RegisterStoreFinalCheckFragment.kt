package `in`.koreatech.koin.ui.business.registerstore.fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.RegisterStoreFinalCheckBinding
import `in`.koreatech.koin.ui.business.registerstore.viewmodel.RegisterStoreViewModel
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import `in`.koreatech.koin.ui.business.registerstore.viewmodel.RegisterOwnerStoreViewModel

class RegisterStoreFinalCheckFragment : Fragment(R.layout.register_store_final_check){
    private val binding by dataBinding<RegisterStoreFinalCheckBinding>()
    private val viewModel by activityViewModels<RegisterStoreViewModel>()
    private val inserStoreViewmodel by activityViewModels<RegisterOwnerStoreViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        binding.viewModelCheck = viewModel
        setFinalInfo()

        binding.checkBoxDelivery.setOnCheckedChangeListener { _, _ ->
            binding.checkBoxDelivery.isChecked = viewModel.detailInfo.value?.isDeliveryOk == true
        }

        binding.checkBoxCard.setOnCheckedChangeListener { _, _ ->
            binding.checkBoxCard.isChecked = viewModel.detailInfo.value?.isCardOk == true
        }

        binding.checkBoxAccount.setOnCheckedChangeListener { _, _ ->
            binding.checkBoxAccount.isChecked = viewModel.detailInfo.value?.isBankOk == true
        }

        binding.backButtonInBasic.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.nextButton.setOnClickListener {
            viewModel.setRegisterStore()
            findNavController().navigate(R.id.register_final_check_fragment_to_register_finish_fragment)
        }
    }

    private fun TextView.setCheckBoxSelected(isSelected: Boolean) {
        setTextColor(
            ContextCompat.getColor(
                context,
                if (isSelected) R.color.colorAccent else R.color.colorSecondaryText
            )
        )
    }

    private fun setFinalInfo(){

        viewModel.detailInfo.value?.let { binding.checkBoxDelivery.setCheckBoxSelected(it.isDeliveryOk) }
        viewModel.detailInfo.value?.let { binding.checkBoxCard.setCheckBoxSelected(it.isCardOk) }
        viewModel.detailInfo.value?.let { binding.checkBoxAccount.setCheckBoxSelected(it.isBankOk) }
    }
}