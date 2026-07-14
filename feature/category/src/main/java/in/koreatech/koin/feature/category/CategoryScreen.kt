package `in`.koreatech.koin.feature.category

import android.content.ActivityNotFoundException
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.R as CoreR
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.feature.category.component.CAMPUS_MENUS
import `in`.koreatech.koin.feature.category.component.CategoryCard
import `in`.koreatech.koin.feature.category.component.CategoryIcon
import `in`.koreatech.koin.feature.category.component.CategoryMenuId
import `in`.koreatech.koin.feature.category.component.CategoryMenuList
import `in`.koreatech.koin.feature.category.component.OTHER_MENUS
import `in`.koreatech.koin.feature.category.component.TRANSPORT_MENUS
import `in`.koreatech.koin.feature.category.navigation.CategoryNavigationHandler
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import timber.log.Timber

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = hiltViewModel(),
    navigateToNotification: () -> Unit = {}
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current
    val navigator = rememberNavigator()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is CategorySideEffect.NavigateToMenu ->
                CategoryNavigationHandler.getIntent(effect.id, navigator, context, uiState.isAnonymous)?.let { intent ->
                    try {
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Timber.e(e)
                        ToastUtil.getInstance().makeShort(context.getString(R.string.category_cannot_open_link))
                    }
                }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars
    ) { paddingValues ->
        CategoryScreenImpl(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8FA))
                .padding(paddingValues),
            uiState = uiState,
            onMenuClick = viewModel::onMenuClick,
            navigateToNotification = navigateToNotification
        )
    }
}

@Composable
private fun CategoryScreenImpl(
    uiState: CategoryState,
    onMenuClick: (CategoryMenuId) -> Unit,
    modifier: Modifier = Modifier,
    navigateToNotification: () -> Unit = {}
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        HeaderSection(
            isNewNotificationReceived = uiState.isNewNotificationReceived,
            onNotificationClick = navigateToNotification
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val timetableTitle = stringResource(R.string.category_timetable)
                CategoryCard(
                    modifier = Modifier.weight(1f),
                    categoryIcon = {
                        CategoryIcon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_calendar_category),
                            contentDescription = timetableTitle
                        )
                    },
                    title = timetableTitle,
                    description = stringResource(R.string.timetable_description),
                    onClick = { onMenuClick(CategoryMenuId.TIMETABLE) }
                )

                val lostItemTitle = stringResource(R.string.lost_and_found)
                CategoryCard(
                    modifier = Modifier.weight(1f),
                    categoryIcon = {
                        CategoryIcon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_delivery_box),
                            contentDescription = lostItemTitle
                        )
                    },
                    title = lostItemTitle,
                    description = stringResource(R.string.lost_and_found_description),
                    onClick = { onMenuClick(CategoryMenuId.LOST_AND_FOUND) }
                )
            }

            CategoryMenuList(
                title = stringResource(R.string.campus),
                items = CAMPUS_MENUS,
                onItemClick = { menu -> onMenuClick(menu.id) }
            )
            CategoryMenuList(
                title = stringResource(R.string.transport),
                items = TRANSPORT_MENUS,
                onItemClick = { menu -> onMenuClick(menu.id) }
            )
            CategoryMenuList(
                title = stringResource(R.string.other),
                items = OTHER_MENUS,
                onItemClick = { menu -> onMenuClick(menu.id) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun HeaderSection(
    isNewNotificationReceived: Boolean,
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_bcsd_symbol),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(4.dp))

        Image(
            imageVector = ImageVector.vectorResource(CoreR.drawable.ic_koin_text),
            contentDescription = null
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = Modifier.noRippleClickable(onClick = onNotificationClick),
            imageVector = if (isNewNotificationReceived) {
                ImageVector.vectorResource(R.drawable.ic_category_notification_dot)
            } else {
                ImageVector.vectorResource(R.drawable.ic_category_notification)
            },
            contentDescription = null
        )
    }
}
