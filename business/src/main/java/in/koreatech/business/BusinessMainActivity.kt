package `in`.koreatech.business

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.business.navigation.KoinBusinessNavHost
import `in`.koreatech.business.navigation.MYSTORESCREEN
import `in`.koreatech.business.navigation.REGISTERMENUSCREEN
import `in`.koreatech.business.navigation.REGISTERSTORESCREEN
import `in`.koreatech.business.navigation.SIGNINSCREEN
import `in`.koreatech.business.ui.theme.KOIN_ANDROIDTheme
import `in`.koreatech.koin.domain.repository.OwnerShopRepository
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class BusinessMainActivity : ComponentActivity() {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var ownerShopRepository: OwnerShopRepository

    lateinit var destination: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkingToken()
        setContent {
            KOIN_ANDROIDTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    KoinBusinessNavHost(
                        startDestination = destination
                    )
                }
            }
        }
    }

    private fun checkingToken(){
        destination = if(userRepository.ownerTokenIsValid()){
            if(ownerShopRepository.getOwnerStoreSize()){
                REGISTERSTORESCREEN
            } else{
                MYSTORESCREEN
            }
        } else SIGNINSCREEN
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KOIN_ANDROIDTheme {
        Greeting("Android")
    }
}
