package com.example.thrive2

// Shared data models for Thrive. Kept separate from UI entry-point code.

data class Transaction(
    val id: Int,
    val description: String,
    val amount: Double,
    val type: String,
    val category: String,
    val account: String,
    val date: String,
    val isCash: Boolean = false,
    val isTransfer: Boolean = false,
    val isCategorized: Boolean = true
): java.io.Serializable

data class TransactionSplit(
    val category: String,
    val amount: Double
): java.io.Serializable

data class FinancialGoal(
    val id: Int,
    val title: String,
    val target: Double,
    val current: Double
): java.io.Serializable

data class Goal(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val trackingType: String,
    val target: Double = 0.0,
    val current: Double = 0.0,
    val milestones: List<String> = emptyList(),
    val completedMilestones: List<Boolean> = emptyList(),
    val status: String = "In Progress"
): java.io.Serializable

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val priority: String,
    val relatedGoal: String = "",
    val completed: Boolean = false
): java.io.Serializable

data class Reflection(
    val id: Int,
    val date: String,
    val mood: String,
    val text: String
): java.io.Serializable
