package `in`.koreatech.koin.ui.business.mystore.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import `in`.koreatech.koin.databinding.ConstTimePickerDialogFragmentBinding
import `in`.koreatech.koin.ui.business.mystore.dialog.dialogFragmentResize
import `in`.koreatech.koin.ui.business.mystore.viewmodel.MyStoreViewModel
import java.text.DecimalFormat

class MyStorePickerDialogFragment : DialogFragment() {
    private var _binding: ConstTimePickerDialogFragmentBinding? = null
    private val binding: ConstTimePickerDialogFragmentBinding get() = _binding!!
    private val viewModel by activityViewModels<MyStoreViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NO_TITLE, R.style.MyStorePickerDialogStyle)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ConstTimePickerDialogFragmentBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
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
        setNumberPickerNum(binding.startTimeHour, numbersHours)
        setNumberPickerNum(binding.startTimeMinutes, numbersMinutes)
        setNumberPickerNum(binding.endTimeHour, numbersHours)
        setNumberPickerNum(binding.endTimeMinutes, numbersMinutes)

        binding.closeTextview.setOnClickListener {
            dismiss()
        }
        binding.confirmTextview.setOnClickListener { }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(
            this@MyStorePickerDialogFragment,
            0.9f,
            0.375f
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setNumberPickerNum(numberPicker: NumberPicker, numbers: List<String>) {
        with(numberPicker) {
            minValue = 0
            maxValue = numbers.size - 1
            wrapSelectorWheel = true
            displayedValues = numbers.toTypedArray()
        }
    }

    companion object {
        fun newInstance(): MyStorePickerDialogFragment {
            val args = Bundle()

            val fragment = MyStorePickerDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}