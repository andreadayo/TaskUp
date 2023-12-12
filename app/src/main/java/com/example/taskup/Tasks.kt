package com.example.taskup

data class Task(
    val taskId: Int,
    val title: String,
    val dueDate: String,
    val time: String,
    val description: String,
    val status: String,
    val priority: String,
    val projectId: Int
)
