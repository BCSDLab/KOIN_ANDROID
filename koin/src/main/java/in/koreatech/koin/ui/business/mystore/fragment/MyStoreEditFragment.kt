package `in`.koreatech.koin.ui.business.mystore.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import `in`.koreatech.koin.constant.MyStoreEnum
import `in`.koreatech.koin.databinding.MystoreEditFragmentBinding
import `in`.koreatech.koin.ui.business.mystore.MyStoreActivity
import `in`.koreatech.koin.ui.business.mystore.viewmodel.MyStoreViewModel

class MyStoreEditFragment : Fragment() {
    private var _binding: MystoreEditFragmentBinding? = null
    private val binding: MystoreEditFragmentBinding
        get() = _binding!!
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return MystoreEditFragmentBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickEditConstTime()
        loadImageFromGallery()
    }

    override fun onResume() {
        super.onResume()
        onBackPressed()
    }

    private fun onClickEditConstTime() {
        binding.myStoreEditConstTime.setOnClickListener {
            viewModel.changeMyStoreState(MyStoreEnum.CONST_TIME_EDIT)
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

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val frag =
                        parentFragmentManager.findFragmentByTag(MyStoreActivity.MyStoreTAG.MY_STORE_STORE_EDIT_TAG)
                    if (frag != null) {
                        parentFragmentManager.beginTransaction()
                            .remove(frag)
                            .commit()
                    }
                }
            })
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): MyStoreEditFragment {
            val args = Bundle()

            val fragment = MyStoreEditFragment()
            fragment.arguments = args
            return fragment
        }
    }


}