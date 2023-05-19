package `in`.koreatech.koin.ui.business.mystore.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import `in`.koreatech.koin.databinding.MystoreEditConstTimeFragmentBinding
import `in`.koreatech.koin.ui.business.mystore.viewmodel.MyStoreViewModel

class MyStoreEditConstTimeFragment : Fragment() {
    private var _binding: MystoreEditConstTimeFragmentBinding? = null
    private val binding: MystoreEditConstTimeFragmentBinding
        get() = _binding!!
    private val viewModel by activityViewModels<MyStoreViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return MystoreEditConstTimeFragmentBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            mondayTimeTextview.setOnClickListener {  }
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): MyStoreEditConstTimeFragment {
            val args = Bundle()

            val fragment = MyStoreEditConstTimeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}