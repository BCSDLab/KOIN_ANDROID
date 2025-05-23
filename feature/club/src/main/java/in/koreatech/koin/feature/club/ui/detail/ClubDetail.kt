package `in`.koreatech.koin.feature.club.ui.detail

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.feature.club.ui.detail.component.ClubDetailScreen

@Composable
fun ClubDetail(
    initialPage: Int = 0,
    viewModel: ClubDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    when {
        state.clubDetails != null -> Log.e("MYLOG", "${state.clubDetails}")
    }

    ClubDetailScreen()
}
