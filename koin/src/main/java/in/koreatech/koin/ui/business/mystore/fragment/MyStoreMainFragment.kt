package `in`.koreatech.koin.ui.business.mystore.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.MystoreMainFragmentBinding

class MyStoreMainFragment: Fragment(R.layout.mystore_main_fragment) {
    private val binding by dataBinding<MystoreMainFragmentBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myStoreEditContent.setOnClickListener {
            findNavController().navigate(R.id.action_mystore_main_fragment_to_mystore_edit_fragment)
        }
    }
}