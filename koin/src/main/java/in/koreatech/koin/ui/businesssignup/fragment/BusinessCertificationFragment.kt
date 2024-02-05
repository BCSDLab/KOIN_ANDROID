package `in`.koreatech.koin.ui.businesssignup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.fragment.BaseFragment
import `in`.koreatech.koin.databinding.FragmentBusinessCertificationBinding
import `in`.koreatech.koin.domain.error.upload.UploadError
import `in`.koreatech.koin.ui.businesssignup.BusinessSignUpCompleteActivity
import `in`.koreatech.koin.ui.businesssignup.adapter.AttachStoreImageAdapter
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessCertificationViewModel
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessSignUpBaseViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.textString
import kotlinx.coroutines.launch

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

        businessSignupBaseViewModel.setFragmentTag("CERTIFICATION_FRAGMENT")

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
        searchStoreButton.setOnClickListener {
            val nextFragment = BusinessSearchStoreFragment()
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container_view, nextFragment).commit()
        }

        businessCertificationNextButton.setOnClickListener {
            if(allWriteCheck) startActivity(Intent(this@BusinessCertificationFragment.context, BusinessSignUpCompleteActivity::class.java))
            else SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.not_enter_all_items))
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
        observeLiveData(businessCertificationContinuationState) {
            /* 아무 일도 일어나지 않는다. */
        }
        observeLiveData(saveImageList) {
            attachStoreAdapter.updateList(it)

            viewModelScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    if(isMinOneInfo.value) {
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