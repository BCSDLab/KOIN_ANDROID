package `in`.koreatech.koin.ui.business.insertmemu.dialog

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import `in`.koreatech.koin.databinding.InsertMenuBottomSheetBinding
import `in`.koreatech.koin.ui.business.insertmemu.viewmodel.InsertMenuViewModel

class ReviseMenuBottomSheet(position: Int) : BottomSheetDialogFragment()  {
    lateinit var binding: InsertMenuBottomSheetBinding
    private val viewModel: InsertMenuViewModel by activityViewModels()
    private val selectImageFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
        uri?.let {
            Log.d("갤러리", position.toString())
            viewModel.reviseUri(uri, position)
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = InsertMenuBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancelDialog.setOnClickListener {
            dismiss()
        }
        binding.buttonCamera.setOnClickListener {
            Log.d("카메라", "카메라 버튼 클릭")
        }
        binding.buttonGallery.setOnClickListener {
            selectImageFromGallery.launch("image/*")
        }

    }

    companion object {
        const val TAG = "BasicBottomModalSheet"
    }
}