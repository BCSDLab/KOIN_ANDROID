package `in`.koreatech.koin.ui.businesssignup

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityBusinessCertificationBinding
import `in`.koreatech.koin.domain.error.upload.BoundOfSizeException
import `in`.koreatech.koin.domain.error.upload.NotAllowedDomainException
import `in`.koreatech.koin.domain.error.upload.NotExistDomainException
import `in`.koreatech.koin.domain.error.upload.NotValidFileException
import `in`.koreatech.koin.ui.businesssignup.adapter.AttachStoreImageAdapter
import `in`.koreatech.koin.ui.businesssignup.fragment.AttachmentDialogFragment
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessCertificationViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.util.ext.textString
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BusinessCertificationActivity : AppCompatActivity() {
    private val binding by dataBinding<ActivityBusinessCertificationBinding>()
    private var allWriteCheck = false
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val attachStoreAdapter = AttachStoreImageAdapter()
    private val businessCertificationViewModel by viewModels<BusinessCertificationViewModel>()
    private var isMinOneStoreImage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_certification)

        initView()
        initViewModel()
        checkAllWrite()
    }

    private fun initView() = with(binding) {
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                val storeName = result.data?.getStringExtra("storeName")?: ""
                editStoreNameText.setText(storeName)
            }
        }
        searchStoreButton.setOnClickListener {
            val intent = Intent(this@BusinessCertificationActivity, BusinessSearchStoreActivity::class.java)
            resultLauncher.launch(intent)
        }

        businessCertificationNextButton.setOnClickListener {
            if(allWriteCheck) startActivity(Intent(this@BusinessCertificationActivity, BusinessSignUpCompleteActivity::class.java))
            else Toast.makeText(this@BusinessCertificationActivity, getString(R.string.not_enter_all_items), Toast.LENGTH_SHORT).show()
        }

        transparentButton.setOnClickListener {
            showDialog()
        }

        signupBackButton.setOnClickListener {
            finish()
        }

        attachFileRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@BusinessCertificationActivity)
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
        dialog.show(supportFragmentManager, dialog.tag)
    }

    private fun initViewModel() = with(businessCertificationViewModel) {
        observeLiveData(businessCertificationContinuationError) { state ->
            when(state) {
                NotExistDomainException() -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.not_exist_domain)
                    )
                }
                BoundOfSizeException() -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.bound_of_size_domain)
                    )
                }
                NotAllowedDomainException() -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.not_allow_type_domain)
                    )
                }
                NotValidFileException() -> {
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
        attachFileText.visibility = View.GONE
        attachFileImageView.visibility = View.GONE
        attachFileInfoText.visibility = View.GONE
    }

    private fun hiddenRecyclerView() = with(binding) {
        attachFileText.visibility = View.VISIBLE
        attachFileImageView.visibility = View.VISIBLE
        attachFileInfoText.visibility = View.VISIBLE
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
        Log.d("CheckWrite", allWriteCheck.toString())
        if(allWriteCheck && isMinOneStoreImage) {
            businessCertificationNextButton.setBackgroundColor(getColor(R.color.colorPrimary))
        }
        else {
            businessCertificationNextButton.setBackgroundColor(getColor(R.color.gray5))
        }
    }
}