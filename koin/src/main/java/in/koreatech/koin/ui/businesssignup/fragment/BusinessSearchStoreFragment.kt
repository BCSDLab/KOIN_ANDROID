package `in`.koreatech.koin.ui.businesssignup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.fragment.BaseFragment
import `in`.koreatech.koin.databinding.FragmentBusinessSearchStoreBinding
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessSignUpBaseViewModel
import `in`.koreatech.koin.ui.store.adapter.StoreRecyclerAdapter
import `in`.koreatech.koin.ui.store.fragment.StoreBottomSheetFragment
import `in`.koreatech.koin.ui.store.viewmodel.StoreViewModel
import `in`.koreatech.koin.util.ext.dpToPx
import `in`.koreatech.koin.util.ext.hideSoftKeyboard
import `in`.koreatech.koin.util.ext.showSoftKeyboard
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BusinessSearchStoreFragment: BaseFragment() {
    private var _binding: FragmentBusinessSearchStoreBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<StoreViewModel>()
    private val businessSignupBaseViewModel by activityViewModels<BusinessSignUpBaseViewModel>()

    private val storeAdapter = StoreRecyclerAdapter()

    private var isSearchMode: Boolean = false
        set(value) {
            if (value) activity?.showSoftKeyboard()
            else activity?.hideSoftKeyboard()
            field = value
        }

    private var showRemoveQueryButton : Boolean = false
        set(value) {
            if (!value) {
                binding.searchStoreButton.background = ContextCompat.getDrawable(
                    this.requireContext(),
                    R.drawable.ic_search
                )
                binding.searchStoreButton.layoutParams.apply {
                    width = this@BusinessSearchStoreFragment.requireActivity().dpToPx(24)
                    height = this@BusinessSearchStoreFragment.requireActivity().dpToPx(24)
                }
            } else {
                binding.searchStoreButton.background = ContextCompat.getDrawable(
                    this.requireContext(),
                    R.drawable.ic_search_close
                )
                binding.searchStoreButton.layoutParams.apply {
                    width = this@BusinessSearchStoreFragment.requireActivity().dpToPx(16)
                    height = this@BusinessSearchStoreFragment.requireActivity().dpToPx(16)
                }
            }
            field = value
        }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusinessSearchStoreBinding.inflate(inflater, container, false)
        val view = binding.root

        businessSignupBaseViewModel.setFragmentTag("searchSToreFragment")

        binding.storeNameEditTextView.addTextChangedListener {
            viewModel.updateSearchQuery(it.toString())
            showRemoveQueryButton = isSearchMode && !it.isNullOrEmpty()
        }

        binding.storeNameEditTextView.setOnFocusChangeListener { _, hasFocus ->
            isSearchMode = hasFocus
        }

        binding.searchStoreRecyclerview.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = storeAdapter
        }

        storeAdapter.setOnItemClickListener {
            viewModel.clickStoreItem(it)
            val bottomSheetView = StoreBottomSheetFragment()
            bottomSheetView.show(this.requireActivity().supportFragmentManager, bottomSheetView.tag)
        }

        binding.searchStoreButton.setOnClickListener {
            if(showRemoveQueryButton) binding.storeNameEditTextView.setText("")
        }

        initViewModel()

        return view
    }

    private fun initViewModel() = with(viewModel) {
        withLoading(this@BusinessSearchStoreFragment, this)
        refreshStores()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                stores.collect {
                    storeAdapter.submitList(it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                needToProceedStoreInfo.collectLatest {
                    businessSignupBaseViewModel.setShopId(it.shopId)

                    if(it.checkState) {
                        setFragmentResult("requestKey", bundleOf("storeName" to it.shopName))
                        businessSignupBaseViewModel.setFragmentTag("certificationFragment")
                    }
                }
            }
        }
    }
}