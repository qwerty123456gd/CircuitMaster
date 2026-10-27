package com.example.circuitmaster.data.model

sealed interface Question {
    val id: String
    val subchapterId: String
    val text: String
    val explanation: String
}

data class ChoiceQuestion(
    override val id: String,
    override val subchapterId: String,
    override val text: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    override val explanation: String
) : Question

data class CalculationQuestion(
    override val id: String,
    override val subchapterId: String,
    override val text: String,
    val targetValue: Double,
    val tolerance: Double,
    override val explanation: String
) : Question
//для тестов и задач