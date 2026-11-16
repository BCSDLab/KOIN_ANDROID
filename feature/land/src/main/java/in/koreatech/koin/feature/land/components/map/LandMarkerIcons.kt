package `in`.koreatech.koin.feature.land.components.map

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Typeface
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.ui.unit.Density
import androidx.core.graphics.createBitmap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import `in`.koreatech.koin.feature.land.R
import kotlin.math.roundToInt

private const val SINGLE_MARKER_SIZE_DP = 24

internal class LandMarkerIcons(
    context: Context,
    density: Density,
    fillColor: Int,
    ringColor: Int,
    selectedColor: Int
) {
    private val singleAnchor = PointF(0.5f, 1f)
    private val clusterAnchor = PointF(0.5f, 0.5f)
    private val single = createLandMarkerIcon(context, density.density, fillColor)
    private val selectedSingle = createLandMarkerIcon(context, density.density, selectedColor)
    private val cluster3 = createClusterIcon(3, 24, density, fillColor, ringColor)
    private val cluster6 = createClusterIcon(6, 34, density, fillColor, ringColor)
    private val cluster9 = createClusterIcon(9, 38, density, fillColor, ringColor)
    private val cluster12 = createClusterIcon(12, 44, density, fillColor, ringColor)

    fun updateClusterMarker(count: Int, marker: Marker) {
        marker.icon = when {
            count < 6 -> cluster3
            count < 9 -> cluster6
            count < 12 -> cluster9
            else -> cluster12
        }
        marker.anchor = clusterAnchor
        resetMarker(marker)
    }

    fun updateLeafMarker(marker: Marker, isSelected: Boolean) {
        updateLeafIcon(marker, isSelected)
        marker.anchor = singleAnchor
        resetMarker(marker)
    }

    fun updateLeafIcon(marker: Marker, isSelected: Boolean) {
        marker.icon = if (isSelected) selectedSingle else single
    }

    private fun resetMarker(marker: Marker) {
        marker.width = Marker.SIZE_AUTO
        marker.height = Marker.SIZE_AUTO
        marker.captionText = ""
        marker.subCaptionText = ""
    }
}

internal fun createLandMarkerIcon(
    context: Context,
    density: Float,
    color: Int
): OverlayImage {
    val size = (SINGLE_MARKER_SIZE_DP * density).roundToInt().coerceAtLeast(1)
    val bitmap = createBitmap(size, size)
    val drawable = requireNotNull(AppCompatResources.getDrawable(context, R.drawable.ic_land_location)).mutate()
    // Tint the black template while preserving its white center.
    drawable.colorFilter = LightingColorFilter(color xor Color.WHITE, color)
    drawable.setBounds(0, 0, size, size)
    drawable.draw(Canvas(bitmap))
    return OverlayImage.fromBitmap(bitmap)
}

private fun createClusterIcon(
    label: Int,
    sizeDp: Int,
    density: Density,
    fillColor: Int,
    ringColor: Int
): OverlayImage {
    val size = (sizeDp * density.density).roundToInt().coerceAtLeast(1)
    val bitmap = createBitmap(size, size)
    val canvas = Canvas(bitmap)
    val center = size / 2f
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.color = ringColor
    canvas.drawCircle(center, center, center, paint)
    paint.color = fillColor
    val ringWidth = when (sizeDp) {
        24 -> 4f
        34, 38 -> 6f
        else -> 8f
    } * density.density
    canvas.drawCircle(center, center, center - ringWidth, paint)
    paint.color = Color.WHITE
    paint.typeface = Typeface.DEFAULT_BOLD
    paint.textSize = when (sizeDp) {
        24 -> 10f
        34 -> 12f
        38 -> 13f
        else -> 14f
    } * density.density * density.fontScale
    paint.textAlign = Paint.Align.CENTER
    val baseline = center - (paint.ascent() + paint.descent()) / 2f
    canvas.drawText(label.toString(), center, baseline, paint)
    return OverlayImage.fromBitmap(bitmap)
}
