package `in`.koreatech.koin.ui.store.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import `in`.koreatech.koin.databinding.SelectStoreBottomSheetBinding
import `in`.koreatech.koin.ui.store.viewmodel.StoreViewModel
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoreBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: SelectStoreBottomSheetBinding? = null
    private val binding: SelectStoreBottomSheetBinding get() = _binding!!
    private val viewModel by activityViewModels<StoreViewModel>()

    private var storeId = -1
    private var storePhoneNumber = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return SelectStoreBottomSheetBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.setNeedToProceedStoreInfo(false, "", -1, "")

                viewModel.store.collectLatest {
                    binding.storeNameText.text = it?.name ?: ""
                    binding.storePhoneText.text = it?.phone ?: ""
                    storeId = it?.uid ?: -1
                    storePhoneNumber = it?.phone ?: ""
                }
            }
        }

        binding.storeSelectButton.setOnClickListener {
            viewModel.setNeedToProceedStoreInfo(true, binding.storeNameText.text.toString(), storeId, storePhoneNumber)
            dismiss()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}