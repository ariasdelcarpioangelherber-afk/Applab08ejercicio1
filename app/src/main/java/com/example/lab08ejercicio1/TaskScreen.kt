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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.BorderStroke


import androidx.compose.foundation.background
import androidx.compose.foundation.text.BasicTextField

import androidx.compose.material3.SmallFloatingActionButton
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
    OutlinedButton( // Usamos OutlinedButton para un aspecto más limpio
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            // El color de contenido es el primario si está seleccionado, de lo contrario, el color por defecto.
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            // El color del contenedor (fondo) es el primario si está seleccionado, de lo contrario, transparente.
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        ),
        border = BorderStroke( // Borde más sutil
            1.dp,
            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        ),
        // Relleno horizontal más reducido para que se vea más compacto
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text)
    }
}

@Composable
fun TaskItemRow(task: Task, viewModel: TaskViewModel) {
    // Usamos Card para darle una ligera elevación y bordes redondeados
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = { viewModel.toggleTaskCompletion(task) }
    ) {

        ListItem(

            headlineContent = {
                Text(
                    text = task.description,
                    style = if (task.isCompleted) {
                        TextStyle(textDecoration = TextDecoration.LineThrough, color = Color.Gray)
                    } else {
                        TextStyle.Default
                    }
                )
            },

            leadingContent = {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { viewModel.toggleTaskCompletion(task) }
                )
            },

            trailingContent = {
                IconButton(onClick = { viewModel.deleteTask(task) }) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Eliminar tarea",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            colors = ListItemDefaults.colors(containerColor = Color.White)
        )
    }
}
@Composable
fun AddOrEditTaskField(
    description: String,
    onDescriptionChange: (String) -> Unit,
    onAddClick: () -> Unit
) {
    val isEnabled = description.isNotEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerLow, shape = MaterialTheme.shapes.extraLarge)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        BasicTextField(
            value = description,
            onValueChange = onDescriptionChange,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (description.isEmpty()) {
                    Text("Nueva tarea...", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                innerTextField()
            }
        )


        SmallFloatingActionButton(
            onClick = { if (isEnabled) onAddClick() },
            containerColor = if (isEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Agregar",
                tint = if (isEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}