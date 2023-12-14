package com.example.taskup

data class Task(
    val taskId: Int,
    val taskTitle: String,
    val taskDue: String,
    val taskTime: String,
    val taskDesc: String,
    val taskStatus: String,
    val taskPriority: String,
    val projectId: Int
)
