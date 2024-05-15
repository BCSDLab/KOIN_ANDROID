package `in`.koreatech.business.feature.insertstore.selectcategory

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.domain.model.store.StoreCategories
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun SelectCategoryScreenImpl(
    modifier: Modifier = Modifier,
    navigateToFinish: () -> Unit,
    onBackPressed: () -> Unit,
    viewModel: SelectCategoryScreenViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value
    SelectCategoryScreen(
        categories = state.categories
    )
}



@Composable
fun SelectCategoryScreen(
    modifier: Modifier = Modifier,
    categories: List<StoreCategories>
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = modifier
                .padding(top = 56.dp, start = 10.dp, bottom = 18.dp)
                .width(40.dp)
                .height(40.dp)
                .clickable { }
        ) {
            Image(
                painter = painterResource(R.drawable.ic_arrow_left),
                contentDescription = "backArrow",
                modifier = modifier
                    .width(40.dp)
                    .height(40.dp)
                    .clickable { }
            )
        }

        Text(
            modifier = modifier.padding(top = 35.dp, start = 40.dp),
            text = stringResource(id = R.string.insert_store),
            fontSize = 24.sp
        )

        Text(
            modifier = modifier.padding(top = 34.dp, start = 32.dp),
            text = stringResource(id = R.string.insert_store_main_info),
            fontSize = 18.sp
        )

        InsertStoreProgressBar(modifier, 0.25f, R.string.insert_store_category_setting, R.string.page_one)

        Text(
            modifier = modifier.padding(top = 24.dp, start = 40.dp),
            text = stringResource(id = R.string.insert_store_choose_category),
            fontSize = 18.sp
        )

        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            modifier = modifier
                .padding(top = 28.dp)
                .padding(horizontal = 15.dp)
                .height(200.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            items(categories) { category ->
                CategoryItem(
                    modifier = Modifier,
                    imageUrl = category.imageUrl,
                    name = category.name
                )
            }
        }

        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(ColorPrimary),
            shape = RectangleShape,
            modifier = modifier
                .padding(top = 57.dp, start = 240.dp, end = 16.dp)
                .height(38.dp)
                .width(105.dp)
        ) {
            Text(
                text = stringResource(id = R.string.next),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

    }

}

@Composable
fun InsertStoreProgressBar(
    modifier: Modifier,
    range: Float,
    resourceId: Int,
    pageId: Int
) {
    Row(
        modifier = modifier
            .padding(top = 24.dp)
            .padding(horizontal = 32.dp)
    ) {
        Text(
            text = stringResource(id = resourceId),
            fontSize = 15.sp,
            color = ColorSecondary
        )

        Text(
            modifier = modifier.padding(start = 160.dp),
            text = stringResource(id = pageId),
            fontSize = 15.sp,
            color = ColorSecondary
        )

    }
    LinearProgressIndicator(
        modifier = modifier
            .padding(top = 4.dp)
            .padding(horizontal = 32.dp)
            .fillMaxWidth()
            .height(3.dp)
        ,
        color = ColorSecondary,
        backgroundColor = Color.Gray,
        progress = range
    )
}

@Composable
fun CategoryItem(modifier: Modifier, imageUrl: String, name: String) {
    Column(
        modifier = modifier
            .width(70.dp)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            modifier = modifier.size(44.dp),
            painter = painterResource(R.drawable.ic_chicken),
            alignment = Alignment.Center,
            contentDescription = "category_image"
        )
        Text(
            modifier = modifier.fillMaxWidth(),
            text = "일반음식점",
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
    }
}

@Preview
@Composable
fun PreviewCategoryItem() {
    CategoryItem(
        modifier = Modifier,
        imageUrl = "",
        name = ""
    )
}

@Preview
@Composable
fun PreviewSelectCategoryScreen(){
    SelectCategoryScreen(
        categories = sampleCategories
    )
}

val sampleCategories = listOf(
    StoreCategories(1,"imageUrl1", "일반음식점"),
    StoreCategories(1, "imageUrl2", "패스트푸드"),
    StoreCategories(1,"imageUrl3", "카페"),
    StoreCategories(1,"imageUrl4", "디저트"),
    StoreCategories(1,"imageUrl5", "바베큐"),
    StoreCategories(1,"imageUrl5", "바베큐"),
    StoreCategories(1,"imageUrl5", "바베큐"),
    StoreCategories(1,"imageUrl5", "바베큐"),
    StoreCategories(1,"imageUrl5", "바베큐")
)