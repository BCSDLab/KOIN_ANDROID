package `in`.koreatech.koin.ui.business.registerstore.fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.RegisterStoreBasicInfoBinding
import `in`.koreatech.koin.domain.model.business.mystore.RegisterStoreBasic
import `in`.koreatech.koin.ui.business.registerstore.viewmodel.RegisterStoreViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.textString
import android.Manifest
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.internal.processedrootsentinel.codegen._in_koreatech_koin_KoinApplication

class RegisterStoreBasicInfoFragment: Fragment(R.layout.register_store_basic_info){
    private val binding by dataBinding<RegisterStoreBasicInfoBinding>()
    private val viewModel by activityViewModels<RegisterStoreViewModel>()
    private lateinit var basicInfo: RegisterStoreBasic
    private var imageUri: String? = null

    private val selectImageFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
        uri?.let {
            binding.imageRegister.setImageURI(uri)
            imageUri = uri.toString()
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
                basicInfo = RegisterStoreBasic(binding.initStoreName.textString,binding.initStoreAddress.textString, imageUri)
                viewModel.setBasicInfo(basicInfo)
                findNavController().navigate(R.id.register_basic_info_fragment_to_register_detail_info_fragment)
            }
        }
    }
}