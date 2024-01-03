package `in`.koreatech.koin.ui.businesssignup

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityBusinessSearchStoreBinding
import `in`.koreatech.koin.ui.store.adapter.StoreRecyclerAdapter
import `in`.koreatech.koin.ui.store.fragment.StoreBottomSheetFragment
import `in`.koreatech.koin.ui.store.viewmodel.StoreViewModel
import `in`.koreatech.koin.util.ext.dpToPx
import `in`.koreatech.koin.util.ext.hideSoftKeyboard
import `in`.koreatech.koin.util.ext.showSoftKeyboard
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BusinessSearchStoreActivity : AppCompatActivity() {
    private val binding by dataBinding<ActivityBusinessSearchStoreBinding>()
    private val viewModel by viewModels<StoreViewModel>()

    private val storeAdapter = StoreRecyclerAdapter()


    private var isSearchMode: Boolean = false
        set(value) {
            if (value) showSoftKeyboard()
            else hideSoftKeyboard()
            field = value
        }

    private var showRemoveQueryButton : Boolean = false
        set(value) {
            if (!value) {
                binding.searchStoreButton.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_search
                )
                binding.searchStoreButton.layoutParams.apply {
                    width = dpToPx(24)
                    height = dpToPx(24)
                }
            } else {
                binding.searchStoreButton.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_search_close
                )
                binding.searchStoreButton.layoutParams.apply {
                    width = dpToPx(16)
                    height = dpToPx(16)
                }
            }
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_search_store)

        binding.storeNameEditTextView.addTextChangedListener {
            viewModel.updateSearchQuery(it.toString())
            showRemoveQueryButton = isSearchMode && !it.isNullOrEmpty()
        }

        binding.storeNameEditTextView.setOnFocusChangeListener { _, hasFocus ->
            isSearchMode = hasFocus
        }

        binding.searchStoreRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@BusinessSearchStoreActivity)
            adapter = storeAdapter
        }

        storeAdapter.setOnItemClickListener {
            viewModel.clickStoreItem(it)
            val bottomSheetView = StoreBottomSheetFragment()
            bottomSheetView.show(supportFragmentManager, bottomSheetView.tag)
        }

        binding.searchStoreButton.setOnClickListener {
            if(showRemoveQueryButton) binding.storeNameEditTextView.setText("")
        }

        initViewModel()
    }

    override fun onBackPressed() {
        if (binding.storeNameEditTextView.hasFocus()) {
            binding.storeNameEditTextView.clearFocus()
            return
        }
        super.onBackPressed()
    }

    private fun initViewModel() {
        viewModel.refreshStores()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stores.collect {
                    storeAdapter.submitList(it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.clickButtonState.collectLatest {
                    val check = it.first
                    val storeName = it.second
                    if(check) {
                        val intent = Intent(
                            this@BusinessSearchStoreActivity,
                            BusinessCertificationActivity::class.java
                        )

                        intent.putExtra("storeName", storeName)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }
    }
}