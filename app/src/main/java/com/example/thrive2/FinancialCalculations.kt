package com.example.thrive2

/**
 * Applies the existing Thrive transaction balance rules.
 * Returns updated (bank, cash) balances without mutating state.
 */
fun applyTransactionToBalances(
    bankBalance: Double,
    cashBalance: Double,
    transaction: Transaction
): Pair<Double, Double> {
    var bank = bankBalance
    var cash = cashBalance

    if (transaction.type == "Income") {
        if (transaction.isCash) cash += transaction.amount else bank += transaction.amount
    } else {
        if (transaction.isCash) cash -= transaction.amount
        else if (!transaction.isTransfer) bank -= transaction.amount
    }

    return bank to cash
}
