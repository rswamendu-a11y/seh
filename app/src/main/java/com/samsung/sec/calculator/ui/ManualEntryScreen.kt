package com.samsung.sec.calculator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.samsung.sec.calculator.data.ManualModuleInputs

@Composable
fun ManualEntryScreen(
    currentInputs: ManualModuleInputs,
    onInputsChanged: (ManualModuleInputs) -> Unit,
    onCalculateClicked: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState)) {
        Text("Samsung Incentive Entry", style = MaterialTheme.typography.headlineMedium)

        // Care+ Section
        Text("Module D: Care+", style = MaterialTheme.typography.titleMedium)
        NumberInputRow("Dev > 1L", currentInputs.countCarePlusStandard) { onInputsChanged(currentInputs.copy(countCarePlusStandard = it)) }
        NumberInputRow("ProtectMax", currentInputs.countCarePlusPromax) { onInputsChanged(currentInputs.copy(countCarePlusPromax = it)) }

        // Accessories Section
        Text("Module E: Accessories", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = currentInputs.totalAccessoryValue.toString(),
            onValueChange = { onInputsChanged(currentInputs.copy(totalAccessoryValue = it.toDoubleOrNull() ?: 0.0)) },
            label = { Text("Total Acc Value") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // Bundles Section
        Text("Module F: Bundles", style = MaterialTheme.typography.titleMedium)
        CounterRow("Host + Watch", currentInputs.countHostWatch) { onInputsChanged(currentInputs.copy(countHostWatch = it)) }
        CounterRow("Host + Buds", currentInputs.countHostBuds) { onInputsChanged(currentInputs.copy(countHostBuds = it)) }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onCalculateClicked, modifier = Modifier.fillMaxWidth()) {
            Text("Calculate")
        }
    }
}

@Composable
fun NumberInputRow(label: String, value: Int, onValueChange: (Int) -> Unit) {
    OutlinedTextField(
        value = value.toString(),
        onValueChange = { onValueChange(it.toIntOrNull() ?: 0) },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun CounterRow(label: String, count: Int, onCountChange: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.weight(1f))
        IconButton(onClick = { if (count > 0) onCountChange(count - 1) }) { Icon(Icons.Default.Remove, "") }
        Text(count.toString())
        IconButton(onClick = { onCountChange(count + 1) }) { Icon(Icons.Default.Add, "") }
    }
}
