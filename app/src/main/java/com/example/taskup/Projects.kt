package com.example.taskup

data class Project(
    val projectId: Int,
    val title: String,
    val status: String,
    val userId: Int?
)
