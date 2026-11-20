package `in`.koreatech.koin.feature.land.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.land.R
import `in`.koreatech.koin.feature.land.components.map.createLandMarkerIcon
import `in`.koreatech.koin.feature.land.components.map.rememberLandMapView

private const val DETAIL_MAP_ZOOM = 16.0

@Composable
internal fun LandDetailMap(
    landId: Int,
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    if (latitude !in -90.0..90.0 || longitude !in -180.0..180.0) {
        Box(
            modifier = modifier
                .background(RebrandKoinTheme.colors.neutral100)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.land_detail_location_unavailable),
                style = RebrandKoinTheme.typography.regular14,
                color = RebrandKoinTheme.colors.neutral500
            )
        }
        return
    }

    val cameraPosition = remember(landId, latitude, longitude) {
        CameraPosition(LatLng(latitude, longitude), DETAIL_MAP_ZOOM)
    }
    val mapView = rememberLandMapView(
        savedStateKey = "land_detail_map_$landId",
        initialCameraPosition = cameraPosition
    )
    val density = LocalDensity.current.density
    val markerColor = RebrandKoinTheme.colors.primary500.toArgb()
    val markerIcon = remember(mapView, density, markerColor) {
        createLandMarkerIcon(mapView.context, density, markerColor)
    }

    DisposableEffect(mapView, cameraPosition, markerIcon) {
        var disposed = false
        var boundMap: NaverMap? = null
        var marker: Marker? = null
        mapView.getMapAsync { map ->
            if (disposed || map.isDestroyed) return@getMapAsync
            boundMap = map
            map.uiSettings.apply {
                setAllGesturesEnabled(false)
                isZoomControlEnabled = false
                isCompassEnabled = false
                isIndoorLevelPickerEnabled = false
                isLocationButtonEnabled = false
            }
            // Keep restored camera state only when it still centers on this property.
            if (map.cameraPosition.target != cameraPosition.target) {
                map.cameraPosition = cameraPosition
            }
            marker = Marker(cameraPosition.target, markerIcon).apply {
                this.map = map
            }
        }

        onDispose {
            disposed = true
            // The shared lifecycle owner may have already destroyed the native map.
            if (boundMap?.isDestroyed == false) {
                marker?.map = null
            }
            marker = null
            boundMap = null
        }
    }

    key(mapView) {
        AndroidView(
            factory = { mapView },
            modifier = modifier
        )
    }
}
