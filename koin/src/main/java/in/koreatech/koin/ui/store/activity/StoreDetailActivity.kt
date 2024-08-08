package `in`.koreatech.koin.ui.store.activity

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.StoreActivityDetailBinding
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.store.adapter.StoreDetailFlyerRecyclerAdapter
import `in`.koreatech.koin.ui.store.adapter.StoreDetailImageViewpagerAdapter
import `in`.koreatech.koin.ui.store.adapter.StoreDetailMenuRecyclerAdapter
import `in`.koreatech.koin.ui.store.adapter.StoreDetailViewpagerAdapter
import `in`.koreatech.koin.ui.store.contract.StoreCallContract
import `in`.koreatech.koin.ui.store.contract.StoreDetailActivityContract
import `in`.koreatech.koin.ui.store.fragment.StoreFlyerDialogFragment
import `in`.koreatech.koin.ui.store.viewmodel.StoreDetailViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.launch

class StoreDetailActivity : KoinNavigationDrawerActivity() {
    override val menuState = MenuState.Store

    private val binding by dataBinding<StoreActivityDetailBinding>(R.layout.store_activity_detail)
    override val screenTitle = "상점 상세"
    private val viewModel by viewModels<StoreDetailViewModel>()
    private var flyerDialogFragment: StoreFlyerDialogFragment? = null
    
    private val callPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                viewModel.store.value?.phone?.let { phoneNumber ->
                    callContract.launch(phoneNumber)
                }
            } else {
                SnackbarUtil.makePermissionSnackBar(
                    binding.root,
                    getString(R.string.store_call_permission_denied_message)
                )
            }
        }

    private val callContract = registerForActivityResult(StoreCallContract()) {}

    private val storeMenuAdapter = StoreDetailMenuRecyclerAdapter()
    private val storeDetailFlyerRecyclerAdapter = StoreDetailFlyerRecyclerAdapter().apply {
        setOnItemClickListener { position, _ ->
            flyerDialogFragment = StoreFlyerDialogFragment()
            flyerDialogFragment?.initialPosition = position
            flyerDialogFragment?.show(supportFragmentManager, DIALOG_TAG)
            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.BUSINESS,
                AnalyticsConstant.Label.SHOP_PICTURE,
                viewModel.store.value?.name ?: "Unknown"
            )
        }
    }
    private val storeDetailViewpagerAdapter = StoreDetailViewpagerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.koinBaseAppbar.setOnClickListener {
            when (it.id) {
                AppBarBase.getLeftButtonId() -> {
                    EventLogger.logClickEvent(
                        AnalyticsConstant.Domain.BUSINESS,
                        AnalyticsConstant.Label.SHOP_BACK_BUTTON,
                        viewModel.store.value?.name ?: "Unknown"
                    )
                    onBackPressed()
                }
                AppBarBase.getRightButtonId() -> {
                    showCallDialog()
                    EventLogger.logClickEvent(
                        AnalyticsConstant.Domain.BUSINESS,
                        AnalyticsConstant.Label.SHOP_CALL,
                        viewModel.store.value?.name ?: "Unknown"
                    )
                }
            }
        }
        binding.storeDetailViewPager.adapter = storeDetailViewpagerAdapter

        binding.scrollUpButton.setOnClickListener {
            viewModel.scrollUp()
        }

        binding.storeDetailViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.settingFragmentIndex(position)
            }
        })

        TabLayoutMediator(
            binding.storeDetailTabLayout,
            binding.storeDetailViewPager
        ) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.menu)
                1 -> getString(R.string.event_notification)
                2 -> getString(R.string.review)
                else -> throw IllegalArgumentException("Invalid position")
            }
        }.attach()

        binding.storeDetailTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 1)
                    EventLogger.logClickEvent(
                        AnalyticsConstant.Domain.BUSINESS,
                        AnalyticsConstant.Label.SHOP_DETAIL_VIEW_EVENT,
                        viewModel.store.value?.name ?: "Unknown"
                    )
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabReselected(p0: TabLayout.Tab?) {}
        })

        binding.storeDetailAccountCopyButton.setOnClickListener {

            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(
                getString(R.string.account_number),
                binding.storeDetailAccountTextview.text
            )
            clipboardManager.setPrimaryClip(clipData)

            ToastUtil.getInstance().makeShort(getString(R.string.store_account_copy))
        }

        initViewModel()
        val storeId = intent.extras?.getInt(StoreDetailActivityContract.STORE_ID)
        if (storeId == null) {
            ToastUtil.getInstance()
                .makeShort(getString(R.string.store_detail_wrong_store_id_message))
            finish()
        }
        viewModel.getStoreWithMenu(storeId!!)
        viewModel.getShopMenus(storeId)
        viewModel.getShopEvents(storeId)
        viewModel.getShopReviews(storeId)
    }

    override fun onBackPressed() {
        if (flyerDialogFragment?.isVisible == true) {
            flyerDialogFragment!!.dismiss()
            flyerDialogFragment = null
            return
        }

        super.onBackPressed()
    }

    private fun initViewModel() {
        withLoading(this@StoreDetailActivity, viewModel)

        observeLiveData(viewModel.store) {
            with(binding) {
                //상점명
                storeDetailTitleTextview.text = it.name

                //전화번호
                storeDetailPhoneTextview.text = it.phone

                //운영시간
                storeDetailTimeTextview.text =
                    generateOpenCloseTimeString(it.open.openTime, it.open.closeTime)

                //주소
                if (it.address == null) {
                    storeDetailConstAddressTextview.isVisible = false
                    storeDetailAddressTextview.isVisible = false
                } else {
                    storeDetailAddressTextview.text = it.address
                }

                //배달가능여부, 배달비
                if (!it.isDeliveryOk) {
                    storeDetailConstDeliverTextview.isVisible = false
                    storeDetailDeliverTextview.isVisible = false
                    storeDetailIsDeliveryTextview.setTextColor(
                        ContextCompat.getColor(
                            this@StoreDetailActivity,
                            R.color.gray2
                        )
                    )
                    storeDetailIsDeliveryTextview.background = ContextCompat.getDrawable(
                        this@StoreDetailActivity,
                        R.drawable.button_rect_gray_radius_5dp
                    )
                } else {
                    storeDetailDeliverTextview.text = if (it.deliveryPrice <= 0) {
                        getString(R.string.store_delivery_free)
                    } else {
                        getString(R.string.store_delivery_price, it.deliveryPrice)
                    }
                }

                //기타정보
                if (it.description == null) {
                    storeDetailConstEtcTextview.isVisible = false
                    storeDetailEtcTextview.isVisible = false
                } else {
                    storeDetailEtcTextview.text = it.description
                }

                if(it.bank == null){
                    storeDetailAccountTextview.isVisible = false
                    storeDetailConstAccountTextview.isVisible = false
                    storeDetailAccountCopyButton.isVisible = false
                }
                else{
                    storeDetailAccountTextview.text = it.bank + " " + it.accountNumber
                }

                setEtcInfo(storeDetailIsCardTextview, it.isCardOk)
                //카드결제
                setEtcInfo(storeDetailIsCardTextview, it.isCardOk)
                //계좌이체
                setEtcInfo(storeDetailIsBankTextview, it.isBankOk)

                updateInfoTv.text = getString(R.string.store_update_at, it.updateAt.replace("-", "."))

                binding.storeDetailImageview.apply {
                    adapter = StoreDetailImageViewpagerAdapter(it.imageUrls)

                }

            }
        }

    }

    override fun onDestroy() {
        flyerDialogFragment?.dismiss()
        flyerDialogFragment = null
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            observeLiveData(viewModel.store) {
                viewModel.getShopReviews(it.uid)
            }
        }
        if(viewModel.store.value != null) viewModel.getShopReviews(viewModel.store.value!!.uid)
    }

    private fun showCallDialog() {
        if (viewModel.store.value?.phone != null) {
            val builder = AlertDialog.Builder(this)
            val message = StringBuilder().apply {
                append(viewModel.store.value?.name)
                appendLine()
                appendLine()
                append(viewModel.store.value?.phone)
            }
            builder.setMessage(message)

            builder.setPositiveButton(getString(R.string.store_dialog_call)) { _, _ ->
                callPermission.launch(Manifest.permission.CALL_PHONE)
            }
            builder.setNegativeButton(getString(R.string.store_dialog_call_cancel)) { dialog, _ -> dialog.dismiss() }

            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun generateOpenCloseTimeString(openTime: String, closeTime: String): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(openTime)
        stringBuilder.append(getString(R.string.store_open_close_time_mark))
        stringBuilder.append(closeTime)
        return stringBuilder.toString()
    }

    private fun setEtcInfo(textView: TextView, isAvailable: Boolean) {
        if (!isAvailable) {
            textView.setTextColor(ContextCompat.getColor(this@StoreDetailActivity, R.color.gray2))
            textView.background = ContextCompat.getDrawable(
                this@StoreDetailActivity,
                R.drawable.button_rect_gray_radius_5dp
            )
        }
    }

    companion object {
        private const val DIALOG_TAG = "flyer_dialog"
    }
}