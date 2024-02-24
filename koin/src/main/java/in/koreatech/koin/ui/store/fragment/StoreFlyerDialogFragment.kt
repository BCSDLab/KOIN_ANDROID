package `in`.koreatech.koin.ui.store.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.StoreFlyerActivityViewLayoutBinding
import `in`.koreatech.koin.ui.store.adapter.StoreDetailFlyerFullRecyclerAdapter
import `in`.koreatech.koin.ui.store.viewmodel.StoreDetailViewModel
import `in`.koreatech.koin.util.ext.observeLiveData

class StoreFlyerDialogFragment : DialogFragment() {
    var initialPosition = 0
    private var _binding: StoreFlyerActivityViewLayoutBinding? = null
    private val binding: StoreFlyerActivityViewLayoutBinding get() = _binding!!

    private val viewModel by activityViewModels<StoreDetailViewModel>()
    private val flyerAdapter = StoreDetailFlyerFullRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return StoreFlyerActivityViewLayoutBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.storeFlyerViewPager.apply {
            adapter = flyerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateIndicatorText(position + 1, flyerAdapter.itemCount)
                }
            })
        }

        binding.storeFlyerCloseImagebutton.setOnClickListener {
            dismiss()
        }

        initViewModel()
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initViewModel() {
        observeLiveData(viewModel.store) {
            flyerAdapter.submitList(it.imageUrls)
            binding.storeFlyerViewPager.setCurrentItem(initialPosition, false)
            updateIndicatorText(initialPosition + 1, flyerAdapter.itemCount)
        }
    }

    private fun updateIndicatorText(current: Int, pages: Int) {
        binding.currentPageTextView.text = requireActivity().getString(R.string.flyer_current_page_text, current, pages)
    }
}