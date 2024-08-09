package `in`.koreatech.business.feature.store.storedetail.event

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailState
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailViewModel
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.business.ui.theme.Gray4
import `in`.koreatech.business.ui.theme.Gray6


@Composable
fun EventEditToolbar(viewModel: MyStoreDetailViewModel, state: MyStoreDetailState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(Gray4),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy((-6).dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(painter = if (state.isAllEventSelected) painterResource(id = R.drawable.ic_check_selected) else painterResource(
                id = R.drawable.ic_check
            ),
                contentDescription = stringResource(R.string.check),
                modifier = Modifier.clickable { viewModel.onChangeAllEventSelected() })
            Text(text = stringResource(R.string.all), color = Gray6, fontSize = 12.sp)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = {
                    if (state.isSelectedEvent.size > 1) viewModel.modifyEventError()
                    else viewModel.navigateToModifyScreen()
                },
                modifier = Modifier
                    .width(100.dp)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorTextField, contentColor = Gray6
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = stringResource(R.string.modify)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.modify))
            }
            Button(
                onClick = viewModel::changeDialogVisibility,
                modifier = Modifier
                    .width(100.dp)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorTextField, contentColor = Gray6
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = stringResource(R.string.delete)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.delete))
            }
            Button(
                onClick = { viewModel.onChangeEditMode() },
                modifier = Modifier
                    .width(100.dp)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorTextField, contentColor = Gray6
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_complete),
                    contentDescription = stringResource(R.string.complete)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.complete))
            }
        }
    }
    Divider(
        color = Gray4, modifier = Modifier.height(1.dp)
    )
}

@Composable
fun EventToolbar() {
    val viewModel: MyStoreDetailViewModel = hiltViewModel()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Button(
            onClick = { viewModel.onChangeEditMode() },
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorTextField, contentColor = Color.Black
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = stringResource(R.string.edit)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.edit))
        }
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorTextField, contentColor = Color.Black
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_add_box),
                contentDescription = stringResource(R.string.add)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.add))
        }
    }
}

