package com.example.lab08ejercicio1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room

import com.example.lab08ejercicio1.TaskDatabase
import com.example.lab08ejercicio1.viewmodel.TaskViewModel
import com.example.lab08ejercicio1.ui.theme.Lab08Ejercicio1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar la Base de Datos
        val db = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "task_db"
        ).build()

        val taskDao = db.taskDao()
        val viewModel = TaskViewModel(taskDao)

        setContent {
            Lab08Ejercicio1Theme {
                TaskScreen(viewModel)
            }
        }
    }
}