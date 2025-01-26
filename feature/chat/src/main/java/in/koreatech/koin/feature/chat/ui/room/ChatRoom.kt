package `in`.koreatech.koin.feature.chat.ui.room

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoom(
    articleId: Int,
    viewModel: ChatRoomViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            KoinTopAppBar(
                title = "Chat Room" //TODO: Replace later
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier.padding(contentPadding)
        ) {

        }
    }
}