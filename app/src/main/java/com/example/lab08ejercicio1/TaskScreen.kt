package com.example.lab08ejercicio1

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add

import com.example.lab08ejercicio1.Task
import com.example.lab08ejercicio1.viewmodel.TaskViewModel
import com.example.lab08ejercicio1.viewmodel.TaskFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    val currentFilter by viewModel.filter.collectAsState()
    var newTaskDescription by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }

    // Implementación de Scaffold
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi APP Para  Gestionar mis tareas") },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Menú de opciones")
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("Eliminar todas las tareas") },
                            onClick = {
                                viewModel.deleteAllTasks()
                                showMenu = false
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Botones de Filtro
            FilterBar(currentFilter = currentFilter, onFilterChange = viewModel::setFilter)

            // Lista de Tareas
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                items(tasks, key = { it.id }) { task ->
                    TaskItemRow(task = task, viewModel = viewModel)
                    Divider()
                }
            }

            // Campo de Añadir Tarea
            AddOrEditTaskField(
                description = newTaskDescription,
                onDescriptionChange = { newTaskDescription = it },
                onAddClick = {
                    if (newTaskDescription.isNotEmpty()) {
                        viewModel.addTask(newTaskDescription)
                        newTaskDescription = ""
                    }
                }
            )
        }
    }
}

// --- Componentes Auxiliares ---

@Composable
fun FilterBar(currentFilter: TaskFilter, onFilterChange: (TaskFilter) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TaskFilter.entries.forEach { filter ->
            FilterButton(
                text = filter.displayName,
                isSelected = currentFilter == filter,
                onClick = { onFilterChange(filter) }
            )
        }
    }
}

@Composable
fun FilterButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray
        )
    ) {
        Text(text)
    }
}

@Composable
fun TaskItemRow(task: Task, viewModel: TaskViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { viewModel.toggleTaskCompletion(task) }
        )

        Text(
            text = task.description,
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            style = if (task.isCompleted) {
                TextStyle(textDecoration = TextDecoration.LineThrough, color = Color.Gray)
            } else {
                TextStyle.Default
            }
        )

        // Botón para ELIMINAR INDIVIDUALMENTE
        IconButton(onClick = { viewModel.deleteTask(task) }) {
            Icon(Icons.Filled.Delete, contentDescription = "Eliminar tarea")
        }
    }
}
@Composable
fun AddOrEditTaskField(
    description: String,
    onDescriptionChange: (String) -> Unit,
    onAddClick: () -> Unit
) {
    val isEnabled = description.isNotEmpty() // Variable de control

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Nueva tarea...") },
            modifier = Modifier.weight(1f),
            singleLine = true
        )
        Spacer(Modifier.width(8.dp))


        FloatingActionButton(
            onClick = { if (isEnabled) onAddClick() },
            containerColor = if (isEnabled) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.6f)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar")
        }
    }
}