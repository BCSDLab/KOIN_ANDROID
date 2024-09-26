package `in`.koreatech.business.feature.store.storedetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.business.ui.theme.Gray2
import `in`.koreatech.business.ui.theme.Shapes
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun CollapsedTopBar(
    modifier: Modifier = Modifier,
    isCollapsed: Boolean,
    onNavigateToModifyScreen: () -> Unit,
    state: MyStoreDetailState,
) {
    val color: Color by animateColorAsState(
        if (isCollapsed) Color.White
        else Color.Transparent
    )
    Box(
        modifier = modifier
            .background(color)
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomStart
    ) {
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = isCollapsed
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = state.storeInfo?.name ?: stringResource(R.string.shop_name))
                    IconButton(onClick = onNavigateToModifyScreen) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_setting),
                            contentDescription = stringResource(R.string.shop_management)
                        )
                    }
                }
                Divider(
                    color = ColorTextField,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
            }
        }
    }
}

@Composable
fun StoreInfoScreen(
    viewModel: MyStoreDetailViewModel,
) {
    val state = viewModel.collectAsState().value
    Column(modifier = Modifier) {

        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        )
        {
            Button(
                onClick = { viewModel.onManageMenuClicked() },
                modifier = Modifier
                    .border(1.dp, ColorPrimary, RoundedCornerShape(0.dp))
                    .width(107.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(0.dp),
                elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
            ) {
                Text(
                    text = stringResource(R.string.register_store),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 15.sp
                    )
                )
            }

            Button(
                onClick = { viewModel.onManageMenuClicked() },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .border(1.dp, ColorPrimary, RoundedCornerShape(0.dp))
                    .width(107.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(0.dp),
                elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
            ) {
                Text(
                    text = stringResource(R.string.select_store),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 15.sp
                    )
                )
            }

            Button(
                onClick = { viewModel.onRegisterMenuClicked() },
                modifier = Modifier
                    .width(107.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(0.dp),
                elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp),

                ) {
                Text(
                    text = stringResource(R.string.register_menu),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 15.sp
                    )
                )
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth(),
            color = ColorPrimary
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray2),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.height(255.dp),
                painter = if(state.storeInfo?.imageUrls?.getOrNull(0) == null) painterResource(id = R.drawable.no_image) else rememberAsyncImagePainter(state.storeInfo?.imageUrls?.getOrNull(0)),
                contentDescription = stringResource(R.string.shop_image),
                contentScale = ContentScale.Crop,
            )
        }
        Button(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .background(Color.White)
                .fillMaxWidth()
                .height(40.dp),
            onClick = viewModel::navigateToModifyScreen,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = ColorPrimary,
            ),
            shape = Shapes.medium,
            border = BorderStroke(1.dp, ColorPrimary)

        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.shop_management), color = ColorPrimary, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(1.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_setting),
                    contentDescription = stringResource(R.string.shop_management),
                )
            }
        }
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = state.storeInfo?.name ?: stringResource(R.string.shop_name),
            style = TextStyle(color = Color.Black, fontSize = 20.sp),
            fontSize = 20.sp,
            fontWeight = FontWeight(600),
        )
    }
}
