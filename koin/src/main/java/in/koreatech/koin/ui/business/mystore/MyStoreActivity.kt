package `in`.koreatech.koin.ui.business.mystore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityMyStoreBinding
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState

class MyStoreActivity : KoinNavigationDrawerActivity() {
    override val menuState: MenuState
        get() = TODO("Not yet implemented")

    private val binding by dataBinding<ActivityMyStoreBinding>(R.layout.activity_my_store)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    private fun onClickEditButton(){
        binding.myStoreEditContent.setOnClickListener {

        }
    }



    override fun onDestroy() {
        super.onDestroy()
    }
}