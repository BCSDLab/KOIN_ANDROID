package `in`.koreatech.koin.ui.store.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.ReviewDeleteDialogBinding
import `in`.koreatech.koin.util.ext.windowHeight
import `in`.koreatech.koin.util.ext.windowWidth

class ReviewDeleteCheckDialog(
    val onDelete: () -> Unit
) : DialogFragment() {
    private lateinit var binding: ReviewDeleteDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReviewDeleteDialogBinding.inflate(inflater, container, false)
        binding.buttonDelete.setOnClickListener {
            onDelete()
            dismiss()
        }
        binding.buttonCancle.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes

        params?.width = (windowWidth * 0.8).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }


}