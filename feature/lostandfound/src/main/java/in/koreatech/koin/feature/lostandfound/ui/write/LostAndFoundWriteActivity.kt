package `in`.koreatech.koin.feature.lostandfound.ui.write

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.feature.lostandfound.R

@AndroidEntryPoint
class LostAndFoundWriteActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithLightStatusBar()

        setContent {
            KoinTheme {
                NewScreenContent(
                    onBackClick = { finish() },
                    onComplete = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewScreenContent(
    onBackClick: () -> Unit,
    onComplete: (Int) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.top_container_text),
                        style = KoinTheme.typography.medium18,
                        color = KoinTheme.colors.neutral800
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                            contentDescription = stringResource(R.string.top_container_icon),
                            tint = KoinTheme.colors.neutral800
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KoinTheme.colors.neutral0
                )
            )
        }
    ) { innerPadding ->

        LostAndFoundWriteArticle(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            onWriteComplete = onComplete
        )
    }
}
