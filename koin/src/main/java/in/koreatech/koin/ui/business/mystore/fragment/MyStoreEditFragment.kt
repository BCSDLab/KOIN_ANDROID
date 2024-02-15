package `in`.koreatech.koin.ui.business.mystore.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.MystoreEditFragmentBinding
import `in`.koreatech.koin.ui.business.mystore.viewmodel.MyStoreViewModel

class MyStoreEditFragment : Fragment(R.layout.mystore_edit_fragment) {
    private val binding by dataBinding<MystoreEditFragmentBinding>()
    private val viewModel by activityViewModels<MyStoreViewModel>()

    private lateinit var galleryPermInfoDialog: GalleryPermInfoDialog
    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
        uri?.let { binding.myStoreImageView.setImageURI(uri) }
    }
    private val requestReadPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if (isGranted){
            selectImageFromGalleryResult.launch("image/*")
        }
        else{
            if (isShouldShowRequestPermissionRationale(readPermission)){
                showPermissionInfoDialog()
            }
        }
    }
    private val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myStoreEditConstTime.setOnClickListener {
            findNavController().navigate(R.id.action_mystore_edit_to_mystore_edit_const_time)
        }
    }

    private fun isPermissionGranted(perm: String): Boolean{
        return ActivityCompat.checkSelfPermission(requireContext(), perm) == PackageManager.PERMISSION_GRANTED
    }
    private fun isShouldShowRequestPermissionRationale(perm:String): Boolean{
        return shouldShowRequestPermissionRationale(perm)
    }
    private fun showPermissionInfoDialog() {
        galleryPermInfoDialog = GalleryPermInfoDialog()
        galleryPermInfoDialog.show(parentFragmentManager, "Gallery Permission Info")
    }
    private fun checkReadPermission(){
        if (isPermissionGranted(readPermission)){
            selectImageFromGalleryResult.launch("image/*")
        } else {
            requestReadPermission.launch(readPermission)
        }
    }

    private fun loadImageFromGallery(){
        binding.myStoreEditMainPhoto.setOnClickListener {
            checkReadPermission()
        }
    }
}