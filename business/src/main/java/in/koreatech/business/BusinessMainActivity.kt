package `in`.koreatech.business

import android.os.Bundle
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
import `in`.koreatech.business.feature.store.MyStoreDetailScreen
import `in`.koreatech.business.feature.storemenu.registermenu.navigator.RegisterMenuNavigator
import `in`.koreatech.business.ui.theme.KOIN_ANDROIDTheme

@AndroidEntryPoint
class BusinessMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KOIN_ANDROIDTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //MyStoreDetailScreen(modifier = Modifier.fillMaxSize(),)
                    RegisterMenuNavigator()
                }
            }
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
