package com.example.thrive2

/**
 * Applies a transaction to the tracked balances (Bank and Cash).
 * Transfers move money between these two tracked accounts.
 */
fun applyTransactionToBalances(
    bankBalance: Double,
    cashBalance: Double,
    transaction: Transaction
): Pair<Double, Double> {
    var bank = bankBalance
    var cash = cashBalance

    if (transaction.isTransfer) {
        if (transaction.isCash) {
            cash -= transaction.amount
            bank += transaction.amount
        } else {
            bank -= transaction.amount
            cash += transaction.amount
        }
    } else if (transaction.type == "Income") {
        if (transaction.isCash) cash += transaction.amount else bank += transaction.amount
    } else {
        if (transaction.isCash) cash -= transaction.amount else bank -= transaction.amount
    }
    return bank to cash
}

/** Reverse a transaction's effect, useful before editing or deleting it. */
fun reverseTransactionFromBalances(
    bankBalance: Double,
    cashBalance: Double,
    transaction: Transaction
): Pair<Double, Double> {
    val inverse = transaction.copy(
        type = if (transaction.type == "Income") "Expense" else "Income",
        isTransfer = transaction.isTransfer,
        isCash = if (transaction.isTransfer) !transaction.isCash else transaction.isCash
    )
    return applyTransactionToBalances(bankBalance, cashBalance, inverse)
}

/** Replace a transaction by reversing the old effect and applying the new one. */
fun replaceTransactionBalances(
    bankBalance: Double,
    cashBalance: Double,
    oldTransaction: Transaction,
    newTransaction: Transaction
): Pair<Double, Double> {
    val reversed = reverseTransactionFromBalances(bankBalance, cashBalance, oldTransaction)
    return applyTransactionToBalances(reversed.first, reversed.second, newTransaction)
}
