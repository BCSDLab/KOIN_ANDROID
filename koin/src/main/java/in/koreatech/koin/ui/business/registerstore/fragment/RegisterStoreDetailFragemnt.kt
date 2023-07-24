package `in`.koreatech.koin.ui.business.registerstore.fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.RegisterStoreDetailInfoBinding
import `in`.koreatech.koin.domain.model.business.mystore.RegisterStoreBasic
import `in`.koreatech.koin.domain.model.business.mystore.RegisterStoreDetail
import `in`.koreatech.koin.ui.business.registerstore.viewmodel.RegisterStoreViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.textString
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

class RegisterStoreDetailFragemnt : Fragment(R.layout.register_store_detail_info){
    private val binding by dataBinding<RegisterStoreDetailInfoBinding>()
    private val viewModel by activityViewModels<RegisterStoreViewModel>()
    private lateinit var detailInfo: RegisterStoreDetail

    var isDeliveryOk: Boolean = false
    var isCardOk: Boolean = false
    var isAccountOk: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        binding.timeTextview.text = viewModel.getTimeString()
        binding.timeInfoTextview.text = viewModel.getHoliday()

        binding.checkBoxDelivery.setOnCheckedChangeListener { _, isChecked ->
            isDeliveryOk = isChecked
            binding.checkBoxDelivery.setCheckBoxSelected(isChecked)
        }

        binding.checkBoxCard.setOnCheckedChangeListener { _, isChecked ->
            isCardOk = isChecked
            binding.checkBoxCard.setCheckBoxSelected(isChecked)
        }

        binding.checkBoxAccount.setOnCheckedChangeListener { _, isChecked ->
            isAccountOk = isChecked
            binding.checkBoxAccount.setCheckBoxSelected(isChecked)
        }

        binding.nextButton.setOnClickListener {
            if (
                binding.initPhoneNumber.textString.isBlank()||
                binding.initDeliveryAmount.textString.isBlank() ||
                binding.initOtherInfo.textString.isBlank()
            ) {
                SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.detail_store_required_field_not_filled))
            } else {
                detailInfo = RegisterStoreDetail(binding.initPhoneNumber.textString,binding.initDeliveryAmount.textString,
                                                    binding.initOtherInfo.textString, isDeliveryOk, isCardOk, isAccountOk)
                viewModel.setDetailInfo(detailInfo)
                findNavController().navigate(R.id.register_detail_info_fragment_to_register_final_check_fragment)
            }
        }

        binding.timeModifyButton.setOnClickListener {
            findNavController().navigate(R.id.register_detail_info_fragment_to_time_table_fragment)
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
}