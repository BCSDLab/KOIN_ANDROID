package `in`.koreatech.koin.ui.business.registerstore.fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.RegisterStoreBasicInfoBinding
import `in`.koreatech.koin.domain.model.business.mystore.RegisterStoreBasic
import `in`.koreatech.koin.ui.business.registerstore.viewmodel.RegisterStoreViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.textString
import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.InputStream

class RegisterStoreBasicInfoFragment: Fragment(R.layout.register_store_basic_info){
    private val binding by dataBinding<RegisterStoreBasicInfoBinding>()
    private val viewModel by activityViewModels<RegisterStoreViewModel>()
    private lateinit var basicInfo: RegisterStoreBasic
    private var imageUri: String? = null

    private val selectImageFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
        uri?.let {
            binding.imageRegister.setImageURI(uri)
            imageUri = convertUriToBase64(requireContext(), uri)
        }
    }

    private val permissionList = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    private val checkPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        result.forEach {
            if(!it.value) {
                SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.register_store_image_permission_denied_message))
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        checkPermission.launch(permissionList)

        binding.imageRegister.setOnClickListener {
            selectImageFromGallery.launch("image/*")
        }

        binding.backButtonInBasic.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.nextButton.setOnClickListener {
            if (
                binding.initStoreName.textString.isBlank() ||
                binding.initStoreAddress.textString.isBlank()
            ) {
                SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.basic_store_required_field_not_filled))
            }
            else {
                Log.d("로그", imageUri.toString())
                basicInfo = RegisterStoreBasic(binding.initStoreName.textString,binding.initStoreAddress.textString, imageUri)
                viewModel.setBasicInfo(basicInfo)
                findNavController().navigate(R.id.register_basic_info_fragment_to_register_detail_info_fragment)
            }
        }
    }

    private fun convertUriToBase64(context: Context, imageUri: Uri): String {
        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        // 비트맵을 바이트 배열로 변환
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()

        // 바이트 배열을 Base64로 인코딩하여 문자열로 변환
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}