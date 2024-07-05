package `in`.koreatech.business.feature.store

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.business.ui.theme.Gray2
import `in`.koreatech.business.ui.theme.Shapes
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun CollapsedTopBar(
    modifier: Modifier = Modifier, isCollapsed: Boolean
) {
    val viewModel: MyStoreDetailViewModel = hiltViewModel()
    val state = viewModel.collectAsState().value
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
                    IconButton(onClick = { /*TODO*/ }) {
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
fun TopBar() {
    val viewModel: MyStoreDetailViewModel = hiltViewModel()
    val state = viewModel.collectAsState().value
    Column(modifier = Modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray2),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.height(255.dp),
                painter = state.storeInfo?.imageUrls?.getOrNull(0)
                    .let { painterResource(id = R.drawable.no_image) },
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
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = ColorPrimary,
            ),
            shape = Shapes.medium,
            border = BorderStroke(1.dp, ColorPrimary)

        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_setting),
                    contentDescription = stringResource(R.string.shop_management),
                )
                Text(text = stringResource(R.string.shop_management))
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
