package com.bruna.stardust.ui.translation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bruna.stardust.data.translation.ExplanationTranslator

class TranslationViewModel : ViewModel() {

    private val translator = ExplanationTranslator()
    private val cache = mutableStateMapOf<String, String>()
    private val pending = mutableSetOf<String>()

    var hasError by mutableStateOf(false)
        private set

    var retryTick by mutableIntStateOf(0)
        private set

    fun translatedOrNull(text: String): String? = cache[text]

    fun request(text: String) {
        if (text in cache || text in pending) return
        pending.add(text)
        translator.translate(
            text = text,
            onSuccess = { result ->
                cache[text] = result
                pending.remove(text)
                hasError = false
            },
            onError = {
                pending.remove(text)
                hasError = true
            }
        )
    }

    fun retry() {
        hasError = false
        retryTick++
    }

    override fun onCleared() {
        translator.close()
    }
}