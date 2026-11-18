package `in`.koreatech.koin.feature.land.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.land.R
import `in`.koreatech.koin.feature.land.model.LandUiModel

@Composable
internal fun LandRow(
    land: LandUiModel,
    modifier: Modifier = Modifier,
    highlightQuery: String = ""
) {
    val borderColor = RebrandKoinTheme.colors.neutral100
    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val halfStroke = strokeWidth / 2
                drawLine(
                    color = borderColor,
                    start = Offset(0f, halfStroke),
                    end = Offset(size.width, halfStroke),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = borderColor,
                    start = Offset(0f, size.height - halfStroke),
                    end = Offset(size.width, size.height - halfStroke),
                    strokeWidth = strokeWidth
                )
            }
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        LandInformation(
            land = land,
            modifier = Modifier.padding(end = 32.dp),
            highlightQuery = highlightQuery
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_land_chevron_right),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(24.dp)
        )
    }
}

@Composable
internal fun LandInformation(
    land: LandUiModel,
    modifier: Modifier = Modifier,
    highlightQuery: String = ""
) {
    val highlightColor = RebrandKoinTheme.colors.primary500
    val highlightedName = remember(land.name, highlightQuery, highlightColor) {
        highlightLandName(land.name, highlightQuery.trim(), highlightColor)
    }
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val baseNameStyle = RebrandKoinTheme.typography.medium16
            val nameStyle = remember(baseNameStyle) {
                baseNameStyle.copy(fontWeight = FontWeight.SemiBold)
            }
            val nameModifier = Modifier.weight(1f, fill = false)
            if (highlightedName == null) {
                Text(
                    text = land.name,
                    style = nameStyle,
                    color = RebrandKoinTheme.colors.neutral800,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = nameModifier
                )
            } else {
                Text(
                    text = highlightedName,
                    style = nameStyle,
                    color = RebrandKoinTheme.colors.neutral800,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = nameModifier
                )
            }
            if (land.roomType.isNotBlank()) {
                Text(
                    text = land.roomType,
                    style = RebrandKoinTheme.typography.regular10,
                    color = RebrandKoinTheme.colors.neutral600,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (land.address.isNotBlank()) {
            Text(
                text = land.address,
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral600
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (land.isMonthlyAvailable || land.isCharterAvailable) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                if (land.isMonthlyAvailable) {
                    LandPrice(
                        text = stringResource(R.string.land_filter_monthly),
                        price = land.monthlyFee,
                        containerColor = RebrandKoinTheme.colors.success200,
                        contentColor = RebrandKoinTheme.colors.success700
                    )
                }
                if (land.isCharterAvailable) {
                    LandPrice(
                        text = stringResource(R.string.land_filter_charter),
                        price = land.charterFee,
                        containerColor = RebrandKoinTheme.colors.warning100,
                        contentColor = RebrandKoinTheme.colors.warning600
                    )
                }
            }
        }
    }
}

private fun highlightLandName(name: String, query: String, color: Color): AnnotatedString? {
    if (query.isEmpty()) return null
    var matchIndex = name.indexOf(query, ignoreCase = true)
    if (matchIndex < 0) return null
    return buildAnnotatedString {
        append(name)
        val highlightStyle = SpanStyle(color = color)
        while (matchIndex >= 0) {
            val matchEnd = matchIndex + query.length
            addStyle(highlightStyle, matchIndex, matchEnd)
            matchIndex = name.indexOf(query, startIndex = matchEnd, ignoreCase = true)
        }
    }
}

@Composable
private fun LandPrice(
    text: String,
    price: String?,
    containerColor: Color,
    contentColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        LandRentalTypeBadge(
            text = text,
            containerColor = containerColor,
            contentColor = contentColor
        )
        if (!price.isNullOrBlank()) {
            Text(
                text = price,
                style = RebrandKoinTheme.typography.regular14,
                color = RebrandKoinTheme.colors.neutral800
            )
        }
    }
}
