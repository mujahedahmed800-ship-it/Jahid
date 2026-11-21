package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.local.AppDatabase
import com.example.data.local.FavoriteItemEntity
import com.example.data.local.HistoryItemEntity
import com.example.data.remote.MyMemoryApi
import com.example.data.repository.TranslationOutcome
import com.example.data.repository.TranslatorRepository
import com.example.data.static.LearningContent
import com.example.data.static.TenseCategory
import com.example.data.static.TenseLesson
import com.example.data.static.VocabularyWord
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppTab(val route: String) {
    HOME("home"),
    VOCABULARY("vocabulary"),
    TENSE("tense"),
    HISTORY("history"),
    FAVORITES("favorites")
}

enum class TranslationDirection(val pairCode: String) {
    EN_TO_BN("en|bn"),
    BN_TO_EN("bn|en");

    fun sourceSpeechTag(): String = if (this == EN_TO_BN) "en-US" else "bn-BD"
    fun targetSpeechTag(): String = if (this == EN_TO_BN) "bn-BD" else "en-US"
    fun badgeLabel(): String = if (this == EN_TO_BN) "EN → BN" else "BN → EN"

    companion object {
        fun fromCode(code: String): TranslationDirection =
            if (code == BN_TO_EN.pairCode) BN_TO_EN else EN_TO_BN
    }
}

sealed interface TranslationResultUiState {
    data class Idle(val hintResId: Int) : TranslationResultUiState
    data object Loading : TranslationResultUiState
    data class Success(
        val sourceText: String,
        val translatedText: String,
        val direction: TranslationDirection
    ) : TranslationResultUiState
    data class Error(val messageResId: Int) : TranslationResultUiState
}

data class TranslatorUiState(
    val selectedTab: AppTab = AppTab.HOME,
    val direction: TranslationDirection = TranslationDirection.EN_TO_BN,
    val sourceText: String = "",
    val resultState: TranslationResultUiState = TranslationResultUiState.Idle(R.string.result_idle_hint),
    val wordSearchQuery: String = "",
    val selectedTenseCategory: TenseCategory? = null,
    val selectedTenseIndex: Int = 0
)

class TranslatorViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TranslatorRepository
    private var translateJob: Job? = null

    init {
        val db = AppDatabase.getInstance(application)
        repository = TranslatorRepository(
            api = MyMemoryApi.create(),
            historyDao = db.historyDao(),
            favoriteDao = db.favoriteDao()
        )
    }

    private val _uiState = MutableStateFlow(TranslatorUiState())
    val uiState: StateFlow<TranslatorUiState> = _uiState.asStateFlow()

    private val _snackbarEvents = MutableSharedFlow<Int>()
    val snackbarEvents: SharedFlow<Int> = _snackbarEvents.asSharedFlow()

    val history: StateFlow<List<HistoryItemEntity>> = repository.history.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favorites: StateFlow<List<FavoriteItemEntity>> = repository.favorites.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val filteredWords: StateFlow<List<VocabularyWord>> = combine(
        _uiState
    ) { states ->
        val query = states[0].wordSearchQuery.trim()
        if (query.isEmpty()) {
            LearningContent.vocabularyWords
        } else {
            LearningContent.vocabularyWords.filter { word ->
                word.english.contains(query, ignoreCase = true) ||
                    word.bangla.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LearningContent.vocabularyWords
    )

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun onSourceTextChanged(text: String) {
        _uiState.update { it.copy(sourceText = text) }
    }

    fun onVoiceInputResult(spokenText: String) {
        _uiState.update { it.copy(sourceText = spokenText) }
    }

    fun swapLanguages() {
        translateJob?.cancel()
        _uiState.update { current ->
            val nextDirection = if (current.direction == TranslationDirection.EN_TO_BN) {
                TranslationDirection.BN_TO_EN
            } else {
                TranslationDirection.EN_TO_BN
            }
            current.copy(
                direction = nextDirection,
                resultState = TranslationResultUiState.Idle(R.string.result_language_changed)
            )
        }
    }

    fun selectLanguageSide(isLeftSide: Boolean) {
        translateJob?.cancel()
        _uiState.update { current ->
            val englishFirst = current.direction == TranslationDirection.EN_TO_BN
            val nextDirection = if (isLeftSide) {
                if (englishFirst) TranslationDirection.EN_TO_BN else TranslationDirection.BN_TO_EN
            } else {
                if (englishFirst) TranslationDirection.BN_TO_EN else TranslationDirection.EN_TO_BN
            }
            if (nextDirection == current.direction) {
                current
            } else {
                current.copy(
                    direction = nextDirection,
                    resultState = TranslationResultUiState.Idle(R.string.result_language_changed)
                )
            }
        }
    }

    fun translate() {
        val currentState = _uiState.value
        val trimmedText = currentState.sourceText.trim()
        if (trimmedText.isEmpty()) {
            emitSnackbar(R.string.msg_enter_text)
            return
        }

        val activeDirection = currentState.direction
        translateJob?.cancel()
        translateJob = viewModelScope.launch {
            _uiState.update { it.copy(resultState = TranslationResultUiState.Loading) }
            val outcome = repository.translate(trimmedText, activeDirection.pairCode)
            // Ignore stale result if direction or text changed while loading
            val latestState = _uiState.value
            if (latestState.direction != activeDirection || latestState.sourceText.trim() != trimmedText) {
                return@launch
            }

            val nextResult = when (outcome) {
                is TranslationOutcome.Success -> TranslationResultUiState.Success(
                    sourceText = trimmedText,
                    translatedText = outcome.translatedText,
                    direction = activeDirection
                )
                TranslationOutcome.NotFound -> TranslationResultUiState.Error(R.string.result_not_found)
                TranslationOutcome.NetworkError -> TranslationResultUiState.Error(R.string.result_network_error)
            }
            _uiState.update { it.copy(resultState = nextResult) }
        }
    }

    fun clearTranslation() {
        translateJob?.cancel()
        _uiState.update {
            it.copy(
                sourceText = "",
                resultState = TranslationResultUiState.Idle(R.string.result_idle_hint)
            )
        }
    }

    fun saveCurrentToFavorite() {
        val result = _uiState.value.resultState
        if (result !is TranslationResultUiState.Success) {
            emitSnackbar(R.string.msg_translate_first)
            return
        }
        viewModelScope.launch {
            val added = repository.saveFavorite(
                sourceText = result.sourceText,
                translatedText = result.translatedText,
                direction = result.direction.pairCode
            )
            emitSnackbar(if (added) R.string.msg_saved_favorite else R.string.msg_already_favorite)
        }
    }

    fun removeFavorite(id: Long) {
        viewModelScope.launch {
            repository.removeFavorite(id)
        }
    }

    fun removeHistoryItem(id: Long) {
        viewModelScope.launch {
            repository.removeHistoryItem(id)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    fun loadTranslationIntoEditor(sourceText: String, translatedText: String, directionCode: String) {
        val direction = TranslationDirection.fromCode(directionCode)
        _uiState.update {
            it.copy(
                selectedTab = AppTab.HOME,
                direction = direction,
                sourceText = sourceText,
                resultState = TranslationResultUiState.Success(
                    sourceText = sourceText,
                    translatedText = translatedText,
                    direction = direction
                )
            )
        }
    }

    fun onWordSearchQueryChanged(query: String) {
        _uiState.update { it.copy(wordSearchQuery = query) }
    }

    fun selectTenseCategory(category: TenseCategory?) {
        _uiState.update { current ->
            val firstMatchingIndex = if (category == null) {
                current.selectedTenseIndex
            } else {
                LearningContent.tenses.indexOfFirst { it.category == category }.takeIf { it >= 0 } ?: 0
            }
            current.copy(
                selectedTenseCategory = category,
                selectedTenseIndex = firstMatchingIndex
            )
        }
    }

    fun selectTense(index: Int) {
        if (index in LearningContent.tenses.indices) {
            _uiState.update { it.copy(selectedTenseIndex = index) }
        }
    }

    fun currentTense(): TenseLesson {
        val idx = _uiState.value.selectedTenseIndex
        return LearningContent.tenses.getOrElse(idx) { LearningContent.tenses.first() }
    }

    fun notifyMessage(resId: Int) {
        emitSnackbar(resId)
    }

    private fun emitSnackbar(resId: Int) {
        viewModelScope.launch {
            _snackbarEvents.emit(resId)
        }
    }
}
