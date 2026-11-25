package com.example.thrive2

import org.junit.Assert.assertEquals
import org.junit.Test

class FinancialCalculationsTest {
    @Test
    fun bankExpenseReducesBankBalance() {
        val result = applyTransactionToBalances(1000.0, 200.0, transaction("Expense"))
        assertEquals(900.0, result.first, 0.001)
        assertEquals(200.0, result.second, 0.001)
    }

    @Test
    fun cashExpenseReducesCashBalance() {
        val result = applyTransactionToBalances(1000.0, 200.0, transaction("Expense", isCash = true))
        assertEquals(1000.0, result.first, 0.001)
        assertEquals(150.0, result.second, 0.001)
    }

    @Test
    fun bankIncomeIncreasesBankBalance() {
        val result = applyTransactionToBalances(1000.0, 200.0, transaction("Income"))
        assertEquals(1050.0, result.first, 0.001)
        assertEquals(200.0, result.second, 0.001)
    }

    @Test
    fun cashIncomeIncreasesCashBalance() {
        val result = applyTransactionToBalances(1000.0, 200.0, transaction("Income", isCash = true))
        assertEquals(1000.0, result.first, 0.001)
        assertEquals(250.0, result.second, 0.001)
    }

    @Test
    fun bankTransferDoesNotReduceBankBalanceUnderExistingRules() {
        val result = applyTransactionToBalances(1000.0, 200.0, transaction("Expense", isTransfer = true))
        assertEquals(1000.0, result.first, 0.001)
        assertEquals(200.0, result.second, 0.001)
    }

    private fun transaction(type: String, isCash: Boolean = false, isTransfer: Boolean = false) =
        Transaction(1, "Test", 50.0, type, "Test", "Account", "Today", isCash, isTransfer)
}
