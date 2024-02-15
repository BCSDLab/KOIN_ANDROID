package `in`.koreatech.koin.ui.businessmain

import `in`.koreatech.koin.databinding.CustomAlertDialogBusinessStartBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class BusinessMainStartBusinessDialog : DialogFragment() {

    private var _binding: CustomAlertDialogBusinessStartBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CustomAlertDialogBusinessStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            textViewDialogCheck.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
