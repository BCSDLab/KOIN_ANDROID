package `in`.koreatech.koin.ui.business.registerstore.fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.RegisterStoreFinishBinding
import `in`.koreatech.koin.ui.main.activity.MainActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment


class RegisterStoreFinishFragment : Fragment(R.layout.register_store_finish){
    private val binding by dataBinding<RegisterStoreFinishBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        binding.goToMainButton.setOnClickListener{
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}