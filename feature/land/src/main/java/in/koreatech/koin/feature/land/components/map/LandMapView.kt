package `in`.koreatech.koin.feature.land.components.map

import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.savedstate.compose.LocalSavedStateRegistryOwner
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMapOptions

private const val MAP_SAVED_STATE_KEY = "land_map"

@Composable
internal fun rememberLandMapView(
    savedStateKey: String = MAP_SAVED_STATE_KEY,
    initialCameraPosition: CameraPosition? = null
): MapView {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val savedStateRegistry = LocalSavedStateRegistryOwner.current.savedStateRegistry
    val mapView = remember(context, lifecycle, savedStateRegistry, savedStateKey) {
        MapView(
            context,
            NaverMapOptions().useTextureView(true).camera(
                initialCameraPosition ?: CameraPosition(LatLng(36.763695, 127.281796), 14.0)
            )
        ).apply {
            onCreate(savedStateRegistry.consumeRestoredStateForKey(savedStateKey))
        }
    }

    DisposableEffect(mapView, lifecycle, savedStateRegistry) {
        var isStarted = false
        var isResumed = false
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    mapView.onStart()
                    isStarted = true
                }
                Lifecycle.Event.ON_RESUME -> {
                    mapView.onResume()
                    isResumed = true
                }
                Lifecycle.Event.ON_PAUSE -> {
                    mapView.onPause()
                    isResumed = false
                }
                Lifecycle.Event.ON_STOP -> {
                    mapView.onStop()
                    isStarted = false
                }
                else -> Unit
            }
        }
        val memoryCallbacks = object : ComponentCallbacks {
            override fun onConfigurationChanged(newConfig: Configuration) = Unit

            override fun onLowMemory() {
                mapView.onLowMemory()
            }
        }
        val applicationContext = context.applicationContext

        savedStateRegistry.registerSavedStateProvider(savedStateKey) {
            Bundle().also(mapView::onSaveInstanceState)
        }
        applicationContext.registerComponentCallbacks(memoryCallbacks)
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
            savedStateRegistry.unregisterSavedStateProvider(savedStateKey)
            applicationContext.unregisterComponentCallbacks(memoryCallbacks)
            if (isResumed) mapView.onPause()
            if (isStarted) mapView.onStop()
            detachLandMap(mapView)
            mapView.onDestroy()
        }
    }

    return mapView
}
