package `in`.koreatech.business.feature.insertstore.insertdetailinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.domain.model.owner.insertstore.StoreBasicInfo
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun InsertDetailInfoScreenImpl(
    modifier: Modifier = Modifier,
    onBackPress: () -> Unit,
    stroeBasicInfo: StoreBasicInfo = StoreBasicInfo(),
    viewModel: InsertDetailInfoScreenViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value
    InsertDetailInfoScreen(
        storeNumber = state.storeAddress
    )
}


@Composable
fun InsertDetailInfoScreen(
    modifier: Modifier = Modifier,
    storeNumber: String = ""
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = storeNumber
        )
    }
}

@Preview
@Composable
fun PreviewInsertDetailInfoScreen(){
    InsertDetailInfoScreen()
}