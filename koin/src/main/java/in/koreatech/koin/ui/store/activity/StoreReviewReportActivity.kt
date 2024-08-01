package `in`.koreatech.koin.ui.store.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.databinding.ActivitySignupCompleteBinding
import `in`.koreatech.koin.databinding.StoreActivityReportReviewBinding
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.state.store.StoreReviewExceptionState
import `in`.koreatech.koin.domain.state.store.StoreReviewState
import `in`.koreatech.koin.ui.store.contract.StoreDetailActivityContract
import `in`.koreatech.koin.ui.store.viewmodel.StoreReviewReportViewModel
import `in`.koreatech.koin.ui.store.viewmodel.StoreViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoreReviewReportActivity:  ActivityBase() {

    private lateinit var binding: StoreActivityReportReviewBinding
    override val screenTitle = "리뷰 신고"
    private val viewModel by viewModels<StoreReviewReportViewModel>()

    private var storeId: Int? = null
    private var reviewId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StoreActivityReportReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding){

//        report_appbar.setOnClickListener {
//            when (it.id) {
//                AppBarBase.getLeftButtonId() -> onBackPressed()
//                AppBarBase.getRightButtonId() -> toggleNavigationDrawer()
//            }
//        }

        storeId = intent.extras?.getInt("storeId")
        reviewId = intent.extras?.getInt("reviewId")

        noRelationRadioButton.setOnClickListener {
            viewModel.reportReasonClicked(0)
        }

        spamRadioButton.setOnClickListener {
            viewModel.reportReasonClicked(1)
        }

        abuseRadioButton.setOnClickListener {
            viewModel.reportReasonClicked(2)
        }

        privateRadioButton.setOnClickListener {
            viewModel.reportReasonClicked(3)
        }
        etcRadioButton.setOnClickListener {
            viewModel.reportReasonClicked(4)
        }

        reportButton.setOnClickListener {
            viewModel.reportReviewButtonClicked(
                storeId,
                reviewId,
                inputReportReasonEdittext.text.toString()
            )
        }

    }

    private fun initViewModel() = with(binding){

        observeLiveData(viewModel.isNotRelation) {
            if (it) {
                noRelationRadioButton.setImageResource(R.drawable.ic_check_selected_24dp)
            } else {
                noRelationRadioButton.setImageResource(R.drawable.ic_check_24dp)
            }
        }

        observeLiveData(viewModel.isSpam) {
            if (it) {
                spamRadioButton.setImageResource(R.drawable.ic_check_selected_24dp)
            } else {
                spamRadioButton.setImageResource(R.drawable.ic_check_24dp)
            }
        }

        observeLiveData(viewModel.isAbuse) {
            if (it) {
                abuseRadioButton.setImageResource(R.drawable.ic_check_selected_24dp)
            } else {
                abuseRadioButton.setImageResource(R.drawable.ic_check_24dp)
            }
        }

        observeLiveData(viewModel.isPrivate) {
            if (it) {
                privateRadioButton.setImageResource(R.drawable.ic_check_selected_24dp)
            } else {
                privateRadioButton.setImageResource(R.drawable.ic_check_24dp)
            }
        }

        observeLiveData(viewModel.isEtc) {
            inputReportReasonEdittext.isEnabled = it

            if (it){
                etcRadioButton.setImageResource(R.drawable.ic_check_selected_24dp)
                inputReportReasonEdittext.background = getDrawable(R.drawable.selected_edittext_border_5dp_radius)
                inputReportReasonEdittext.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    }

                    override fun afterTextChanged(s: Editable?) {
                        characterRangeTextview.text = "${s?.length ?: 0}/150"
                        if(s?.length == 150)
                            characterRangeTextview.setTextColor(ContextCompat.getColor(this@StoreReviewReportActivity, R.color.colorAccent))
                        else
                            characterRangeTextview.setTextColor(ContextCompat.getColor(this@StoreReviewReportActivity, R.color.gray10))
                    }
                })
            } else {
                etcRadioButton.setImageResource(R.drawable.ic_check_24dp)
                inputReportReasonEdittext.text = null
                inputReportReasonEdittext.background = getDrawable(R.drawable.edittext_border_5dp_radius)
            }
        }

        lifecycleScope.launch {
            viewModel.storeReviewState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    StoreReviewState.ReportComplete -> {
                        ToastUtil.getInstance().makeShort(getString(R.string.review_report_complete))
                        finish()
                    }
                }
            }
        }

        lifecycleScope.launch {

            viewModel.storeReviewExceptionState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                when(it){
                    StoreReviewExceptionState.ToastNullCheckBox ->  ToastUtil.getInstance().makeShort(getString(R.string.review_null_check_box))
                }
            }
        }

    }


}