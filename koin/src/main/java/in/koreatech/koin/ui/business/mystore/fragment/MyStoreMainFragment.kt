package `in`.koreatech.koin.ui.business.mystore.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.MystoreMainFragmentBinding
import `in`.koreatech.koin.domain.model.business.mystore.MyStore
import `in`.koreatech.koin.ui.business.mystore.viewmodel.MyStoreViewModel

class MyStoreMainFragment: Fragment(R.layout.mystore_main_fragment) {
    private val binding by dataBinding<MystoreMainFragmentBinding>()
    private val viewModel by activityViewModels<MyStoreViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myStoreEditContent.setOnClickListener {
            findNavController().navigate(R.id.action_mystore_main_fragment_to_mystore_edit_fragment)
        }

        with(binding){
            myStoreDetailTitleTextview.text = viewModel.stores.value[0].name
            myStoreDetailPhoneTextview.text = viewModel.stores.value[0].phoneNumber
            myStoreDetailTimeTextview.text = "${viewModel.stores.value[0].openTime} ~ ${viewModel.stores.value[0].closeTime}"
            myStoreDetailHolidayTextview.text = "매주 " + viewModel.stores.value[0].dayOfHoliday
            myStoreDetailAddressTextview.text = viewModel.stores.value[0].address
            myStoreDetailDeliverTextview.text = viewModel.stores.value[0].deliveryPrice.toString() + "원"
            myStoreDetailEtcTextview.text = viewModel.stores.value[0].description
        }
    }
}