package com.example.thrive2

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class ThriveNotificationListener :
    NotificationListenerService() {

    override fun onNotificationPosted(
        sbn: StatusBarNotification
    ) {

        Log.d(
            "THRIVE_NOTIFICATION",
            "LISTENER IS RUNNING"
        )

        // Get the notification
        val notification =
            sbn.notification

        // Get the notification information
        val extras =
            notification.extras

        val title =
            extras.getString(
                "android.title"
            ) ?: ""

        val text =
            extras.getCharSequence(
                "android.text"
            )?.toString() ?: ""

        Log.d(
            "THRIVE_NOTIFICATION",
            "RECEIVED: title=$title | text=$text"
        )

        // Ignore completely empty notifications
        if (
            title.isBlank() &&
            text.isBlank()
        ) {

            Log.d(
                "THRIVE_NOTIFICATION",
                "IGNORED: empty notification"
            )

            return
        }

        // Combine title and text so we can search both
        val message =
            "$title $text".lowercase()

        // Words that commonly appear in bank/payment notifications
        val transactionKeywords =
            listOf(
                "purchase",
                "payment",
                "spent",
                "transaction",
                "debit",
                "credit",
                "card",
                "received"
            )

        val looksLikeTransaction =
            transactionKeywords.any {
                message.contains(it)
            }

        // Look for a South African Rand amount
        // Examples:
        // R85
        // R85.00
        // R 85.00
        // R85,00
        val amountPattern =
            Regex(
                """R\s?(\d+(?:[.,]\d{2})?)"""
            )

        val containsAmount =
            amountPattern.containsMatchIn(
                "$title $text"
            )

        Log.d(
            "THRIVE_NOTIFICATION",
            "FILTER: keyword=$looksLikeTransaction amount=$containsAmount"
        )

        // Ignore notifications that do not look like transactions
        if (
            !looksLikeTransaction ||
            !containsAmount
        ) {

            Log.d(
                "THRIVE_NOTIFICATION",
                "IGNORED: not a transaction"
            )

            return
        }

        // Find the actual amount
        val amountMatch =
            amountPattern.find(
                "$title $text"
            )

        if (amountMatch == null) {

            Log.d(
                "THRIVE_NOTIFICATION",
                "IGNORED: amount could not be extracted"
            )

            return
        }

        // Convert the amount into a Double
        val amount =
            amountMatch
                .groupValues[1]
                .replace(",", ".")
                .toDoubleOrNull()

        if (amount == null) {

            Log.d(
                "THRIVE_NOTIFICATION",
                "IGNORED: invalid amount"
            )

            return
        }

        // Show the extracted amount in Logcat
        Log.d(
            "THRIVE_NOTIFICATION",
            "EXTRACTED AMOUNT: R$amount"
        )

        // Save the notification
        val storage =
            ThriveStorage(this)

        storage.saveNotification(
            title = title,
            text = text
        )

        Log.d(
            "THRIVE_NOTIFICATION",
            "ACCEPTED AND SAVED"
        )
    }
}