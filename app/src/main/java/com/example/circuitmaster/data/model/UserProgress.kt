package com.example.circuitmaster.data.model

data class UserProgress(
    val subchapterId: String,
    val isCompleted: Boolean,
    val score: Int
)
//трекер прогресса