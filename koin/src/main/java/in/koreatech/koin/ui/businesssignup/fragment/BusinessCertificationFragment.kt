package `in`.koreatech.koin.ui.businesssignup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.fragment.BaseFragment
import `in`.koreatech.koin.databinding.FragmentBusinessCertificationBinding
import `in`.koreatech.koin.domain.error.upload.UploadError
import `in`.koreatech.koin.ui.businesssignup.adapter.AttachStoreImageAdapter
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessCertificationViewModel
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessSignUpBaseViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.textString
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BusinessCertificationFragment: BaseFragment() {
    private var _binding: FragmentBusinessCertificationBinding? = null
    private val binding get() = _binding!!

    private var allWriteCheck = false
    private val attachStoreAdapter = AttachStoreImageAdapter()
    private val businessCertificationViewModel by activityViewModels<BusinessCertificationViewModel>()
    private val businessSignupBaseViewModel by activityViewModels<BusinessSignUpBaseViewModel>()
    private var isMinOneStoreImage = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusinessCertificationBinding.inflate(inflater, container, false)
        val view = binding.root

        businessSignupBaseViewModel.setFragmentTag("certificationFragment")

        initView()
        initViewModel()
        checkAllWrite()

        return view
    }

    private fun initView() = with(binding) {
        setFragmentResultListener("requestKey") { requestKey, bundle ->
            val result = bundle.getString("storeName")
            editStoreNameText.setText(result)
        }

        editPersonalContactText.doOnTextChanged { text, start, before, count ->
            if(text?.length == 3 || text?.length == 8) {
                editPersonalContactText.setText(getString(R.string.set_form, text))
                editPersonalContactText.setSelection(editPersonalContactText.text.length)
            }
        }

        editRegistrationNumberText.doOnTextChanged { text, start, before, count ->
            if(text?.length == 3 || text?.length == 6) {
                editRegistrationNumberText.setText(getString(R.string.set_form, text))
                editRegistrationNumberText.setSelection(editRegistrationNumberText.text.length)
            }
        }

        searchStoreButton.setOnClickListener {
            val nextFragment = BusinessSearchStoreFragment()
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container_view, nextFragment).commit()
        }

        businessCertificationNextButton.setOnClickListener {
            if(allWriteCheck) {
                businessSignupBaseViewModel.setName(editBusinessmanNameText.text.toString())
                businessSignupBaseViewModel.setShopName(editStoreNameText.text.toString())
                businessSignupBaseViewModel.setPhoneNumber(editPersonalContactText.text.toString())
                businessSignupBaseViewModel.setCompanyNumber(editRegistrationNumberText.text.toString())

                businessSignupBaseViewModel.setFragmentTag("completeActivity")
            }
            else SnackbarUtil.makeShortSnackbar(root, getString(R.string.not_enter_all_items))
        }

        transparentButton.setOnClickListener {
            showDialog()
        }

        attachFileRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@BusinessCertificationFragment.context)
            adapter = attachStoreAdapter
        }
    }

    private fun checkAllWrite() {
        binding.editBusinessmanNameText.addTextChangedListener {
            allWriteCheck = isAllWrite()
            check()
        }
        binding.editStoreNameText.addTextChangedListener {
            allWriteCheck = isAllWrite()
            check()
        }
        binding.editRegistrationNumberText.addTextChangedListener {
            allWriteCheck = isAllWrite()
            check()
        }
        binding.editPersonalContactText.addTextChangedListener {
            allWriteCheck = isAllWrite()
            check()
        }
    }

    private fun showDialog() {
        val dialog = AttachmentDialogFragment()
        dialog.show(requireActivity().supportFragmentManager, dialog.tag)
    }

    private fun initViewModel() = with(businessCertificationViewModel) {
        observeLiveData(businessCertificationContinuationError) { state ->
            when(state) {
                UploadError.NotExistDomainException -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.not_exist_domain)
                    )
                }
                UploadError.BoundOfSizeException -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.bound_of_size_domain)
                    )
                }
                UploadError.NotAllowedDomainException -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.not_allow_type_domain)
                    )
                }
                UploadError.NotValidFileException -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.is_not_valid_file)
                    )
                }
            }
        }

        observeLiveData(shopImageUrlAndSize) {
            val fileUrl = "https://${it.resultUrl}/${it.fileName}"
            businessSignupBaseViewModel.setFileUrls(fileUrl)

            val inputStream = requireActivity().contentResolver.openInputStream(it.uri.toUri())

            if(inputStream == null) {
                SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.failed_file_upload))
            } else {
                uploadPreSignedUrl(
                    it.preSignedUrl,
                    inputStream,
                    it.mediaType,
                    it.fileSize
                )
            }
        }

        observeLiveData(failUploadPreSignedUrl) {
            SnackbarUtil.makeShortSnackbar(
                binding.root,
                getString(R.string.failed_file_upload)
            )
        }

        observeLiveData(saveImageList) {
            attachStoreAdapter.updateList(it)



            viewModelScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    isMinOneInfo.collectLatest { check ->
                        if(check) {
                            isMinOneStoreImage = true
                            hiddenInitView()
                            check()
                        }
                        else {
                            hiddenRecyclerView()
                            check()
                        }
                    }
                }
            }
        }
    }

    private fun hiddenInitView() = with(binding) {
        recyclerviewInitGroup.visibility = View.GONE
    }

    private fun hiddenRecyclerView() = with(binding) {
        recyclerviewInitGroup.visibility = View.VISIBLE
    }

    private fun isAllWrite(): Boolean {
        if(binding.editBusinessmanNameText.textString.trim().isBlank()) {
            check()
            return false
        }
        if(binding.editStoreNameText.textString.trim().isBlank()) {
            check()
            return false
        }
        if(binding.editRegistrationNumberText.textString.trim().isBlank()) {
            check()
            return false
        }
        if(binding.editPersonalContactText.textString.trim().isBlank()) {
            check()
            return false
        }

        return true
    }

    private fun check() = with(binding) {
        if(allWriteCheck && isMinOneStoreImage) {
            businessCertificationNextButton.setBackgroundColor(context!!.getColor(R.color.colorPrimary))
        }
        else {
            businessCertificationNextButton.setBackgroundColor(context!!.getColor(R.color.gray5))
        }
    }
}