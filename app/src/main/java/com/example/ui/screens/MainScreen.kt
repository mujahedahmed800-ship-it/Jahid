package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.local.FavoriteItemEntity
import com.example.data.local.HistoryItemEntity
import com.example.data.static.TenseCategory
import com.example.data.static.VocabularyWord
import com.example.ui.AppTab
import com.example.ui.TranslatorUiState

private data class NavDestinationItem(
    val tab: AppTab,
    val labelResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val navItems = listOf(
    NavDestinationItem(
        tab = AppTab.HOME,
        labelResId = R.string.nav_home,
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    NavDestinationItem(
        tab = AppTab.VOCABULARY,
        labelResId = R.string.nav_vocabulary,
        selectedIcon = Icons.Rounded.MenuBook,
        unselectedIcon = Icons.Outlined.MenuBook
    ),
    NavDestinationItem(
        tab = AppTab.TENSE,
        labelResId = R.string.nav_tense,
        selectedIcon = Icons.Rounded.AutoStories,
        unselectedIcon = Icons.Outlined.AutoStories
    ),
    NavDestinationItem(
        tab = AppTab.HISTORY,
        labelResId = R.string.nav_history,
        selectedIcon = Icons.Rounded.History,
        unselectedIcon = Icons.Outlined.History
    ),
    NavDestinationItem(
        tab = AppTab.FAVORITES,
        labelResId = R.string.nav_favorites,
        selectedIcon = Icons.Rounded.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    )
)

@Composable
fun MainScreen(
    uiState: TranslatorUiState,
    filteredWords: List<VocabularyWord>,
    history: List<HistoryItemEntity>,
    favorites: List<FavoriteItemEntity>,
    snackbarHostState: SnackbarHostState,
    onSelectTab: (AppTab) -> Unit,
    onSourceTextChanged: (String) -> Unit,
    onSwapLanguages: () -> Unit,
    onSelectLanguageSide: (Boolean) -> Unit,
    onTranslate: () -> Unit,
    onClear: () -> Unit,
    onVoiceInput: () -> Unit,
    onCopyText: (String) -> Unit,
    onSpeakText: (String, String) -> Unit,
    onShareTranslation: (String, String) -> Unit,
    onSaveFavorite: () -> Unit,
    onWordSearchQueryChanged: (String) -> Unit,
    onSelectTenseCategory: (TenseCategory?) -> Unit,
    onSelectTense: (Int) -> Unit,
    onClearHistory: () -> Unit,
    onDeleteHistoryItem: (Long) -> Unit,
    onReuseHistoryItem: (HistoryItemEntity) -> Unit,
    onDeleteFavorite: (Long) -> Unit,
    onReuseFavoriteItem: (FavoriteItemEntity) -> Unit
) {
    BackHandler(enabled = uiState.selectedTab != AppTab.HOME) {
        onSelectTab(AppTab.HOME)
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useNavigationRail = maxWidth >= 600.dp

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                if (!useNavigationRail) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        windowInsets = WindowInsets.navigationBars
                    ) {
                        navItems.forEach { item ->
                            val selected = uiState.selectedTab == item.tab
                            val label = stringResource(item.labelResId)
                            NavigationBarItem(
                                selected = selected,
                                onClick = { onSelectTab(item.tab) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = label
                                    )
                                },
                                label = {
                                    Text(
                                        text = label,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag("nav_${item.tab.route}")
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (useNavigationRail) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        navItems.forEach { item ->
                            val selected = uiState.selectedTab == item.tab
                            val label = stringResource(item.labelResId)
                            NavigationRailItem(
                                selected = selected,
                                onClick = { onSelectTab(item.tab) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = label
                                    )
                                },
                                label = {
                                    Text(
                                        text = label,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag("nav_${item.tab.route}")
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    val contentModifier = Modifier
                        .widthIn(max = 840.dp)
                        .fillMaxSize()

                    when (uiState.selectedTab) {
                        AppTab.HOME -> HomeTranslateScreen(
                            uiState = uiState,
                            onSourceTextChanged = onSourceTextChanged,
                            onSwapLanguages = onSwapLanguages,
                            onSelectLanguageSide = onSelectLanguageSide,
                            onTranslate = onTranslate,
                            onClear = onClear,
                            onVoiceInput = onVoiceInput,
                            onCopyResult = onCopyText,
                            onSpeakResult = onSpeakText,
                            onShareResult = onShareTranslation,
                            onSaveFavorite = onSaveFavorite,
                            modifier = contentModifier
                        )
                        AppTab.VOCABULARY -> VocabularyScreen(
                            searchQuery = uiState.wordSearchQuery,
                            words = filteredWords,
                            onSearchQueryChanged = onWordSearchQueryChanged,
                            onSpeakWord = { word -> onSpeakText(word, "en-US") },
                            modifier = contentModifier
                        )
                        AppTab.TENSE -> TenseScreen(
                            selectedCategory = uiState.selectedTenseCategory,
                            selectedTenseIndex = uiState.selectedTenseIndex,
                            onSelectCategory = onSelectTenseCategory,
                            onSelectTense = onSelectTense,
                            onSpeakExample = { example -> onSpeakText(example, "en-US") },
                            modifier = contentModifier
                        )
                        AppTab.HISTORY -> HistoryScreen(
                            history = history,
                            onClearHistory = onClearHistory,
                            onDeleteItem = onDeleteHistoryItem,
                            onSpeakItem = onSpeakText,
                            onReuseItem = onReuseHistoryItem,
                            modifier = contentModifier
                        )
                        AppTab.FAVORITES -> FavoritesScreen(
                            favorites = favorites,
                            onDeleteFavorite = onDeleteFavorite,
                            onCopyText = onCopyText,
                            onSpeakItem = onSpeakText,
                            onReuseItem = onReuseFavoriteItem,
                            modifier = contentModifier
                        )
                    }
                }
            }
        }
    }
}
