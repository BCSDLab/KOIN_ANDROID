package `in`.koreatech.business.feature.store.storedetail.event

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailState
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailViewModel
import `in`.koreatech.business.feature.store.OwnerStoreDialog

@Composable
fun EventScreen(
    verticalOffset: Boolean,
    currentPage: Int,
    viewModel: MyStoreDetailViewModel,
    state: MyStoreDetailState,
    onDeleteEvent: () -> Unit
) {
    val scrollState = rememberScrollState()
    val enabledScroll by remember(
        verticalOffset, scrollState.value
    ) { derivedStateOf { verticalOffset || scrollState.value != 0 } }

    LaunchedEffect(scrollState.value) {
        if (scrollState.value != 0 && currentPage != 1) {
            scrollState.scrollTo(0)
        }
    }

    LaunchedEffect(state.storeEvent) {
        state.isEventExpanded.forEachIndexed { index, item ->
            viewModel.initEventItem()
        }
    }
    if (state.isEditMode) {
        EventEditToolbar(viewModel, state)
    } else {
        EventToolbar()
    }
    EventItem(enabledScroll, scrollState, viewModel, state)
    OwnerStoreDialog(
        onDismissRequest = { viewModel.changeDialogVisibility() },
        onConfirmation = { onDeleteEvent() },
        dialogTitle = stringResource(R.string.event_delete_title),
        dialogText = stringResource(R.string.event_delete_text),
        positiveButtonText = stringResource(id = R.string.delete),
        visibility = state.dialogVisibility
    )

}