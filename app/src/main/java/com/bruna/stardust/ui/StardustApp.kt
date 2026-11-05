package com.bruna.stardust.ui

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bruna.stardust.ui.archive.ArchiveScreen
import com.bruna.stardust.ui.favorites.FavoritesScreen
import com.bruna.stardust.ui.navigation.AppTab
import com.bruna.stardust.ui.settings.LanguageDialog
import com.bruna.stardust.ui.settings.SettingsViewModel
import com.bruna.stardust.ui.today.TodayScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StardustApp() {
    var selectedTab by rememberSaveable { mutableStateOf(AppTab.Today) }
    var selectedFavoriteDate by rememberSaveable { mutableStateOf<String?>(null) }
    val settingsViewModel: SettingsViewModel = viewModel()
    var showLanguageDialog by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Stardust", style = MaterialTheme.typography.headlineSmall) },
                actions = {
                    IconButton(onClick = { showLanguageDialog = true }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                ),
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            NavigationBar {
                AppTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = tab == selectedTab,
                        onClick = {
                            selectedTab = tab
                            if (tab == AppTab.Favorites) selectedFavoriteDate = null
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding)

        when (selectedTab) {
            AppTab.Today -> TodayScreen(modifier = contentModifier)
            AppTab.Archive -> ArchiveScreen(modifier = contentModifier)
            AppTab.Favorites -> FavoritesScreen(
                selectedDate = selectedFavoriteDate,
                onSelectedDateChange = { selectedFavoriteDate = it },
                modifier = contentModifier
            )
        }
    }

    if (showLanguageDialog) {
        LanguageDialog(
            current = settingsViewModel.language,
            onSelect = {
                settingsViewModel.selectLanguage(it)
                showLanguageDialog = false
            },
            onDismiss = { showLanguageDialog = false }
        )
    }
}