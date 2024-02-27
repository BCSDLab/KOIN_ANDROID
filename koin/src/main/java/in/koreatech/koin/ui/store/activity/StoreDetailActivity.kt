package `in`.koreatech.koin.ui.store.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.StoreActivityDetailBinding
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.store.adapter.StoreDetailFlyerRecyclerAdapter
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
    private val viewModel by viewModels<StoreDetailViewModel>()
    private var flyerDialogFragment: StoreFlyerDialogFragment? = null
    private var isMenuExpanded: Boolean
        get() {
            return when {
                viewModel.storeMenu.value == null -> true
                viewModel.storeMenu.value!!.size <= MAX_MENUS_FOLDED -> true
                else -> viewModel.storeMenu.value!!.size == storeMenuAdapter.itemCount
            }
        }
        set(value) {
            viewModel.storeMenu.value?.let {
                storeMenuAdapter.submitList(
                    if (!value) it.take(MAX_MENUS_FOLDED) else it
                )
            }

            binding.menuSpreadTextView.text = getString(
                if (value) {
                    R.string.hide_menu
                } else {
                    R.string.show_more_menu
                }
            )

            binding.arrowImageView.rotation = if (value) 0F else 180F
        }

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

    private val storeMenuAdapter = StoreDetailMenuRecyclerAdapter()
    private val storeDetailFlyerRecyclerAdapter = StoreDetailFlyerRecyclerAdapter().apply {
        setOnItemClickListener { position, _ ->
            flyerDialogFragment = StoreFlyerDialogFragment()
            flyerDialogFragment?.initialPosition = position
            flyerDialogFragment?.show(supportFragmentManager, DIALOG_TAG)
        }
    }

    private val storeRecyclerAdapter = StoreRecyclerAdapter().apply {
        setOnItemClickListener {
            storeDetailActivityContract.launch(it.uid)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.storeDetailFlyerRecyclerview.apply {
            layoutManager = LinearLayoutManager(
                this@StoreDetailActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
            adapter = storeDetailFlyerRecyclerAdapter
        }

        binding.storeDetailRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@StoreDetailActivity)
            adapter = storeMenuAdapter
        }

        binding.storeRandomRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@StoreDetailActivity)
            adapter = storeRecyclerAdapter
        }

        binding.storeDetailCallButton.setOnClickListener {
            showCallDialog()
        }

        binding.menuSpreadTextView.setOnClickListener {
            isMenuExpanded = !isMenuExpanded
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
                    storeDetailIsDeliveryTextview.isVisible = false
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

                //카드결제
                if (!it.isCardOk) {
                    storeDetailIsCardTextview.isVisible = false
                }

                //계좌이체
                if (!it.isBankOk) {
                    storeDetailIsBankTextview.isVisible = false
                }

                storeDetailFlyerRecyclerAdapter.submitList(it.imageUrls)
            }
        }

        observeLiveData(viewModel.storeMenu) {
            storeMenuAdapter.submitList(it)
            (it.size <= MAX_MENUS_FOLDED).let {
                binding.menuSpreadTextView.isVisible = !it
                binding.arrowImageView.isVisible = !it
            }
            isMenuExpanded = false
        }

        observeLiveData(viewModel.recommendStores) {
            if (it != null) {
                storeRecyclerAdapter.submitList(it)
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

    companion object {
        private const val DIALOG_TAG = "flyer_dialog"
        private const val MAX_MENUS_FOLDED = 6
    }
}