package `in`.koreatech.business.util.ext

import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Modifier.clickableOnce(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    }
){
    val timer = rememberCoroutineScope()
    var duplicated by remember { mutableStateOf(false) }

    Modifier.clickable(
        enabled = enabled && !duplicated,
        onClickLabel = onClickLabel,
        role = role,
    ){
        duplicated = true
        onClick()

        timer.launch {
            delay(100)
            duplicated = false
        }
    }
}