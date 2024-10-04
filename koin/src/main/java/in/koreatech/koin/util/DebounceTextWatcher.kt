package `in`.koreatech.koin.util

import android.text.Editable
import android.text.TextWatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebounceTextWatcher(
    val scope: CoroutineScope,
    val debounceTime: Long = 500L,
    val onTextChangeCallback: (String) -> Unit
) : TextWatcher {

    private var job: Job? = null
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        job?.takeIf { it.isActive }?.cancel()
        job = scope.launch {
            delay(debounceTime)
            onTextChangeCallback(s.toString())
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable?) {}
}