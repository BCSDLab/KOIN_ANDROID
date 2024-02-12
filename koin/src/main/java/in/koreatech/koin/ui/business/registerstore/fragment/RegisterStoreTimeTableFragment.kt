package `in`.koreatech.koin.ui.business.registerstore.fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.RegisterStoreTimeTableBinding
import `in`.koreatech.koin.domain.model.business.mystore.Holiday
import `in`.koreatech.koin.ui.business.registerstore.viewmodel.RegisterStoreViewModel
import `in`.koreatech.koin.util.ext.observeLiveData
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class RegisterStoreTimeTableFragment : Fragment(R.layout.register_store_time_table){
    private val binding by dataBinding<RegisterStoreTimeTableBinding>()
    private val viewModel by activityViewModels<RegisterStoreViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        setHoliday()
        binding.viewModelTime = viewModel

        binding.initTime.setOnClickListener{
            RegisterStoreTimeSetter().show(parentFragmentManager, CONST_TIME_DIALOG_TAG)
        }

        binding.mondayClickCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.mondayTextview.setCheckBoxSelected(isChecked)
            binding.mondayTimeTextview.setCheckBoxSelected(isChecked)
            Holiday.Mon.isHoliday = isChecked
        }

        binding.tuesdayClickCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.tuesdayTextview.setCheckBoxSelected(isChecked)
            binding.tuesdayTimeTextview.setCheckBoxSelected(isChecked)
            Holiday.Tues.isHoliday = isChecked
        }

        binding.wednesdayClickCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.wednesdayTextview.setCheckBoxSelected(isChecked)
            binding.wednesdayTimeTextview.setCheckBoxSelected(isChecked)
            Holiday.Wed.isHoliday = isChecked
        }

        binding.thursdayClickCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.thursdayTextview.setCheckBoxSelected(isChecked)
            binding.thursdayTimeTextview.setCheckBoxSelected(isChecked)
            Holiday.Thurs.isHoliday = isChecked
        }

        binding.fridayClickCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.fridayTextview.setCheckBoxSelected(isChecked)
            binding.fridayTimeTextview.setCheckBoxSelected(isChecked)
            Holiday.Fri.isHoliday = isChecked
        }

        binding.saturdayClickCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.saturdayTextview.setCheckBoxSelected(isChecked)
            binding.saturdayTimeTextview.setCheckBoxSelected(isChecked)
            Holiday.Sat.isHoliday = isChecked
        }

        binding.sundayClickCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.sundayTextview.setCheckBoxSelected(isChecked)
            binding.sundayTimeTextview.setCheckBoxSelected(isChecked)
            Holiday.Sun.isHoliday = isChecked
        }

        binding.backButtonInBasic.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.nextButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        observeLiveData(viewModel.storeTime){
            setTime()
        }
    }


    private fun setHoliday(){
        Holiday.Mon.isHoliday = false
        Holiday.Tues.isHoliday = false
        Holiday.Wed.isHoliday = false
        Holiday.Thurs.isHoliday = false
        Holiday.Fri.isHoliday = false
        Holiday.Sat.isHoliday = false
        Holiday.Sun.isHoliday = false

    }

    private fun setTime(){
        binding.mondayTimeTextview.text = viewModel.getTimeString()
        binding.tuesdayTimeTextview.text = viewModel.getTimeString()
        binding.wednesdayTimeTextview.text = viewModel.getTimeString()
        binding.thursdayTimeTextview.text = viewModel.getTimeString()
        binding.fridayTimeTextview.text = viewModel.getTimeString()
        binding.saturdayTimeTextview.text = viewModel.getTimeString()
        binding.sundayTimeTextview.text = viewModel.getTimeString()
    }

    companion object{
        const val CONST_TIME_DIALOG_TAG = "constTimeDialogTag"
    }

    private fun TextView.setCheckBoxSelected(isSelected: Boolean) {
        setTextColor(
            ContextCompat.getColor(
                context,
                if (isSelected) R.color.colorSecondaryText else R.color.black
            )
        )
    }

}
