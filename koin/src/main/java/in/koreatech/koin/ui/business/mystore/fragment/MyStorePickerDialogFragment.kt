package `in`.koreatech.koin.ui.business.mystore.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ConstTimePickerDialogFragmentBinding
import `in`.koreatech.koin.ui.business.mystore.dialog.dialogFragmentResize
import `in`.koreatech.koin.ui.business.mystore.viewmodel.MyStoreViewModel
import java.text.DecimalFormat

class MyStorePickerDialogFragment : DialogFragment(R.layout.const_time_picker_dialog_fragment) {
    private val binding by dataBinding<ConstTimePickerDialogFragmentBinding>()
    private val viewModel by activityViewModels<MyStoreViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NO_TITLE, R.style.MyStorePickerDialogStyle)
        isCancelable = true
    }

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
        val openTime = viewModel.stores.value?.openTime.toString().split(":")
        val closeTime = viewModel.stores.value?.closeTime.toString().split(":")

        setNumberPickerNum(binding.startTimeHour, numbersHours, openTime[0].toInt())
        setNumberPickerNum(binding.startTimeMinutes, numbersMinutes, openTime[1].toInt() / 5)
        setNumberPickerNum(binding.endTimeHour, numbersHours, closeTime[0].toInt())
        setNumberPickerNum(binding.endTimeMinutes, numbersMinutes, closeTime[1].toInt() / 5)

        binding.closeTextview.setOnClickListener { dismiss() }
        binding.confirmTextview.setOnClickListener {  }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(
            this@MyStorePickerDialogFragment,
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


}
