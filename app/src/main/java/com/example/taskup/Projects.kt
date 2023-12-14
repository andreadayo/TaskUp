package com.example.taskup

data class Project(
    val projectId: Int,
    val projectTitle: String,
    val projectStatus: String,
    val userId: Int?
)
