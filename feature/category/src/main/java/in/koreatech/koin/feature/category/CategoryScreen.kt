package `in`.koreatech.koin.feature.category

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = hiltViewModel(),
    navigateToNotification: () -> Unit = {}
) {
    Text("Category Screen")
}
