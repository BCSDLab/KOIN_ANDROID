package `in`.koreatech.koin.ui.business.registerstore.fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ConstTimePickerDialogFragmentBinding
import `in`.koreatech.koin.domain.model.business.mystore.RegisterStoreTime
import `in`.koreatech.koin.ui.business.mystore.dialog.dialogFragmentResize
import `in`.koreatech.koin.ui.business.registerstore.viewmodel.RegisterStoreViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import java.text.DecimalFormat

class RegisterStoreTimeSetter: DialogFragment(R.layout.const_time_picker_dialog_fragment) {
    private val binding by dataBinding<ConstTimePickerDialogFragmentBinding>()
    private val viewModel by activityViewModels<RegisterStoreViewModel>()
    lateinit var timeSet: RegisterStoreTime

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val numbersHours = mutableListOf<String>()
        val numbersMinutes = mutableListOf<String>()
        for (i in 0..23) {
            numbersHours.add(DecimalFormat("00").format(i))
        }
        for (i in 0..55 step 5) {
            numbersMinutes.add(DecimalFormat("00").format(i))
        }
        var openTimeModify = "00:00"
        var closeTimeModify = "00:00"
        val openTime = openTimeModify.split(":")
        val closeTime = closeTimeModify.split(":")

        setNumberPickerNum(binding.startTimeHour, numbersHours, openTime[0].toInt())
        setNumberPickerNum(binding.startTimeMinutes, numbersMinutes, openTime[1].toInt() / 5)
        setNumberPickerNum(binding.endTimeHour, numbersHours, closeTime[0].toInt())
        setNumberPickerNum(binding.endTimeMinutes, numbersMinutes, closeTime[1].toInt() / 5)

        binding.closeTextview.setOnClickListener { dismiss() }
        binding.confirmTextview.setOnClickListener {
            if(checkValidTime(binding.startTimeHour.value,binding.endTimeHour.value, binding.startTimeMinutes.value, binding.endTimeMinutes.value)){
                openTimeModify = numbersHours[binding.startTimeHour.value] + ":" + numbersMinutes[binding.startTimeMinutes.value]
                closeTimeModify = numbersHours[binding.endTimeHour.value] + ":" + numbersMinutes[binding.endTimeMinutes.value]
                timeSet = RegisterStoreTime(openTimeModify, closeTimeModify)
                viewModel.setTimeInfo(timeSet)
            dismiss()
            }
            else{
                SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.valid_time_not_filled))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(
            this@RegisterStoreTimeSetter,
            0.9f,
            0.375f
        )
    }

    private fun setNumberPickerNum(numberPicker: NumberPicker, numbers: List<String>, setVal:Int) {
        with(numberPicker) {
            minValue = 0
            maxValue = numbers.size - 1
            wrapSelectorWheel = true
            displayedValues = numbers.toTypedArray()
            value = setVal
        }
    }

    private fun checkValidTime(startHour: Int, endHour:Int, startMinute:Int, endMinute:Int) :Boolean{
        return if(startHour < endHour) true
        else if (startHour == endHour){
            startMinute < endMinute
        }
        else false
    }
}