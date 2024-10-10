package `in`.koreatech.koin.ui.store.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventExtra
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
import kotlin.properties.Delegates

class StoreDetailActivity : KoinNavigationDrawerActivity() {
    override val menuState = MenuState.Store
    private var storeCurrentTime by Delegates.notNull<Long>()
    private var storeElapsedTime by Delegates.notNull<Long>()
    private var dialogCurrentTime by Delegates.notNull<Long>()
    private var dialogElapsedTime by Delegates.notNull<Long>()
    private var reviewCurrentTime by Delegates.notNull<Long>()
    private var reviewElapsedTime by Delegates.notNull<Long>()
    private var isSwipeGesture = false
    private val binding by dataBinding<StoreActivityDetailBinding>(R.layout.store_activity_detail)
    override val screenTitle = "상점 상세"
    private val viewModel by viewModels<StoreDetailViewModel>()
    private var flyerDialogFragment: StoreFlyerDialogFragment? = null
    private var currentTab = 0
    private var currentPage: String = "메뉴"
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
                EventAction.BUSINESS,
                AnalyticsConstant.Label.SHOP_PICTURE,
                viewModel.store.value?.name ?: "Unknown"
            )
        }
    }
    private val storeDetailViewpagerAdapter = StoreDetailViewpagerAdapter(this)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewModel()


        binding.koinBaseAppbar.storeDetailClickListener {
            when (it.id) {
                AppBarBase.getLeftButtonId() -> {
                    storeElapsedTime = System.currentTimeMillis() - storeCurrentTime
                    isSwipeGesture = false
                    onBackPressed()
                }

                AppBarBase.getRightButtonId() -> {
                    dialogElapsedTime = System.currentTimeMillis() - dialogCurrentTime
                    reviewElapsedTime = System.currentTimeMillis() - reviewCurrentTime

                    showCallDialog()
                    EventLogger.logClickEvent(
                        EventAction.BUSINESS,
                        AnalyticsConstant.Label.SHOP_CALL,
                        viewModel.store.value?.name ?: "Unknown",
                        EventExtra(AnalyticsConstant.DURATION_TIME, {dialogElapsedTime / 1000}.toString())
                    )
                    if (currentTab == 2) {// 리뷰탭에서 전화누르기까지 시간

                        EventLogger.logClickEvent(
                            EventAction.BUSINESS,
                            AnalyticsConstant.Label.SHOP_DETAIL_VIEW_REVIEW_BACK,
                            viewModel.store.value?.name ?: "Unknown",
                            EventExtra(AnalyticsConstant.PREVIOUS_PAGE, "리뷰"),
                            EventExtra(AnalyticsConstant.CURRENT_PAGE, currentPage),
                            EventExtra(AnalyticsConstant.DURATION_TIME, {reviewElapsedTime / 1000}.toString())
                        )
                    }
                }
            }
        }
        binding.storeDetailViewPager.adapter = storeDetailViewpagerAdapter

        binding.scrollUpButton.setOnClickListener {
            viewModel.scrollUp()
        }

        binding.storeDetailViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.settingFragmentIndex(position)
            }
        })

        val tabLayoutMediator = TabLayoutMediator(
            binding.storeDetailTabLayout,
            binding.storeDetailViewPager
        ) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.menu)
                1 -> getString(R.string.event_notification)
                2 -> getString(R.string.review)
                else -> throw IllegalArgumentException("Invalid position")
            }
        }

        tabLayoutMediator.attach()

        binding.storeDetailTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        currentPage = "메뉴"
                        EventLogger.logClickEvent(
                            EventAction.BUSINESS,
                            AnalyticsConstant.Label.SHOP_DETAIL_VIEW,
                            viewModel.store.value?.name ?: "Unknown"
                        )
                        if (currentTab == 2) {
                            reviewElapsedTime = System.currentTimeMillis() - reviewCurrentTime
                            EventLogger.logClickEvent(
                                EventAction.BUSINESS,
                                AnalyticsConstant.Label.SHOP_DETAIL_VIEW_REVIEW_BACK,
                                viewModel.store.value?.name ?: "Unknown",
                                EventExtra(AnalyticsConstant.PREVIOUS_PAGE, "리뷰"),
                                EventExtra(AnalyticsConstant.CURRENT_PAGE, currentPage),
                                EventExtra(AnalyticsConstant.DURATION_TIME, {reviewElapsedTime / 1000}.toString())
                            )
                        }
                    }

                    1 -> {
                        currentPage = "이벤트/공지"
                        EventLogger.logClickEvent(
                            EventAction.BUSINESS,
                            AnalyticsConstant.Label.SHOP_DETAIL_VIEW_EVENT,
                            viewModel.store.value?.name ?: "Unknown"
                        )
                        if (currentTab == 2) {
                            reviewElapsedTime = System.currentTimeMillis() - reviewCurrentTime

                            EventLogger.logClickEvent(
                                EventAction.BUSINESS,
                                AnalyticsConstant.Label.SHOP_DETAIL_VIEW_REVIEW_BACK,
                                viewModel.store.value?.name ?: "Unknown",
                                EventExtra(AnalyticsConstant.PREVIOUS_PAGE, "리뷰"),
                                EventExtra(AnalyticsConstant.CURRENT_PAGE, currentPage),
                                EventExtra(AnalyticsConstant.DURATION_TIME, {reviewElapsedTime / 1000}.toString())
                            )
                        }
                    }

                    2 -> {
                        reviewCurrentTime = System.currentTimeMillis()
                        EventLogger.logClickEvent(
                            EventAction.BUSINESS,
                            AnalyticsConstant.Label.SHOP_DETAIL_VIEW_REVIEW,
                            viewModel.store.value?.name ?: "Unknown"
                        )

                    }

                }
                currentTab = tab?.position ?: 0

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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_CANCEL) {
            isSwipeGesture = true
        }
        return super.onTouchEvent(event)
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

        observeLiveData(viewModel.storeReview) {
            binding.storeDetailTabLayout.getTabAt(2)?.text =
                getString(R.string.review, it.totalCount.toString())
        }

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

                if (it.bank == null) {
                    storeDetailAccountTextview.isVisible = false
                    storeDetailConstAccountTextview.isVisible = false
                    storeDetailAccountCopyButton.isVisible = false
                } else {
                    storeDetailAccountTextview.text = it.bank + " " + it.accountNumber
                }

                setEtcInfo(storeDetailIsCardTextview, it.isCardOk)
                //카드결제
                setEtcInfo(storeDetailIsCardTextview, it.isCardOk)
                //계좌이체
                setEtcInfo(storeDetailIsBankTextview, it.isBankOk)

                updateInfoTv.text =
                    getString(R.string.store_update_at, it.updateAt.replace("-", "."))

                binding.storeDetailImageview.apply {
                    adapter = StoreDetailImageViewpagerAdapter(it.imageUrls) {
                        EventLogger.logClickEvent(
                            EventAction.BUSINESS,
                            AnalyticsConstant.Label.SHOP_PICTURE,
                            viewModel.store.value?.name ?: "Unknown"
                        )
                    }
                }
            }
        }

    }

    override fun onRestart() {
        val storeId = intent.extras?.getInt(StoreDetailActivityContract.STORE_ID)
        if (storeId != null) {
            viewModel.getShopReviews(storeId)
        }
        super.onRestart()
    }

    override fun onDestroy() {
        val category = intent.extras?.getString(StoreDetailActivityContract.CATEGORY)
        storeElapsedTime = System.currentTimeMillis() - storeCurrentTime
        currentPage = "카테고리($category)"
        if ( isSwipeGesture) {
            EventLogger.logSwipeEvent(
                EventAction.BUSINESS,
                AnalyticsConstant.Label.SHOP_DETAIL_VIEW_BACK,
                viewModel.store.value?.name ?: "Unknown" ,
                EventExtra(AnalyticsConstant.CURRENT_PAGE, category ?: "Unknown"),
                EventExtra(AnalyticsConstant.DURATION_TIME, {storeElapsedTime / 1000}.toString()),
            )

        }
        else{
            EventLogger.logSwipeEvent(
                EventAction.BUSINESS,
                AnalyticsConstant.Label.SHOP_DETAIL_VIEW_BACK,
                viewModel.store.value?.name ?: "Unknown" ,
                EventExtra(AnalyticsConstant.CURRENT_PAGE, category ?: "Unknown"),
                EventExtra(AnalyticsConstant.DURATION_TIME, {storeElapsedTime / 1000}.toString()),
            )
        }

        if(currentTab == 2){
            reviewElapsedTime = System.currentTimeMillis() - reviewCurrentTime
            EventLogger.logClickEvent(
                EventAction.BUSINESS,
                AnalyticsConstant.Label.SHOP_DETAIL_VIEW_REVIEW_BACK,
                viewModel.store.value?.name ?: "Unknown",
                EventExtra(AnalyticsConstant.PREVIOUS_PAGE, "리뷰"),
                EventExtra(AnalyticsConstant.CURRENT_PAGE, currentPage),
                EventExtra(AnalyticsConstant.DURATION_TIME, {reviewElapsedTime / 1000}.toString())
            )

        }
        flyerDialogFragment?.dismiss()
        flyerDialogFragment = null
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        dialogCurrentTime = System.currentTimeMillis()
        storeCurrentTime = System.currentTimeMillis()
        reviewCurrentTime = System.currentTimeMillis()
        lifecycleScope.launch {
            observeLiveData(viewModel.store) {
                viewModel.getShopReviews(it.uid)
            }
        }
        if (viewModel.store.value != null) viewModel.getShopReviews(viewModel.store.value!!.uid)
    }


    private fun showCallDialog() {
        if (viewModel.store.value?.phone != null) {
            currentPage = "전화"
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
            builder.setNegativeButton(getString(R.string.store_dialog_call_cancel)) { dialog, _ ->
                dialog.dismiss()

            }

            builder.setOnDismissListener {
                dialogCurrentTime = System.currentTimeMillis()
                reviewCurrentTime = System.currentTimeMillis()
            }

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
        const val ELAPSED_TIME = "elapsedTime"
        const val STORE_NAME = "storeName"
        const val BACK_ACTION = "back_action"
    }
}