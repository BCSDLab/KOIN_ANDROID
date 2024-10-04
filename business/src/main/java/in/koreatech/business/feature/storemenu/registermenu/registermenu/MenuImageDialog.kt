package `in`.koreatech.business.feature.storemenu.registermenu.registermenu

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import `in`.koreatech.business.R
import kotlinx.coroutines.launch

@Composable
fun CustomAlertDialog(
    onClickCancel: () -> Unit = {},
    onClickConfirm: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = { onClickCancel() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Card(
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp), // Card의 모든 꼭지점에 8.dp의 둥근 모서리 적용
        )
        {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight()
                    .background(
                        color = Color.White,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_x),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 16.dp)
                    )
                }

                Text(
                    text = stringResource(id = R.string.menu_image_add),
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(id = R.string.menu_image_can_input),
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                )

                Image(
                    modifier = Modifier.padding(top = 32.dp),
                    painter = painterResource(R.drawable.ic_gallery_picture),
                    contentDescription = ""
                )

                Image(
                    modifier = Modifier.padding(top = 16.dp, bottom = 48.dp),
                    painter = painterResource(R.drawable.ic_camera_picture),
                    contentDescription = ""
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetDialog(
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
) {
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd =20.dp),
        sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(color = Color.White,),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Box(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_x),
                            contentDescription = "",
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 16.dp)
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.menu_image_add),
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(id = R.string.menu_image_can_input),
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Image(
                        modifier = Modifier
                            .padding(top = 32.dp)
                            .clickable {
                                Log.e("로그 클릭", "갤러리 클릭")
                            }
                        ,
                        painter = painterResource(R.drawable.ic_gallery_picture),
                        contentDescription = ""
                    )

                    Image(
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 48.dp)
                            .clickable {
                                Log.e("로그 클릭", "사진 클릭")
                            },
                        painter = painterResource(R.drawable.ic_camera_picture),
                        contentDescription = ""
                    )
                }
        }
    )
    {
        Button(
            onClick = {
                coroutineScope.launch {
                    if (sheetState.isVisible) {
                        sheetState.hide()
                    } else {
                        sheetState.show()
                    }
                }
            }
        ) {
            Text("Toggle Bottom Sheet")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyApp() {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "This is a Bottom Sheet")
            }
        },
        sheetPeekHeight = 0.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                scope.launch {
                    if (scaffoldState.bottomSheetState.isCollapsed) {
                        scaffoldState.bottomSheetState.expand()
                    } else {
                        scaffoldState.bottomSheetState.collapse()
                    }
                }
            }) {
                Text(text = if (scaffoldState.bottomSheetState.isCollapsed) "Show Bottom Sheet" else "Hide Bottom Sheet")
            }
        }
    }
}

@Preview
@Composable
fun PreviewDialog(){
    CustomAlertDialog()
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PreviewBottomDialog(){
    BottomSheetDialog()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp()
}