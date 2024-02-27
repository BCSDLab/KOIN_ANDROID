package `in`.koreatech.koin.ui.businesssignup.fragment

import `in`.koreatech.koin.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import `in`.koreatech.koin.databinding.FragmentAttachmentDialogBinding
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessCertificationViewModel
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels

class AttachmentDialogFragment : DialogFragment() {
    private var _binding: FragmentAttachmentDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageResultLauncher: ActivityResultLauncher<Intent>
    private val viewModel by activityViewModels<BusinessCertificationViewModel>()

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        when(isGranted) {
            true -> getImageFile()
            else -> {
                when(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    true -> permissionDialog(true)
                    else -> permissionDialog(false)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttachmentDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        
        binding.cancleButton.setOnClickListener {
            dismiss()
        }

        imageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data ?: Uri.parse("")
                val fileInfo = getFileInfo(uri)
                val fileName = fileInfo.first
                val fileSize = fileInfo.second
                val fileType = "image/" + fileName.split(".")[1]

                viewModel.addImageItem(uri.toString(), fileName)
                viewModel.continueVCertification(uri, fileSize, fileType, fileName)
                dismiss()
            }
        }

        binding.attachFileButton.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermission()
            }
            else {
                getImageFile()
            }
        }

        return view
    }

    private fun checkPermission() {
        requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun permissionDialog(isDeniedOnce: Boolean) {
        when(isDeniedOnce) {
            true -> {
                val builder = AlertDialog.Builder(activity!!.applicationContext)
                builder.setTitle(getString(R.string.dialog_permission_title))
                    .setPositiveButton(getString(R.string.allow)) { _, _ ->
                        checkPermission()
                    }
                    .setNegativeButton(getString(R.string.denied)) { dialog, _ ->
                        dialog.dismiss()
                    }
                builder.show()
            }
            else -> {
                dismiss()
            }
        }
    }

    private fun getImageFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        imageResultLauncher.launch(intent)
    }

    private fun getFileInfo(uri: Uri): Pair<String, Long> {
        var fileName = ""
        var fileSize = 0L

        if(uri.scheme.equals("content")) {
            val cursor = this.requireContext().contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if(cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    fileSize = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE))
                }
            }
        }

        if(fileName.isBlank()) fileName = uri.lastPathSegment!!
        return Pair(fileName, fileSize)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}