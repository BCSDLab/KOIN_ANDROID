package `in`.koreatech.koin.ui.main.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.data.util.localized
import `in`.koreatech.koin.databinding.FragmentDiningContainerBinding
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.ext.arrange
import `in`.koreatech.koin.domain.util.ext.typeFilter
import `in`.koreatech.koin.ui.main.viewmodel.MainActivityViewModel
import `in`.koreatech.koin.util.ext.observeLiveData

@AndroidEntryPoint
class DiningContainerFragment : Fragment(R.layout.fragment_dining_container) {
    private val binding by dataBinding<FragmentDiningContainerBinding>()
    private val viewModel by activityViewModels<MainActivityViewModel>()
    private val place by lazy { arguments?.getString(PLACE) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        addListener()
        initViewModel()
    }

    private fun initView() {
        place.apply {
//            binding.textViewDiningContainer.text = this.toString()
        }
    }

    private fun addListener() {

    }

    private fun initViewModel() = with(viewModel) {
        observeLiveData(diningData) {
            updateDining(it, selectedPosition.value ?: 0)
        }

        observeLiveData(selectedPosition) { position ->
            diningData.value?.let { list ->
                updateDining(list, position)
            }
        }

        observeLiveData(selectedType) {
            binding.textViewDiningTime.text = it.localized(requireActivity())
        }
    }

    fun updateDining(list: List<Dining>, position: Int) {
        val diningType = DiningUtil.getCurrentType()
        val diningArranged = list
            .typeFilter(diningType)
            .arrange()

        if (list.isEmpty() || position >= diningArranged.size) {
            binding.viewEmptyDining.emptyDiningListFrameLayout.isVisible = true
            return
        }
        binding.viewEmptyDining.emptyDiningListFrameLayout.isVisible = false

        listOf(
            binding.textViewDiningContainerMenu0,
            binding.textViewDiningContainerMenu2,
            binding.textViewDiningContainerMenu4,
            binding.textViewDiningContainerMenu6,
            binding.textViewDiningContainerMenu8,
            binding.textViewDiningContainerMenu1,
            binding.textViewDiningContainerMenu3,
            binding.textViewDiningContainerMenu5,
            binding.textViewDiningContainerMenu7,
            binding.textViewDiningContainerMenu9
        ).zip(diningArranged[position].menu).forEach { (textView, menu) ->
            textView.text = menu
        }

        val isSoldOut = diningArranged[position].soldoutAt.isNotEmpty()
        val isChanged = diningArranged[position].changedAt.isNotEmpty()
        with (binding.textViewDiningStatus) {
            when {
                isSoldOut -> {
                    text = context.getString(R.string.dining_soldout)
                    setTextColor(ContextCompat.getColor(context, R.color.dining_soldout_text))
                    background = ContextCompat.getDrawable(context, R.drawable.dining_soldout_fill_radius_4)
                }
                isChanged -> {
                    text = context.getString(R.string.dining_changed)
                    setTextColor(ContextCompat.getColor(context, R.color.dining_changed_text))
                    background = ContextCompat.getDrawable(context, R.drawable.dining_changed_fill_radius_4)
                }
                else -> {
                    visibility = View.INVISIBLE
                }
            }
        }
    }

    companion object {
        private const val PLACE = "place"
        fun newInstance(place: String) =
            DiningContainerFragment().apply {
                arguments = Bundle().apply {
                    putString(PLACE, place)
                }
            }
    }
}