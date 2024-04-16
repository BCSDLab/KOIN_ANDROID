package `in`.koreatech.koin.ui.store.activity

import android.Manifest
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.StoreActivityDetailBinding
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.store.adapter.StoreDetailMenuRecyclerAdapter
import `in`.koreatech.koin.ui.store.adapter.StoreRecyclerAdapter
import `in`.koreatech.koin.ui.store.contract.StoreCallContract
import `in`.koreatech.koin.ui.store.contract.StoreDetailActivityContract
import `in`.koreatech.koin.ui.store.fragment.StoreFlyerDialogFragment
import `in`.koreatech.koin.ui.store.viewmodel.StoreDetailViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.withLoading

class StoreDetailActivity : KoinNavigationDrawerActivity() {
    override val menuState = MenuState.Store

    private val binding by dataBinding<StoreActivityDetailBinding>(R.layout.store_activity_detail)
    override val screenTitle = "상점 상세"
    private val viewModel by viewModels<StoreDetailViewModel>()
    private var flyerDialogFragment: StoreFlyerDialogFragment? = null


    private val storeDetailActivityContract =
        registerForActivityResult(StoreDetailActivityContract()) {
        }

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
    private val storeMenuAdapter: MutableList<StoreDetailMenuRecyclerAdapter> =
        List(4) { StoreDetailMenuRecyclerAdapter() }.toMutableList()

    private val storeRecyclerAdapter = StoreRecyclerAdapter().apply {
        setOnItemClickListener {
            storeDetailActivityContract.launch(it.uid)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        repeat(4) {
            storeMenuAdapter.add(StoreDetailMenuRecyclerAdapter())
        }

        binding.storeDetailRecommendRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@StoreDetailActivity)
            adapter = storeMenuAdapter[0]
        }
        binding.storeDetailMainRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@StoreDetailActivity)
            adapter = storeMenuAdapter[1]
        }
        binding.storeDetailSetRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@StoreDetailActivity)
            adapter = storeMenuAdapter[2]
        }
        binding.storeDetailSideRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@StoreDetailActivity)
            adapter = storeMenuAdapter[3]
        }

        binding.storeRandomRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@StoreDetailActivity)
            adapter = storeRecyclerAdapter
        }

        binding.storeDetailCallButton.setOnClickListener {
            showCallDialog()
        }

        viewModel.categories.value?.menuCategories?.forEachIndexed { index, category ->

            viewModel.storeMenu.value?.let {
                storeMenuAdapter[index].submitList(it)
            }
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
                setEtcInfo(storeDetailIsCardTextview, it.isCardOk)
                //카드결제
                setEtcInfo(storeDetailIsCardTextview, it.isCardOk)
                //계좌이체
                setEtcInfo(storeDetailIsBankTextview, it.isBankOk)

                Glide.with(this@StoreDetailActivity)
                    .load(it.imageUrls?.getOrNull(0) ?: R.drawable.defualt_image)
                    .error(R.drawable.arrow_back)
                    .into(storeDetailImageview)

            }
        }

        observeLiveData(viewModel.storeMenu) {
            viewModel.categories.value?.menuCategories?.forEachIndexed { index, category ->
                viewModel.storeMenu.value?.let {
                    storeMenuAdapter[index].submitList(
                        it
                    )
                }
            }
        }
        observeLiveData(viewModel.recommendStores) {
            if (it != null) {
                storeRecyclerAdapter.submitList(it)
            }
        }
        observeLiveData(viewModel.categories) {
            if (it.menuCategories != null) {
                viewModel.categories.value?.menuCategories?.forEachIndexed { index, category ->
                    storeMenuAdapter[index].setCategory(category.name)
                }
            }
        }
    }

    override fun onDestroy() {
        flyerDialogFragment?.dismiss()
        flyerDialogFragment = null
        super.onDestroy()
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