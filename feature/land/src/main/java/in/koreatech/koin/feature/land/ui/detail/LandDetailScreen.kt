package `in`.koreatech.koin.feature.land.ui.detail

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventExtra
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.analytics.RealEstateAnalyticsConstant
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.snackbar.KoinSnackbarHost
import `in`.koreatech.koin.core.designsystem.component.snackbar.rememberKoinSnackbarHostState
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar2
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.land.R
import `in`.koreatech.koin.feature.land.components.LandRentalTypeBadge
import `in`.koreatech.koin.feature.land.components.LandStatus
import `in`.koreatech.koin.feature.land.model.LandDetailUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LandDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: LandDetailViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = rememberKoinSnackbarHostState()
    val callUnavailable = stringResource(R.string.land_detail_call_unavailable)
    val permissionDenied = stringResource(R.string.land_detail_call_permission_denied)
    val coroutineScope = rememberCoroutineScope()
    val callPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            viewModel.onPhoneClick()
        } else {
            coroutineScope.launch { snackbarHostState.showSnackbar(permissionDenied) }
        }
    }

    BackHandler(onBack = viewModel::onBackClick)
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            LandDetailSideEffect.NavigateBack -> onNavigateBack()
            is LandDetailSideEffect.CallPhone -> {
                try {
                    context.startActivity(
                        Intent(Intent.ACTION_CALL, Uri.fromParts("tel", sideEffect.phone, null))
                    )
                } catch (_: ActivityNotFoundException) {
                    snackbarHostState.showSnackbar(callUnavailable)
                } catch (_: SecurityException) {
                    snackbarHostState.showSnackbar(permissionDenied)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = RebrandKoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar2(
                title = { Text(stringResource(R.string.land_title)) },
                onNavigationIconClick = {
                    EventLogger.logClickEvent(
                        EventAction.DA1,
                        RealEstateAnalyticsConstant.ROOM_COMMON_BACK_CLICK,
                        RealEstateAnalyticsConstant.VALUE_BACK,
                        EventExtra(RealEstateAnalyticsConstant.SOURCE_SCREEN, RealEstateAnalyticsConstant.SCREEN_ROOM_DETAIL)
                    )
                    viewModel.onBackClick()
                }
            )
        },
        bottomBar = {
            LandDetailContactBar(
                phone = state.detail?.phone.orEmpty(),
                enabled = state.canDialPhone,
                onPhoneClick = {
                    EventLogger.logClickEvent(
                        EventAction.DA1,
                        RealEstateAnalyticsConstant.ROOM_DETAIL_CONTACT_CALL_CLICK,
                        RealEstateAnalyticsConstant.VALUE_CONTACT_CALL
                    )
                    callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                }
            )
        },
        snackbarHost = { KoinSnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        val detail = state.detail
        when {
            state.isLoading -> LandStatus(
                messageRes = R.string.land_detail_loading,
                modifier = Modifier.fillMaxSize().padding(padding),
                isLoading = true
            )
            state.hasLoadError || detail == null -> LandStatus(
                messageRes = R.string.land_detail_load_failed,
                modifier = Modifier.fillMaxSize().padding(padding)
            )
            else -> {
                LandDetailContent(
                    detail = detail,
                    isMonthlyAvailable = state.isMonthlyAvailable,
                    isCharterAvailable = state.isCharterAvailable,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
private fun LandDetailContent(
    detail: LandDetailUiModel,
    isMonthlyAvailable: Boolean,
    isCharterAvailable: Boolean,
    modifier: Modifier = Modifier
) {
    val inquire = stringResource(R.string.land_detail_inquire)
    val unavailable = stringResource(R.string.land_detail_info_unavailable)
    val rentLabel = stringResource(R.string.land_detail_rent_label)
    val depositLabel = stringResource(R.string.land_detail_deposit_label)
    val sizeLabel = stringResource(R.string.land_detail_size_label)
    val roomTypeLabel = stringResource(R.string.land_detail_room_type_label)
    val labels = remember(rentLabel, depositLabel, sizeLabel, roomTypeLabel) {
        persistentListOf(rentLabel, depositLabel, sizeLabel, roomTypeLabel)
    }
    val labelStyle = RebrandKoinTheme.typography.regular12
    val textMeasurer = rememberTextMeasurer(cacheSize = 0)
    val labelWidthPx = remember(labels, labelStyle, textMeasurer) {
        labels.maxOf { textMeasurer.measure(it, labelStyle, softWrap = false, maxLines = 1).size.width }
    }
    val labelWidth = with(LocalDensity.current) { labelWidthPx.toDp() }
    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            if (isMonthlyAvailable || isCharterAvailable) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (isMonthlyAvailable) {
                        LandRentalTypeBadge(
                            text = stringResource(R.string.land_filter_monthly),
                            containerColor = RebrandKoinTheme.colors.success200,
                            contentColor = RebrandKoinTheme.colors.success700
                        )
                    }
                    if (isCharterAvailable) {
                        LandRentalTypeBadge(
                            text = stringResource(R.string.land_filter_charter),
                            containerColor = RebrandKoinTheme.colors.warning100,
                            contentColor = RebrandKoinTheme.colors.warning600
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
            Text(
                text = detail.name.ifBlank { unavailable },
                style = RebrandKoinTheme.typography.bold20.copy(fontWeight = FontWeight.Medium),
                color = Color.Black
            )
        }
        LandDetailPhotos(imageUrls = detail.imageUrls, name = detail.name)
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
            Spacer(Modifier.height(32.dp))
            Text(
                text = stringResource(R.string.land_detail_room_info),
                style = RebrandKoinTheme.typography.medium16,
                color = Color.Black
            )
            Spacer(Modifier.height(16.dp))
            LandDetailInfoRow(
                label = rentLabel,
                labelWidth = labelWidth,
                value = if (detail.monthlyFee.isBlank() && detail.charterFee.isBlank()) {
                    inquire
                } else {
                    stringResource(
                        R.string.land_detail_paired_values,
                        detail.monthlyFee.ifBlank { inquire },
                        detail.charterFee.ifBlank { inquire }
                    )
                }
            )
            LandDetailInfoRow(
                label = depositLabel,
                labelWidth = labelWidth,
                value = if (detail.deposit.isBlank() && detail.managementFee.isBlank()) {
                    inquire
                } else {
                    stringResource(
                        R.string.land_detail_paired_values,
                        detail.deposit.ifBlank { inquire },
                        detail.managementFee.ifBlank { inquire }
                    )
                }
            )
            val size = detail.size.trim()
            LandDetailInfoRow(
                label = sizeLabel,
                labelWidth = labelWidth,
                value = when {
                    size.isBlank() -> unavailable
                    size.toDoubleOrNull()?.isFinite() == true -> stringResource(R.string.land_detail_size_pyeong, size)
                    else -> detail.size
                }
            )
            LandDetailInfoRow(
                label = roomTypeLabel,
                labelWidth = labelWidth,
                value = detail.roomType.ifBlank { unavailable }
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.land_detail_location_info),
                style = RebrandKoinTheme.typography.medium16,
                color = Color.Black
            )
            Spacer(Modifier.height(16.dp))
            LandDetailMap(
                landId = detail.id,
                latitude = detail.latitude,
                longitude = detail.longitude,
                modifier = Modifier.fillMaxWidth().height(180.dp)
            )
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun LandDetailPhotos(imageUrls: ImmutableList<String>, name: String) {
    val photoModifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1.92f)
        .background(RebrandKoinTheme.colors.neutral100)
    Column {
        if (imageUrls.isEmpty()) {
            Column(
                modifier = photoModifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_land_bbiko_sleep),
                    contentDescription = null
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.land_detail_photo_preparing),
                    style = RebrandKoinTheme.typography.regular14,
                    color = RebrandKoinTheme.colors.neutral600
                )
            }
            Spacer(Modifier.height(32.dp))
        } else {
            val pagerState = rememberPagerState(pageCount = { imageUrls.size })
            LaunchedEffect(pagerState, imageUrls) {
                var previousPage = pagerState.settledPage
                snapshotFlow { pagerState.settledPage }.collect { page ->
                    if (page != previousPage) {
                        EventLogger.logSwipeEvent(
                            EventAction.DA1,
                            RealEstateAnalyticsConstant.ROOM_DETAIL_PHOTO_SLIDE_SWIPE,
                            if (page > previousPage) {
                                RealEstateAnalyticsConstant.VALUE_NEXT
                            } else {
                                RealEstateAnalyticsConstant.VALUE_PREVIOUS
                            }
                        )
                        previousPage = page
                    }
                }
            }
            var selectedPhotoUrl by remember(imageUrls) { mutableStateOf<String?>(null) }
            selectedPhotoUrl?.let { imageUrl ->
                LandPhotoViewerDialog(
                    imageUrl = imageUrl,
                    contentDescription = stringResource(R.string.land_detail_photo_description, name, pagerState.currentPage + 1),
                    onDismiss = { selectedPhotoUrl = null }
                )
            }
            HorizontalPager(state = pagerState, modifier = photoModifier) { page ->
                AsyncImage(
                    model = imageUrls[page],
                    contentDescription = stringResource(R.string.land_detail_photo_description, name, page + 1),
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            selectedPhotoUrl = imageUrls[page]
                        },
                    contentScale = ContentScale.Crop
                )
            }
            val photoPosition = stringResource(
                R.string.land_detail_photo_position,
                pagerState.currentPage + 1,
                imageUrls.size
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .semantics { contentDescription = photoPosition },
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(imageUrls.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(
                                color = if (pagerState.currentPage == index) {
                                    RebrandKoinTheme.colors.primary500
                                } else {
                                    RebrandKoinTheme.colors.neutral400
                                },
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun LandDetailInfoRow(label: String, value: String, labelWidth: Dp) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.width(labelWidth),
            style = RebrandKoinTheme.typography.regular12,
            color = RebrandKoinTheme.colors.neutral600
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            style = RebrandKoinTheme.typography.regular12,
            color = Color.Black
        )
    }
}

@Composable
private fun LandDetailContactBar(phone: String, enabled: Boolean, onPhoneClick: () -> Unit) {
    val shape = remember { RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp) }
    Surface(
        color = RebrandKoinTheme.colors.neutral0,
        shape = shape,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.land_detail_contact),
                    style = RebrandKoinTheme.typography.regular12,
                    color = Color.Black
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = phone.ifBlank { stringResource(R.string.land_detail_phone_unavailable) },
                    style = RebrandKoinTheme.typography.medium14,
                    color = Color.Black
                )
            }
            FilledButton(
                text = stringResource(R.string.land_detail_phone_inquiry),
                onClick = onPhoneClick,
                enabled = enabled,
                textStyle = RebrandKoinTheme.typography.medium14,
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RebrandKoinTheme.colors.primary500,
                    contentColor = RebrandKoinTheme.colors.neutral0,
                    disabledContainerColor = RebrandKoinTheme.colors.neutral400,
                    disabledContentColor = RebrandKoinTheme.colors.neutral0
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}
