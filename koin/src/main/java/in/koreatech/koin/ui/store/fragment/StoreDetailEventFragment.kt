package `in`.koreatech.koin.ui.store.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.FragmentStoreDetailEventBinding
import `in`.koreatech.koin.databinding.FragmentStoreDetailMenuBinding
import `in`.koreatech.koin.databinding.StoreFlyerActivityViewLayoutBinding

class StoreDetailEventFragment : Fragment() {
    private var _binding: FragmentStoreDetailEventBinding? = null
    private val binding: FragmentStoreDetailEventBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentStoreDetailEventBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViews() {

    }

    private fun initViewModel() {

    }
}