package `in`.koreatech.koin.ui.dining

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.FragmentDiningItemsBinding
import `in`.koreatech.koin.domain.util.ext.arrange
import `in`.koreatech.koin.ui.dining.adapter.DiningAdapter
import `in`.koreatech.koin.ui.dining.viewmodel.DiningViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiningItemsFragment : Fragment(R.layout.fragment_dining_items) {
    private val binding by dataBinding<FragmentDiningItemsBinding>()
    private val viewModel by activityViewModels<DiningViewModel>()
    private val type by lazy { arguments?.getString(TYPE) }
    private val diningAdapter by lazy { DiningAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewDiningType.apply {
            adapter = diningAdapter
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dining.collect {
                    val diningList = it.filter { dining -> dining.type == type }.arrange()
                    diningAdapter.submitList( diningList.filter { dining -> dining.menu.isNotEmpty() } )
                }
            }
        }
    }

    companion object {
        private const val TYPE = "type"
        fun newInstance(type: String) = DiningItemsFragment().apply {
            arguments = Bundle().apply {
                putString(TYPE, type)
            }
        }
    }
}