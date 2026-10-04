package com.example.thrive2

import android.content.Context
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

data class ThriveState(
    val bankBalance: Double,
    val cashBalance: Double,
    val transactions: List<Transaction>,
    val financialGoals: List<FinancialGoal>,
    val goals: List<Goal>,
    val tasks: List<Task>,
    val reflections: List<Reflection>
) : Serializable

class ThriveStorage(context: Context) {

    private val preferences =
        context.getSharedPreferences(
            "thrive_storage",
            Context.MODE_PRIVATE
        )

    // --------------------------------------------------------
    // SAVE THRIVE DATA
    // --------------------------------------------------------

    fun save(state: ThriveState) {

        try {

            val outputStream =
                ByteArrayOutputStream()

            val objectOutputStream =
                ObjectOutputStream(outputStream)

            objectOutputStream.writeObject(state)

            objectOutputStream.close()

            val encoded =
                Base64.encodeToString(
                    outputStream.toByteArray(),
                    Base64.DEFAULT
                )

            preferences
                .edit()
                .putString(
                    "thrive_state",
                    encoded
                )
                .apply()

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

    // --------------------------------------------------------
    // LOAD THRIVE DATA
    // --------------------------------------------------------

    fun load(): ThriveState? {

        return try {

            val encoded =
                preferences.getString(
                    "thrive_state",
                    null
                ) ?: return null

            val bytes =
                Base64.decode(
                    encoded,
                    Base64.DEFAULT
                )

            val inputStream =
                ByteArrayInputStream(bytes)

            val objectInputStream =
                ObjectInputStream(inputStream)

            objectInputStream.readObject()
                    as ThriveState

        } catch (e: Exception) {

            e.printStackTrace()

            null
        }
    }

    // --------------------------------------------------------
    // BANK NOTIFICATION
    // --------------------------------------------------------

    fun saveNotification(
        title: String,
        text: String
    ) {

        preferences
            .edit()
            .putString(
                "notification_title",
                title
            )
            .putString(
                "notification_text",
                text
            )
            .apply()
    }

    fun getNotificationTitle(): String {

        return preferences.getString(
            "notification_title",
            ""
        ) ?: ""
    }

    fun getNotificationText(): String {

        return preferences.getString(
            "notification_text",
            ""
        ) ?: ""
    }
}