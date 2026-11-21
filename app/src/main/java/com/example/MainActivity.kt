package com.example

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.TranslatorViewModel
import com.example.ui.screens.MainScreen
import com.example.ui.theme.JahidulTranslateTheme
import com.example.util.SpeechManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JahidulTranslateTheme {
                val context = LocalContext.current
                val viewModel: TranslatorViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val filteredWords by viewModel.filteredWords.collectAsStateWithLifecycle()
                val history by viewModel.history.collectAsStateWithLifecycle()
                val favorites by viewModel.favorites.collectAsStateWithLifecycle()

                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()
                val speechManager = remember(context) { SpeechManager(context) }

                DisposableEffect(speechManager) {
                    onDispose { speechManager.shutdown() }
                }

                LaunchedEffect(viewModel) {
                    viewModel.snackbarEvents.collect { resId ->
                        snackbarHostState.showSnackbar(context.getString(resId))
                    }
                }

                val voiceLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val matches = result.data
                            ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        val spoken = matches?.firstOrNull()?.trim()
                        if (!spoken.isNullOrEmpty()) {
                            viewModel.onVoiceInputResult(spoken)
                        } else {
                            viewModel.notifyMessage(R.string.msg_voice_failed)
                        }
                    }
                }

                fun showSnackbar(resId: Int) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(context.getString(resId))
                    }
                }

                fun speakText(text: String, languageTag: String) {
                    if (text.isBlank()) {
                        showSnackbar(R.string.msg_translate_first)
                        return
                    }
                    speechManager.speak(text, languageTag) {
                        showSnackbar(R.string.msg_tts_unavailable)
                    }
                }

                fun copyText(text: String) {
                    if (text.isBlank()) {
                        showSnackbar(R.string.msg_translate_first)
                        return
                    }
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(
                        ClipData.newPlainText(context.getString(R.string.app_name), text)
                    )
                    showSnackbar(R.string.msg_copied)
                }

                fun shareTranslation(source: String, translated: String) {
                    if (translated.isBlank()) {
                        showSnackbar(R.string.msg_translate_first)
                        return
                    }
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
                        putExtra(Intent.EXTRA_TEXT, "$source\n\n$translated")
                    }
                    context.startActivity(
                        Intent.createChooser(shareIntent, context.getString(R.string.btn_share))
                    )
                }

                fun launchVoiceInput() {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(
                            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                        )
                        putExtra(
                            RecognizerIntent.EXTRA_LANGUAGE,
                            uiState.direction.sourceSpeechTag()
                        )
                    }
                    if (intent.resolveActivity(context.packageManager) != null) {
                        voiceLauncher.launch(intent)
                    } else {
                        showSnackbar(R.string.msg_voice_unavailable)
                    }
                }

                MainScreen(
                    uiState = uiState,
                    filteredWords = filteredWords,
                    history = history,
                    favorites = favorites,
                    snackbarHostState = snackbarHostState,
                    onSelectTab = viewModel::selectTab,
                    onSourceTextChanged = viewModel::onSourceTextChanged,
                    onSwapLanguages = viewModel::swapLanguages,
                    onSelectLanguageSide = viewModel::selectLanguageSide,
                    onTranslate = viewModel::translate,
                    onClear = viewModel::clearTranslation,
                    onVoiceInput = ::launchVoiceInput,
                    onCopyText = ::copyText,
                    onSpeakText = ::speakText,
                    onShareTranslation = ::shareTranslation,
                    onSaveFavorite = viewModel::saveCurrentToFavorite,
                    onWordSearchQueryChanged = viewModel::onWordSearchQueryChanged,
                    onSelectTenseCategory = viewModel::selectTenseCategory,
                    onSelectTense = viewModel::selectTense,
                    onClearHistory = viewModel::clearHistory,
                    onDeleteHistoryItem = viewModel::removeHistoryItem,
                    onReuseHistoryItem = { item ->
                        viewModel.loadTranslationIntoEditor(
                            sourceText = item.sourceText,
                            translatedText = item.translatedText,
                            directionCode = item.direction
                        )
                    },
                    onDeleteFavorite = viewModel::removeFavorite,
                    onReuseFavoriteItem = { item ->
                        viewModel.loadTranslationIntoEditor(
                            sourceText = item.sourceText,
                            translatedText = item.translatedText,
                            directionCode = item.direction
                        )
                    }
                )
            }
        }
    }
}
