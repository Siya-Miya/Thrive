package com.example.thrive2

import org.junit.Assert.assertEquals
import org.junit.Test

class FinancialCalculationsTest {
    private fun tx(type: String = "Expense", cash: Boolean = false, transfer: Boolean = false, amount: Double = 50.0) =
        Transaction(1, "Test", amount, type, "Test", "Account", "Today", cash, transfer)

    @Test fun bankExpenseReducesBank() {
        val result = applyTransactionToBalances(1000.0, 200.0, tx())
        assertEquals(950.0, result.first, 0.001); assertEquals(200.0, result.second, 0.001)
    }
    @Test fun cashExpenseReducesCash() {
        val result = applyTransactionToBalances(1000.0, 200.0, tx(cash = true))
        assertEquals(1000.0, result.first, 0.001); assertEquals(150.0, result.second, 0.001)
    }
    @Test fun bankIncomeIncreasesBank() {
        val result = applyTransactionToBalances(1000.0, 200.0, tx(type = "Income"))
        assertEquals(1050.0, result.first, 0.001); assertEquals(200.0, result.second, 0.001)
    }
    @Test fun cashIncomeIncreasesCash() {
        val result = applyTransactionToBalances(1000.0, 200.0, tx(type = "Income", cash = true))
        assertEquals(1000.0, result.first, 0.001); assertEquals(250.0, result.second, 0.001)
    }
    @Test fun bankToCashTransferMovesFunds() {
        val result = applyTransactionToBalances(1000.0, 200.0, tx(transfer = true))
        assertEquals(950.0, result.first, 0.001); assertEquals(250.0, result.second, 0.001)
    }
    @Test fun cashToBankTransferMovesFunds() {
        val result = applyTransactionToBalances(1000.0, 200.0, tx(cash = true, transfer = true))
        assertEquals(1050.0, result.first, 0.001); assertEquals(150.0, result.second, 0.001)
    }
    @Test fun reversalRestoresBalancesForExpense() {
        val original = tx()
        val after = applyTransactionToBalances(1000.0, 200.0, original)
        val reversed = reverseTransactionFromBalances(after.first, after.second, original)
        assertEquals(1000.0, reversed.first, 0.001); assertEquals(200.0, reversed.second, 0.001)
    }
    @Test fun reversalRestoresBalancesForTransfer() {
        val original = tx(transfer = true)
        val after = applyTransactionToBalances(1000.0, 200.0, original)
        val reversed = reverseTransactionFromBalances(after.first, after.second, original)
        assertEquals(1000.0, reversed.first, 0.001); assertEquals(200.0, reversed.second, 0.001)
    }
    @Test fun replaceExpenseWithIncomeAppliesNetChange() {
        val old = tx()
        val new = tx(type = "Income", amount = 80.0)
        val result = replaceTransactionBalances(1000.0, 200.0, old, new)
        assertEquals(1080.0, result.first, 0.001); assertEquals(200.0, result.second, 0.001)
    }
}
