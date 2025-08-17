package `in`.koreatech.koin.feature.dining.component

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.feature.dining.R

@Composable
fun DiningItemOriginal(
    dining: Dining,
    context: Context,
    modifier: Modifier = Modifier,
    onImageClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = KoinTheme.colors.neutral0)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dining.place,
                    style = KoinTheme.typography.bold16.copy(fontWeight = FontWeight.SemiBold)
                )
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${dining.kcal}kcal",
                    style = KoinTheme.typography.regular12,
                    color = KoinTheme.colors.neutral500
                )
                Text(
                    text = "•",
                    style = KoinTheme.typography.regular12,
                    color = KoinTheme.colors.neutral500
                )
                Text(
                    text = "${dining.priceCard}원/${dining.priceCash}원",
                    style = KoinTheme.typography.regular12,
                    color = KoinTheme.colors.neutral500
                )
            }
            if(dining.soldOutAt.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .background(
                            color = KoinTheme.colors.warning200,
                            shape = KoinTheme.shapes.extraSmall
                        )
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.sold_out),
                        style = KoinTheme.typography.medium14,
                        color = KoinTheme.colors.warning600
                    )
                }
            }
        }
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f),
                contentAlignment = Alignment.Center
            ) {
                if(dining.imageUrl.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = KoinTheme.colors.neutral50,
                                shape = KoinTheme.shapes.small
                            )
                            .border(
                                width = 1.dp,
                                color = KoinTheme.colors.neutral400,
                                shape = KoinTheme.shapes.small
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(R.drawable.no_photo),
                                contentDescription = ""
                            )
                            Text(
                                text = stringResource(R.string.no_photo),
                                style = KoinTheme.typography.regular14,
                                color = KoinTheme.colors.neutral500 // Change to a similar color; original color is 0xFF8E8E8E
                            )
                        }
                    }
                }
                else {
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .clip(shape = KoinTheme.shapes.small)
                            .clickable {
                                onImageClick()
                            },
                        model = ImageRequest.Builder(context)
                            .data(dining.imageUrl)
                            .build(),
                        contentDescription = "Dining Image",
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        loading = {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    )
                }
                if(dining.soldOutAt.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = KoinTheme.colors.neutral800.copy(alpha = 0.5f),
                                shape = KoinTheme.shapes.small
                            )
                            .zIndex(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(25.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(R.drawable.no_meals),
                                contentDescription = "",
                                modifier = Modifier.scale(1.5f)
                            )
                            Text(
                                text = stringResource(R.string.sold_out_menu),
                                style = KoinTheme.typography.regular14,
                                color = KoinTheme.colors.neutral0
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    dining.menu.forEachIndexed { index, item ->
                        if(index %2 == 0) {
                            Text(
                                text = item,
                                style = KoinTheme.typography.regular14
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    dining.menu.forEachIndexed { index, item ->
                        if(index %2 == 1) {
                            Text(
                                text = item,
                                style = KoinTheme.typography.regular14
                            )
                        }
                    }
                }
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = KoinTheme.colors.neutral100
        )
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    shape = KoinTheme.shapes.small.copy(
                        topStart = CornerSize(0.dp),
                        topEnd = CornerSize(0.dp)
                    )
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(
                        bounded = true,
                        color = KoinTheme.colors.neutral500
                    )
                ) {
                    onShareClick()
                }
                .padding(vertical = 14.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Row (
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.action_share_origin),
                    style = KoinTheme.typography.regular14,
                    color = KoinTheme.colors.neutral600
                )
            }
        }
    }
}

@Preview
@Composable
private fun DiningItemOriginalPreview() {
    DiningItemOriginal(
        Dining(
            id = 0,
            date = "2025.05.17",
            type = "BREAKFAST",
            place = "A코너",
            priceCard = "1000",
            priceCash = "1000",
            kcal = "786",
            menu = listOf("밥","국","김치"),
            imageUrl = "",
            createdAt = "2025.05.17",
            updatedAt = "2025.05.17",
            soldOutAt = "",
            changedAt = "2025.05.17"
        ),
        context = LocalContext.current
    )
}

@Preview
@Composable
private fun DiningItemOriginalSoldoutPreview() {
    DiningItemOriginal(
        Dining(
            id = 0,
            date = "2025.05.17",
            type = "아침",
            place = "A코너",
            priceCard = "1000",
            priceCash = "1000",
            kcal = "786",
            menu = listOf("밥","국","김치"),
            imageUrl = "",
            createdAt = "2025.05.17",
            updatedAt = "2025.05.17",
            soldOutAt = "2025.05.17",
            changedAt = "2025.05.17"
        ),
        context = LocalContext.current
    )
}