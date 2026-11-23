package `in`.koreatech.koin.feature.land.components.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.clustering.Clusterer
import com.naver.maps.map.clustering.ClusteringKey
import com.naver.maps.map.clustering.DefaultClusterOnClickListener
import com.naver.maps.map.overlay.Marker
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.analytics.RealEstateAnalyticsConstant
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.land.R
import `in`.koreatech.koin.feature.land.model.LandUiModel
import java.util.WeakHashMap
import kotlinx.collections.immutable.ImmutableList

private const val MARKER_OVERLAP_DISTANCE_DP = 44.0
private const val SELECTED_LAND_ZOOM = 16.0

@Composable
internal fun BindLandMap(
    mapView: MapView,
    lands: ImmutableList<LandUiModel>,
    bottomPaddingPx: Int,
    selectedLandId: Int?,
    onLandSelect: (Int?) -> Unit
) {
    val density = LocalDensity.current
    val fillColor = RebrandKoinTheme.colors.primary500.toArgb()
    val ringColor = RebrandKoinTheme.colors.primary300.toArgb()
    val selectedColor = RebrandKoinTheme.colors.primary700.toArgb()
    val currentOnLandSelect = rememberUpdatedState(onLandSelect)
    val binding = remember(mapView, density, fillColor, ringColor, selectedColor) {
        LandMapBinding(
            LandMarkerIcons(mapView.context, density, fillColor, ringColor, selectedColor),
            onLandSelect = { currentOnLandSelect.value(it) }
        )
    }
    val items = remember(lands) {
        buildMap(lands.size) {
            for (land in lands) {
                put(LandMarkerKey(land.id, LatLng(land.latitude, land.longitude)), null)
            }
        }
    }

    DisposableEffect(mapView, binding) {
        mapView.setTag(R.id.land_map_marker_binding, binding)
        mapView.getMapAsync(binding::onMapReady)

        onDispose {
            detachLandMap(mapView)
        }
    }
    SideEffect {
        binding.update(items, bottomPaddingPx, selectedLandId)
    }
}

internal fun detachLandMap(mapView: MapView) {
    val binding = mapView.getTag(R.id.land_map_marker_binding) as? LandMapBinding ?: return
    mapView.setTag(R.id.land_map_marker_binding, null)
    binding.dispose()
}

private data class LandMarkerKey(
    val id: Int,
    private val coordinate: LatLng
) : ClusteringKey {
    override fun getPosition(): LatLng = coordinate
}

private class LandMapBinding(
    private val icons: LandMarkerIcons,
    private val onLandSelect: (Int?) -> Unit
) {
    private var isDisposed = false
    private var clusterer: Clusterer<LandMarkerKey>? = null
    private var items: Map<LandMarkerKey, Nothing?> = emptyMap()
    private var bottomPaddingPx = 0
    private var selectedLandId: Int? = null

    private val leafMarkers = WeakHashMap<Marker, Int>()
    private val mapClickListener = NaverMap.OnMapClickListener { _, _ ->
        if (!isDisposed) onLandSelect(null)
    }

    fun onMapReady(map: NaverMap) {
        if (isDisposed || map.isDestroyed || clusterer != null) return
        map.uiSettings.isZoomControlEnabled = false
        map.setContentPadding(0, 0, 0, 0)
        map.setContentPadding(0, 0, 0, bottomPaddingPx, true)
        map.onMapClickListener = mapClickListener
        val builder = Clusterer.Builder<LandMarkerKey>()
        map.maxZoom = builder.maxZoom.toDouble()
        clusterer = builder
            .screenDistance(MARKER_OVERLAP_DISTANCE_DP)
            .clusterMarkerUpdater { info, marker ->
                if (!isDisposed) {
                    leafMarkers.remove(marker)
                    icons.updateClusterMarker(info.size, marker)
                    val defaultListener = DefaultClusterOnClickListener(info)
                    marker.setOnClickListener { overlay ->
                        if (isDisposed) {
                            true
                        } else {
                            EventLogger.logClickEvent(
                                EventAction.DA1,
                                RealEstateAnalyticsConstant.ROOM_LIST_MAP_CLUSTER_CLICK,
                                RealEstateAnalyticsConstant.VALUE_MAP_CLUSTER
                            )
                            defaultListener.onClick(overlay)
                        }
                    }
                }
            }
            .leafMarkerUpdater { info, marker ->
                if (!isDisposed) {
                    val key = info.key as LandMarkerKey
                    val landId = key.id
                    leafMarkers[marker] = landId
                    icons.updateLeafMarker(marker, landId == selectedLandId)
                    marker.setOnClickListener {
                        if (!isDisposed) {
                            onLandSelect(landId)
                            map.moveCamera(
                                CameraUpdate.scrollAndZoomTo(key.getPosition(), maxOf(map.cameraPosition.zoom, SELECTED_LAND_ZOOM))
                                    .animate(CameraAnimation.Easing)
                            )
                        }
                        true
                    }
                }
            }
            .build()
            .also {
                it.addAll(items)
                it.map = map
            }
    }

    fun update(
        newItems: Map<LandMarkerKey, Nothing?>,
        bottomPaddingPx: Int,
        selectedLandId: Int?
    ) {
        if (isDisposed) return
        if (this.selectedLandId != selectedLandId) {
            val previousLandId = this.selectedLandId
            this.selectedLandId = selectedLandId
            for ((marker, landId) in leafMarkers) {
                if (landId == previousLandId || landId == selectedLandId) {
                    icons.updateLeafIcon(marker, landId == selectedLandId)
                }
            }
        }
        if (this.bottomPaddingPx != bottomPaddingPx) {
            this.bottomPaddingPx = bottomPaddingPx
            clusterer?.map?.setContentPadding(0, 0, 0, bottomPaddingPx, true)
        }
        if (items == newItems) return
        items = newItems
        leafMarkers.clear()
        clusterer?.let {
            it.clear()
            it.addAll(newItems)
        }
    }

    fun dispose() {
        if (isDisposed) return
        isDisposed = true
        clusterer?.map?.let {
            if (!it.isDestroyed && it.onMapClickListener === mapClickListener) {
                it.onMapClickListener = null
            }
        }
        clusterer?.let {
            it.map = null
            it.clear()
        }
        clusterer = null
        items = emptyMap()
        leafMarkers.clear()
    }
}
