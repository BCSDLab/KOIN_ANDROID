package `in`.koreatech.koin.feature.lostandfound.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val AUTO_SLIDE_UP_TIME = 5000L

@Composable
fun SlideUpText(
    textList: ImmutableList<String>,
    modifier: Modifier = Modifier,
    initIndex: Int = 0,
    style: TextStyle = TextStyle.Default
) {
    if (textList.isEmpty()) return
    val currentIndex = remember(initIndex) { mutableIntStateOf(initIndex) }
    val text = remember(textList) { mutableStateOf(textList[initIndex]) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(currentIndex.intValue) {
        coroutineScope.launch {
            delay(AUTO_SLIDE_UP_TIME)
            currentIndex.intValue = (currentIndex.intValue + 1) % textList.size
            text.value = textList[currentIndex.intValue]
        }
    }

    AnimatedContent(
        targetState = text.value,
        transitionSpec = {
            slideInVertically { height -> height } + fadeIn() togetherWith
                slideOutVertically { height -> -height } + fadeOut()
        }
    ) { showedText ->
        Text(
            modifier = modifier,
            text = showedText,
            style = style
        )
    }
}
