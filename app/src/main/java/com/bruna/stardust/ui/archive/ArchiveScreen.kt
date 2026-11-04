package com.bruna.stardust.ui.archive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bruna.stardust.ui.components.LoadingState
import com.bruna.stardust.ui.components.MessageState
import com.bruna.stardust.ui.favorites.FavoritesViewModel
import com.bruna.stardust.ui.today.ApodContent
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

private val firstApodDate = LocalDate.of(1995, 6, 16)

@Composable
fun ArchiveScreen(
    modifier: Modifier = Modifier,
    viewModel: ArchiveViewModel = viewModel(),
    favoritesViewModel: FavoritesViewModel = viewModel()
) {
    var showPicker by rememberSaveable { mutableStateOf(false) }
    val favoriteDates by favoritesViewModel.favoriteDates.collectAsState()
    val state = viewModel.uiState

    if (state is ArchiveUiState.Idle) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Escolha uma data entre 16/06/1995 e hoje.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                PickDateButton(onClick = { showPicker = true })
            }
        }
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            PickDateButton(
                onClick = { showPicker = true },
                modifier = Modifier.padding(16.dp)
            )

            when (state) {
                is ArchiveUiState.Idle -> Unit

                is ArchiveUiState.Loading -> LoadingState(modifier = Modifier.weight(1f))

                is ArchiveUiState.Success -> ApodContent(
                    item = state.item,
                    isFavorite = state.item.date in favoriteDates,
                    onToggleFavorite = { favoritesViewModel.toggleFavorite(state.item) },
                    modifier = Modifier.weight(1f)
                )

                is ArchiveUiState.NotFound -> MessageState(
                    message = "Não há fotografia para esta data.",
                    modifier = Modifier.weight(1f)
                )

                is ArchiveUiState.Error -> MessageState(
                    message = state.message,
                    actionLabel = "Tentar de novo",
                    onAction = viewModel::retry,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    if (showPicker) {
        ArchiveDatePickerDialog(
            onDismiss = { showPicker = false },
            onDateChosen = { date ->
                showPicker = false
                viewModel.loadDay(date)
            }
        )
    }
}

@Composable
private fun PickDateButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
        Text("Escolher data")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArchiveDatePickerDialog(
    onDismiss: () -> Unit,
    onDateChosen: (LocalDate) -> Unit
) {
    val today = LocalDate.now()
    val minMillis = firstApodDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    val maxMillis = today.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

    val pickerState = rememberDatePickerState(
        yearRange = firstApodDate.year..today.year,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                utcTimeMillis in minMillis..maxMillis
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = pickerState.selectedDateMillis != null,
                onClick = {
                    pickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()
                        onDateChosen(date)
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    ) {
        DatePicker(state = pickerState)
    }
}