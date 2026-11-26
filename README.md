# Thrive

**Thrive** is an Android personal management application built with Kotlin and Jetpack Compose. It brings personal finance tracking, goal management, daily planning and personal reflection together in one application.

The project explores building a multi-feature Android application with persistent state, reactive UI components, financial logic and Android system service integration.

## Features

### 💰 Money Management
- Track bank and cash balances.
- Record income and expenses.
- View transaction activity.
- Create and monitor financial goals.

### 🎯 Goal Management
- Create goals across different categories.
- Track goal progress.
- Manage milestones and completion.

### 📅 Daily Planner
- Create tasks with descriptions, dates, times and priorities.
- Associate tasks with personal goals.
- Track task completion.

### 📝 Personal Reflection
- Record written reflections.
- Select moods when journaling.
- Review saved reflections.

### 🔔 Notification Monitoring (Experimental)
- Android Notification Listener Service integration.
- Detect potentially transaction-related notifications.
- Extract candidate South African Rand amounts using pattern matching.
- Store detected notification details locally.

Notification monitoring is experimental and does not automatically validate detected notifications as financial transactions.

## Technology Stack

| Technology | Purpose |
|---|---|
| Kotlin | Application development |
| Jetpack Compose | UI development |
| Material 3 | UI components |
| SharedPreferences | Local application state persistence |
| Android NotificationListenerService | Notification monitoring |
| JUnit | Unit testing |

## Development

Thrive is an evolving Android development project. Development efforts include separating financial calculation logic from UI code, introducing unit testing and improving the organisation of application functionality.

## Planned Improvements
- Improve transaction editing and deletion.
- Refine bank and cash transfer handling.
- Strengthen financial validation.
- Introduce ViewModel-based state management.
- Migrate local persistence to Room.
- Improve notification detection and transaction confirmation.
- Expand automated testing.
- Improve accessibility and UI/UX.

## Getting Started

1. Clone the repository.
2. Open the project in Android Studio.
3. Allow Gradle to sync.
4. Run the application on an Android emulator or compatible Android device.

## Project Status

Thrive is a personal development project undergoing continued improvement. Some features are experimental and functionality is subject to further testing.

## Disclaimer

Thrive is an experimental personal management application and is not intended to provide financial advice or replace professional financial services.
