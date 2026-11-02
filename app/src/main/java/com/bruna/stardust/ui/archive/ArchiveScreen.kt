package com.bruna.stardust.ui.archive

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bruna.stardust.ui.today.ApodContent
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

private val firstApodDate = LocalDate.of(1995, 6, 16)

@Composable
fun ArchiveScreen(
    modifier: Modifier = Modifier,
    viewModel: ArchiveViewModel = viewModel()
) {
    var showPicker by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        Button(
            onClick = { showPicker = true },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Escolher data")
        }

        when (val state = viewModel.uiState) {
            is ArchiveUiState.Idle ->
                Text(
                    "Escolha uma data entre 16/06/1995 e hoje.",
                    modifier = Modifier.padding(16.dp)
                )

            is ArchiveUiState.Loading ->
                Text("Carregando...", modifier = Modifier.padding(16.dp))

            is ArchiveUiState.Success ->
                ApodContent(item = state.item, modifier = Modifier.weight(1f))

            is ArchiveUiState.NotFound ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Não há fotografia para esta data.")
                    Button(onClick = { showPicker = true }) {
                        Text("Tentar outra data")
                    }
                }

            is ArchiveUiState.Error ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(state.message)
                    Button(onClick = viewModel::retry) {
                        Text("Tentar de novo")
                    }
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