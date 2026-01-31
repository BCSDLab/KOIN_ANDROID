package `in`.koreatech.koin.feature.lostandfound.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import `in`.koreatech.koin.core.designsystem.component.picker.KoinPicker
import `in`.koreatech.koin.core.designsystem.component.picker.rememberPickerState
import `in`.koreatech.koin.core.designsystem.component.text.LeadingIconText
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.DESCRIPTION_MAX_LENGTH
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import java.time.LocalDate

@Composable
fun EditArticleItemDetail(
    modifier: Modifier = Modifier,
    type: LostOrFoundType,
    location: String,
    locationRequired: Boolean = false,
    onLocationChange: (String) -> Unit = {},
    moreDescription: String,
    onMoreDescriptionChange: (String) -> Unit = {},
    date: LocalDate?,
    dateRequired: Boolean = false,
    onDateChange: (date: LocalDate) -> Unit = {},
    shouldCollapse: Boolean = false
) {
    val dayPickerState = rememberPickerState()
    val monthPickerState = rememberPickerState()
    val yearPickerState = rememberPickerState()

    var isPickerExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(shouldCollapse) {
        if (shouldCollapse) {
            isPickerExpanded = false
        }
    }

    val now = LocalDate.now()

    val yearList = (LocalDate.now().year - 1..LocalDate.now().year).map { it.toString() }
    var monthList by remember { mutableStateOf((1..12).map { it.toString() }) }
    var dayList by remember { mutableStateOf((1..31).map { it.toString() }) }

    LaunchedEffect(Unit) {
        if (date != null) {
            yearPickerState.selectedItem = date.year.toString()
            yearPickerState.selectedItemIndex = yearList.indexOf(date.year.toString())
            monthPickerState.selectedItem = date.monthValue.toString()
            monthPickerState.selectedItemIndex = monthList.indexOf(date.monthValue.toString())
            dayPickerState.selectedItem = date.dayOfMonth.toString()
            dayPickerState.selectedItemIndex = dayList.indexOf(date.dayOfMonth.toString())
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                style = KoinTheme.typography.medium14,
                text = buildAnnotatedString {
                    append(
                        when (type) {
                            LostOrFoundType.LOST -> stringResource(id = R.string.lost_date)
                            LostOrFoundType.FOUND -> stringResource(id = R.string.found_date)
                        }
                    )
                    withStyle(style = SpanStyle(color = Color(0xFFC82A2A))) {
                        append("*")
                    }
                }
            )

            if (dateRequired) {
                LeadingIconText(
                    textStyle = KoinTheme.typography.regular12.copy(color = Color(0xFFF7941E)),
                    text =
                    when (type) {
                        LostOrFoundType.LOST -> stringResource(id = R.string.lost_date_required)
                        LostOrFoundType.FOUND -> stringResource(id = R.string.found_date_required)
                    },
                    iconRes = R.drawable.ic_required,
                    iconSize = 16.dp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        var dateComposablePosition by remember { mutableStateOf(Offset.Zero) }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(KoinTheme.colors.neutral100)
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .noRippleClickable {
                    isPickerExpanded = !isPickerExpanded
                }
                .onGloballyPositioned {
                    dateComposablePosition = it.positionInParent() +
                        Offset(
                            0f,
                            it.size.height.toFloat()
                        )
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (dayPickerState.selectedItem == "" || monthPickerState.selectedItem == "" || yearPickerState.selectedItem == "") {
                    Text(
                        modifier = Modifier.weight(1f),
                        color = KoinTheme.colors.neutral500,
                        style = KoinTheme.typography.regular12,
                        text =
                        when (type) {
                            LostOrFoundType.LOST -> stringResource(id = R.string.lost_date_hint)
                            LostOrFoundType.FOUND -> stringResource(id = R.string.found_date_hint)
                        }
                    )
                } else {
                    Text(
                        modifier = Modifier.weight(1f),
                        style = KoinTheme.typography.regular14,
                        text = "${yearPickerState.selectedItem.substring(
                            2
                        )}.${monthPickerState.selectedItem}.${dayPickerState.selectedItem}"
                    )
                }

                val rotateDegree: Float by animateFloatAsState(
                    targetValue = if (isPickerExpanded) 180f else 0f,
                    label = "degree"
                )

                Image(
                    modifier =
                    Modifier
                        .size(24.dp)
                        .rotate(rotateDegree),
                    painter = painterResource(id = R.drawable.ic_dropdown_arrow),
                    contentDescription =
                    when (type) {
                        LostOrFoundType.LOST -> stringResource(id = R.string.lost_date_hint)
                        LostOrFoundType.FOUND -> stringResource(id = R.string.found_date_hint)
                    }
                )
            }
        }

        LaunchedEffect(
            dayPickerState.selectedItem,
            monthPickerState.selectedItem,
            yearPickerState.selectedItem
        ) {
            if (yearPickerState.selectedItem != "" && monthPickerState.selectedItem != "" && dayPickerState.selectedItem != "") {
                val selectedYear = Integer.parseInt(yearPickerState.selectedItem)
                val selectedMonth = Integer.parseInt(monthPickerState.selectedItem)
                val selectedDay = Integer.parseInt(dayPickerState.selectedItem)
                val lastDayOfMonth = getLastDayOfMonth(selectedYear, selectedMonth)
                onDateChange(
                    LocalDate.of(
                        selectedYear,
                        selectedMonth,
                        if (selectedDay > lastDayOfMonth) lastDayOfMonth else selectedDay
                    )
                )
            }
        }

        LaunchedEffect(isPickerExpanded) {
            if (date == null) {
                yearPickerState.selectedItemIndex = yearList.indexOf(now.year.toString())
                monthPickerState.selectedItemIndex = monthList.indexOf(now.monthValue.toString())
                dayPickerState.selectedItemIndex = dayList.indexOf(now.dayOfMonth.toString())
            }
        }

        if (isPickerExpanded) {
            LaunchedEffect(yearPickerState.selectedItem, monthPickerState.selectedItem) {
                if (yearPickerState.selectedItem == "") return@LaunchedEffect
                if (monthPickerState.selectedItem == "") return@LaunchedEffect
                if (yearPickerState.selectedItem == now.year.toString()) {
                    monthList = (1..now.monthValue).map { it.toString() }

                    if (monthPickerState.selectedItem == "") return@LaunchedEffect
                    if (monthPickerState.selectedItem == now.monthValue.toString()) {
                        dayList = (1..now.dayOfMonth).map { it.toString() }
                    } else {
                        val lastDayOfMonth =
                            getLastDayOfMonth(
                                now.year,
                                Integer.parseInt(monthPickerState.selectedItem)
                            )
                        dayList = (1..lastDayOfMonth).map { it.toString() }
                    }
                } else {
                    monthList = (1..12).map { it.toString() }

                    val lastDayOfMonth =
                        getLastDayOfMonth(
                            Integer.parseInt(yearPickerState.selectedItem),
                            Integer.parseInt(monthPickerState.selectedItem)
                        )
                    dayList = (1..lastDayOfMonth).map { it.toString() }
                }
            }

            Popup(
                offset =
                IntOffset(
                    dateComposablePosition.x.toInt(),
                    dateComposablePosition.y.toInt()
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(KoinTheme.shapes.small)
                        .background(KoinTheme.colors.neutral100)

                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 24.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(vertical = 12.dp, horizontal = 32.dp)
                                .fillMaxWidth()
                        ) {
                            KoinPicker(
                                modifier = Modifier.weight(1f),
                                items = yearList,
                                pickerState = yearPickerState,
                                visibleItemsCount = 3,
                                contentPadding = PaddingValues(vertical = 3.dp, horizontal = 12.dp),
                                startIndex = yearPickerState.selectedItemIndex,
                                infiniteScroll = false,
                                selectedTextStyle =
                                KoinTheme.typography.medium16.copy(
                                    textAlign = TextAlign.Center
                                ),
                                unselectedTextStyle =
                                KoinTheme.typography.medium16.copy(
                                    textAlign = TextAlign.Center
                                )
                            )
                            KoinPicker(
                                modifier = Modifier.weight(1f),
                                items = monthList,
                                pickerState = monthPickerState,
                                visibleItemsCount = 3,
                                contentPadding = PaddingValues(vertical = 3.dp, horizontal = 12.dp),
                                startIndex = monthPickerState.selectedItemIndex,
                                infiniteScroll = false,
                                selectedTextStyle =
                                KoinTheme.typography.medium16.copy(
                                    textAlign = TextAlign.Center
                                ),
                                unselectedTextStyle =
                                KoinTheme.typography.medium16.copy(
                                    textAlign = TextAlign.Center
                                )
                            )
                            KoinPicker(
                                modifier = Modifier.weight(1f),
                                items = dayList,
                                pickerState = dayPickerState,
                                visibleItemsCount = 3,
                                contentPadding = PaddingValues(vertical = 3.dp, horizontal = 12.dp),
                                startIndex = dayPickerState.selectedItemIndex,
                                infiniteScroll = false,
                                selectedTextStyle =
                                KoinTheme.typography.medium16.copy(
                                    textAlign = TextAlign.Center
                                ),
                                unselectedTextStyle =
                                KoinTheme.typography.medium16.copy(
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                    HorizontalDivider(color = KoinTheme.colors.neutral300)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = KoinTheme.colors.neutral100)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp, horizontal = 16.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.date_reset),
                                style = KoinTheme.typography.medium14,
                                color = KoinTheme.colors.primary600,
                                modifier = Modifier
                                    .clickable {
                                        yearPickerState.selectedItemIndex = yearList.indexOf(now.year.toString())
                                        monthPickerState.selectedItemIndex = monthList.indexOf(now.month.toString())
                                        dayPickerState.selectedItemIndex = dayList.indexOf(now.dayOfMonth.toString())
                                    }
                                    .padding(all = 4.dp)
                            )
                            Spacer(
                                modifier = Modifier.width(24.dp)
                            )
                            Text(
                                text = stringResource(id = R.string.date_confirm),
                                style = KoinTheme.typography.medium14,
                                color = KoinTheme.colors.primary600,
                                modifier = Modifier
                                    .clickable {
                                        isPickerExpanded = false
                                    }
                                    .padding(all = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            style = KoinTheme.typography.medium14,
            text = buildAnnotatedString {
                when (type) {
                    LostOrFoundType.LOST -> append(stringResource(id = R.string.lost_location))
                    LostOrFoundType.FOUND -> {
                        append(stringResource(id = R.string.found_location))
                        withStyle(style = SpanStyle(color = Color(0xFFC82A2A))) {
                            append("*")
                        }
                    }
                }
            }
        )

        if (locationRequired) {
            LeadingIconText(
                textStyle = KoinTheme.typography.medium12.copy(color = Color(0xFFF7941E)),
                text =
                when (type) {
                    LostOrFoundType.LOST -> stringResource(id = R.string.lost_location_required)
                    LostOrFoundType.FOUND -> stringResource(id = R.string.found_location_required)
                },
                iconRes = R.drawable.ic_required,
                iconSize = 16.dp
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    EditArticleTextField(
        value = location,
        onValueChange = onLocationChange,
        singleLine = true,
        hint =
        when (type) {
            LostOrFoundType.LOST -> stringResource(id = R.string.lost_location_hint)
            LostOrFoundType.FOUND -> stringResource(id = R.string.found_location_hint)
        },
        modifier =
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(KoinTheme.colors.neutral100),
        textPaddingValues = PaddingValues(16.dp, 8.dp)
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            style = KoinTheme.typography.medium14,
            text = stringResource(id = R.string.more_description)
        )
        Text(
            style = KoinTheme.typography.regular12,
            text = "${moreDescription.length}/$DESCRIPTION_MAX_LENGTH",
            color = KoinTheme.colors.neutral500
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    EditArticleTextField(
        value = moreDescription,
        onValueChange = {
            if (moreDescription.length < DESCRIPTION_MAX_LENGTH) {
                onMoreDescriptionChange(it)
            } else {
                onMoreDescriptionChange(it.take(DESCRIPTION_MAX_LENGTH))
            }
        },
        hint = stringResource(id = R.string.more_description_hint),
        modifier =
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(KoinTheme.colors.neutral100),
        textPaddingValues = PaddingValues(16.dp, 8.dp, 16.dp, 32.dp)
    )

    Spacer(modifier = Modifier.height(16.dp))
}

private fun getLastDayOfMonth(
    year: Int,
    month: Int
): Int {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) 29 else 28
        else -> 31
    }
}
