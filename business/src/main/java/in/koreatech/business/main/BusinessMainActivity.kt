package `in`.koreatech.business.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import `in`.koreatech.business.navigation.REGISTERSTORESCREEN
import `in`.koreatech.business.navigation.SIGNINSCREEN
import `in`.koreatech.business.ui.theme.KOIN_ANDROIDTheme
import `in`.koreatech.koin.domain.repository.OwnerShopRepository
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

@AndroidEntryPoint
class BusinessMainActivity : ComponentActivity() {

    lateinit var destination: String

    private val viewModel by viewModels<BusinessMainActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
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
    private fun initViewModel() = with(viewModel) {
        destinationString.observe(this@BusinessMainActivity
        ) {
            destination = destinationString.value.toString()
        }
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
