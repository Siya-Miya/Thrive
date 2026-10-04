package com.example.thrive2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thrive2.ui.theme.ThriveTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ============================================================
// THRIVE
// Student-focused finance, goals, planning and reflection app
// Prototype version - functionality first
// ============================================================

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ThriveTheme {
                ThriveApp()
            }
        }
    }
}

// ============================================================
// COLORS
// ============================================================

val ThriveBlue = Color(0xFF2979FF)
val ThriveOrange = Color(0xFFFF8A3D)
val ThriveGreen = Color(0xFF22A06B)
val ThrivePurple = Color(0xFF8E5CF6)

val Background = Color(0xFFF7F8FC)
val DarkText = Color(0xFF20232A)
val GreyText = Color(0xFF737780)
val CardWhite = Color.White
val LightBlue = Color(0xFFEAF2FF)
val LightOrange = Color(0xFFFFF0E7)
val LightGreen = Color(0xFFE8F8F1)
val LightPurple = Color(0xFFF0EAFF)

// ============================================================
// DATA MODELS
// ============================================================

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

// ============================================================
// MAIN APP
// ============================================================

@Composable
fun ThriveApp() {

    val context = androidx.compose.ui.platform.LocalContext.current
    val storage = remember {
        ThriveStorage(context)
    }

    var notificationTitle by remember {
        mutableStateOf(
            storage.getNotificationTitle()
        )
    }

    var notificationText by remember {
        mutableStateOf(
            storage.getNotificationText()
        )
    }

    val savedState = remember {
        storage.load()
    }

    var selectedTab by remember {
        mutableStateOf(0)
    }

    // --------------------------------------------------------
// FINANCIAL DATA
// --------------------------------------------------------

    var bankBalance by remember {
        mutableStateOf(
            savedState?.bankBalance ?: 5000.0
        )
    }

    var cashBalance by remember {
        mutableStateOf(
            savedState?.cashBalance ?: 500.0
        )
    }

    var transactions by remember {
        mutableStateOf(
            savedState?.transactions
                ?: listOf(
                    Transaction(
                        1,
                        "Initial Balance",
                        5000.0,
                        "Income",
                        "Starting Balance",
                        "Main Account",
                        today()
                    )
                )
        )
    }

    var financialGoals by remember {
        mutableStateOf(
            savedState?.financialGoals
                ?: emptyList()
        )
    }

// --------------------------------------------------------
// GOALS
// --------------------------------------------------------

    var goals by remember {
        mutableStateOf(
            savedState?.goals
                ?: emptyList()
        )
    }

// --------------------------------------------------------
// PLANNER
// --------------------------------------------------------

    var tasks by remember {
        mutableStateOf(
            savedState?.tasks
                ?: emptyList()
        )
    }

// --------------------------------------------------------
// REFLECTIONS
// --------------------------------------------------------

    var reflections by remember {
        mutableStateOf(
            savedState?.reflections
                ?: emptyList()
        )
    }

    LaunchedEffect(
        bankBalance,
        cashBalance,
        transactions,
        financialGoals,
        goals,
        tasks,
        reflections
    ) {

        storage.save(
            ThriveState(
                bankBalance = bankBalance,
                cashBalance = cashBalance,
                transactions = transactions,
                financialGoals = financialGoals,
                goals = goals,
                tasks = tasks,
                reflections = reflections
            )
        )
    }

    Scaffold(
        containerColor = Background,
        bottomBar = {

            NavigationBar(
                containerColor = Color.White
            ) {

                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Text("💰", fontSize = 21.sp)
                    },
                    label = {
                        Text("Money")
                    }
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Text("🎯", fontSize = 21.sp)
                    },
                    label = {
                        Text("Goals")
                    }
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Text("📅", fontSize = 21.sp)
                    },
                    label = {
                        Text("Planner")
                    }
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = {
                        Text("💜", fontSize = 21.sp)
                    },
                    label = {
                        Text("Reflect")
                    }
                )
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            when (selectedTab) {

                0 -> MoneyScreen(
                    bankBalance = bankBalance,
                    cashBalance = cashBalance,
                    notificationTitle = notificationTitle,
                    notificationText = notificationText,
                    transactions = transactions,
                    financialGoals = financialGoals,
                    goals = goals,
                    tasks = tasks,
                    reflections = reflections,
                    onAddTransaction = { transaction ->

                        transactions =
                            transactions + transaction

                        if (transaction.type == "Income") {

                            if (transaction.isCash) {
                                cashBalance += transaction.amount
                            } else {
                                bankBalance += transaction.amount
                            }

                        } else {

                            if (transaction.isCash) {
                                cashBalance -= transaction.amount
                            } else if (!transaction.isTransfer) {
                                bankBalance -= transaction.amount
                            }
                        }
                    },

                    onAddFinancialGoal = { goal ->
                        financialGoals =
                            financialGoals + goal
                    },

                    onUpdateFinancialGoal = { updated ->

                        financialGoals =
                            financialGoals.map {

                                if (it.id == updated.id)
                                    updated
                                else
                                    it
                            }
                    }
                )

                1 -> GoalsScreen(
                    goals = goals,

                    onCreateGoal = { goal ->
                        goals = goals + goal
                    },

                    onUpdateGoal = { updated ->

                        goals =
                            goals.map {

                                if (it.id == updated.id)
                                    updated
                                else
                                    it
                            }
                    }
                )

                2 -> PlannerScreen(
                    tasks = tasks,
                    goals = goals,

                    onAddTask = { task ->
                        tasks = tasks + task
                    },

                    onToggleTask = { id ->

                        tasks =
                            tasks.map {

                                if (it.id == id)
                                    it.copy(completed = !it.completed)
                                else
                                    it
                            }
                    }
                )

                3 -> ReflectScreen(
                    reflections = reflections,

                    onSaveReflection = { reflection ->
                        reflections =
                            listOf(reflection) + reflections
                    }
                )
            }
        }
    }
}

// ============================================================
// MONEY SCREEN
// ============================================================

@Composable
fun MoneyScreen(
    bankBalance: Double,
    cashBalance: Double,
    notificationTitle: String,
    notificationText: String,
    transactions: List<Transaction>,
    financialGoals: List<FinancialGoal>,
    goals: List<Goal>,
    tasks: List<Task>,
    reflections: List<Reflection>,
    onAddTransaction: (Transaction) -> Unit,
    onAddFinancialGoal: (FinancialGoal) -> Unit,
    onUpdateFinancialGoal: (FinancialGoal) -> Unit
) {

    var showTransactionDialog by remember {
        mutableStateOf(false)
    }

    var showGoalDialog by remember {
        mutableStateOf(false)
    }

    val totalBalance =
        bankBalance + cashBalance

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),

        contentPadding = PaddingValues(20.dp),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        // -----------------------------
        // ASSISTANT
        // -----------------------------

        item {

            ThriveAssistantCard(
                bankBalance = bankBalance,
                cashBalance = cashBalance,
                transactions = transactions,
                financialGoals = financialGoals,
                goals = goals,
                tasks = tasks,
                reflections = reflections
            )
        }

        // -----------------------------
        // MONEY HEADER
        // -----------------------------

        item {

            Column {

                Text(
                    text = "Money",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = ThriveBlue
                )

                Text(
                    text = "Understand where your money goes.",
                    color = GreyText
                )
            }
        }

        // -----------------------------
        // BALANCE CARD
        // -----------------------------

        item {

            Card(
                shape = RoundedCornerShape(24.dp),

                colors = CardDefaults.cardColors(
                    containerColor = ThriveBlue
                ),

                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    Text(
                        "Available Balance",
                        color = Color.White.copy(
                            alpha = 0.8f
                        )
                    )

                    Text(
                        "R ${"%.2f".format(totalBalance)}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(
                        Modifier.height(16.dp)
                    )

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Column {

                            Text(
                                "Bank",
                                color =
                                    Color.White.copy(
                                        alpha = 0.75f
                                    )
                            )

                            Text(
                                "R ${"%.2f".format(bankBalance)}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column {

                            Text(
                                "Cash",
                                color =
                                    Color.White.copy(
                                        alpha = 0.75f
                                    )
                            )

                            Text(
                                "R ${"%.2f".format(cashBalance)}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // -----------------------------
        // INCOME / EXPENSE
        // -----------------------------

        item {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                MoneyActionButton(
                    text = "Income",
                    color = ThriveGreen,
                    modifier =
                        Modifier.weight(1f)
                ) {

                    showTransactionDialog = true
                }

                MoneyActionButton(
                    text = "Expense",
                    color = ThriveOrange,
                    modifier =
                        Modifier.weight(1f)
                ) {

                    showTransactionDialog = true
                }
            }
        }

        // -----------------------------
        // FINANCIAL GOALS
        // -----------------------------

        item {

            SectionTitle(
                title = "Financial Goals",
                action = "+ Add"
            ) {

                showGoalDialog = true
            }
        }

        if (financialGoals.isEmpty()) {

            item {

                EmptyCard(
                    "Create a financial goal such as saving for a laptop, emergency fund or trip."
                )
            }

        } else {

            items(financialGoals) { goal ->

                FinancialGoalCard(
                    goal = goal,
                    onUpdate = onUpdateFinancialGoal
                )
            }
        }

        // -----------------------------
        // RECENT ACTIVITY
        // -----------------------------

        item {

            SectionTitle(
                title = "Recent Activity"
            )
        }

        if (transactions.isEmpty()) {

            item {

                EmptyCard(
                    "No transactions recorded yet."
                )
            }

        } else {

            items(
                transactions
                    .takeLast(10)
                    .reversed()
            ) { transaction ->

                TransactionCard(
                    transaction
                )
            }
        }
    }

    // -----------------------------
    // TRANSACTION DIALOG
    // -----------------------------

    if (showTransactionDialog) {

        AddTransactionDialog(

            onDismiss = {

                showTransactionDialog = false
            },

            onAdd = { transaction ->

                onAddTransaction(transaction)

                showTransactionDialog = false
            }
        )
    }

    // -----------------------------
    // FINANCIAL GOAL DIALOG
    // -----------------------------

    if (showGoalDialog) {

        AddFinancialGoalDialog(

            onDismiss = {

                showGoalDialog = false
            },

            onAdd = { goal ->

                onAddFinancialGoal(goal)

                showGoalDialog = false
            }
        )
    }
}


// ============================================================
// ADD TRANSACTION
// ============================================================

@Composable
fun AddTransactionDialog(
    onDismiss: () -> Unit,
    onAdd: (Transaction) -> Unit
) {

    var description by remember {
        mutableStateOf("")
    }

    var amount by remember {
        mutableStateOf("")
    }

    var type by remember {
        mutableStateOf("Expense")
    }

    var category by remember {
        mutableStateOf("Food")
    }

    var isCash by remember {
        mutableStateOf(false)
    }

    val categories =
        listOf(
            "Food",
            "Snacks",
            "Transport",
            "Entertainment",
            "Clothing",
            "Education",
            "Bills",
            "Personal",
            "Health",
            "Subscriptions",
            "Other",
            "Micro-spending",
            "Transfer"
        )

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text("Add Transaction")
        },

        text = {

            Column(
                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                OutlinedTextField(
                    value = description,

                    onValueChange = {
                        description = it
                    },

                    label = {
                        Text("Description")
                    }
                )

                OutlinedTextField(
                    value = amount,

                    onValueChange = {
                        amount = it
                    },

                    label = {
                        Text("Amount")
                    }
                )

                Row {

                    FilterChip(
                        selected =
                            type == "Expense",

                        onClick = {
                            type = "Expense"
                        },

                        label = {
                            Text("Expense")
                        }
                    )

                    Spacer(
                        Modifier.width(8.dp)
                    )

                    FilterChip(
                        selected =
                            type == "Income",

                        onClick = {
                            type = "Income"
                        },

                        label = {
                            Text("Income")
                        }
                    )
                }

                Text(
                    "Category",
                    fontWeight =
                        FontWeight.Bold
                )

                Row(
                    modifier =
                        Modifier.horizontalScroll(
                            rememberScrollState()
                        ),

                    horizontalArrangement =
                        Arrangement.spacedBy(6.dp)
                ) {

                    categories.forEach { categoryItem ->

                        FilterChip(

                            selected =
                                category == categoryItem,

                            onClick = {
                                category =
                                    categoryItem
                            },

                            label = {
                                Text(categoryItem)
                            }
                        )
                    }
                }

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = isCash,

                        onCheckedChange = {
                            isCash = it
                        }
                    )

                    Text(
                        "Cash transaction"
                    )
                }
            }
        },

        confirmButton = {

            Button(

                onClick = {

                    val value =
                        amount.toDoubleOrNull()

                    if (
                        description.isNotBlank() &&
                        value != null &&
                        value > 0
                    ) {

                        val finalCategory =
                            if (
                                value <= 70 &&
                                type == "Expense"
                            ) {
                                "Micro-spending"
                            } else {
                                category
                            }

                        onAdd(

                            Transaction(

                                id =
                                    System.currentTimeMillis()
                                        .toInt(),

                                description =
                                    description,

                                amount =
                                    value,

                                type =
                                    type,

                                category =
                                    finalCategory,

                                account =
                                    if (isCash)
                                        "Cash"
                                    else
                                        "Main Account",

                                date =
                                    today(),

                                isCash =
                                    isCash,

                                isTransfer =
                                    finalCategory ==
                                            "Transfer"
                            )
                        )
                    }
                }

            ) {

                Text("Add")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Cancel")
            }
        }
    )
}



// ============================================================
// TRANSACTION CARD
// ============================================================



// ============================================================
// FINANCIAL GOAL CARD
// ============================================================



// ============================================================
// ADD FINANCIAL GOAL
// ============================================================

@Composable
fun AddFinancialGoalDialog(
    onDismiss: () -> Unit,
    onAdd: (FinancialGoal) -> Unit
) {

    var title by remember {
        mutableStateOf("")
    }

    var target by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Financial Goal")
        },

        text = {

            Column {

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    label = {
                        Text("What are you saving for?")
                    }
                )

                Spacer(
                    Modifier.height(10.dp)
                )

                OutlinedTextField(
                    value = target,
                    onValueChange = {
                        target = it
                    },
                    label = {
                        Text("Target amount")
                    }
                )
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    val amount =
                        target.toDoubleOrNull()

                    if (
                        title.isNotBlank() &&
                        amount != null
                    ) {

                        onAdd(
                            FinancialGoal(
                                id =
                                    System.currentTimeMillis()
                                        .toInt(),

                                title = title,

                                target = amount,

                                current = 0.0
                            )
                        )
                    }
                }
            ) {

                Text("Create")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Cancel")
            }
        }
    )
}

// ============================================================
// GOALS SCREEN
// ============================================================

@Composable
fun GoalsScreen(
    goals: List<Goal>,
    onCreateGoal: (Goal) -> Unit,
    onUpdateGoal: (Goal) -> Unit
) {

    var showCreate by remember {
        mutableStateOf(false)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),

        contentPadding =
            PaddingValues(20.dp),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        item {

            Text(
                "Goals",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = ThriveOrange
            )

            Text(
                "Decide where you want to go.",
                color = GreyText
            )
        }

        item {

            Button(
                onClick = {
                    showCreate = true
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            ThriveOrange
                    ),

                shape =
                    RoundedCornerShape(16.dp)
            ) {

                Text(
                    "+ Create Goal",
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }

        if (goals.isEmpty()) {

            item {

                EmptyCard(
                    "Your goals will appear here. Choose a suggested goal or create your own."
                )
            }

        } else {

            items(goals) { goal ->

                GoalCard(
                    goal = goal,
                    onUpdate = onUpdateGoal
                )
            }
        }
    }

    if (showCreate) {

        CreateGoalDialog(

            onDismiss = {
                showCreate = false
            },

            onCreate = { goal ->

                onCreateGoal(goal)
                showCreate = false
            }
        )
    }
}

// ============================================================
// CREATE GOAL
// ============================================================

@Composable
fun CreateGoalDialog(
    onDismiss: () -> Unit,
    onCreate: (Goal) -> Unit
) {

    var step by remember {
        mutableStateOf(0)
    }

    var category by remember {
        mutableStateOf("")
    }

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var trackingType by remember {
        mutableStateOf("")
    }

    var target by remember {
        mutableStateOf("")
    }

    var milestonesText by remember {
        mutableStateOf("")
    }

    val suggestedTitles =
        when (category) {

            "Academic" ->
                listOf(
                    "Pass a module",
                    "Improve my marks",
                    "Prepare for an exam",
                    "Complete a project",
                    "Learn a skill"
                )

            "Financial" ->
                listOf(
                    "Save money",
                    "Build emergency savings",
                    "Reduce spending",
                    "Buy something important"
                )

            "Fitness" ->
                listOf(
                    "Exercise regularly",
                    "Lose weight",
                    "Gain fitness",
                    "Run a specific distance"
                )

            "Career" ->
                listOf(
                    "Build a portfolio",
                    "Learn a career skill",
                    "Find an internship",
                    "Complete a certification"
                )

            "Personal" ->
                listOf(
                    "Build a habit",
                    "Improve relationships",
                    "Read more",
                    "Personal development"
                )

            else ->
                emptyList()
        }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {

            Text(
                when (step) {

                    0 -> "What do you want to achieve?"

                    1 -> "Choose a goal"

                    else -> "How should we track it?"
                }
            )
        },

        text = {

            when (step) {

                0 -> {

                    Column(
                        verticalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {

                        listOf(
                            "Academic",
                            "Financial",
                            "Fitness",
                            "Career",
                            "Personal",
                            "Custom"
                        ).forEach { item ->

                            Button(
                                onClick = {

                                    category = item
                                    step = 1

                                    if (item == "Custom") {
                                        title = ""
                                    }
                                },

                                modifier =
                                    Modifier.fillMaxWidth(),

                                colors =
                                    ButtonDefaults.buttonColors(
                                        containerColor =
                                            when (item) {

                                                "Academic" ->
                                                    ThriveBlue

                                                "Financial" ->
                                                    ThriveGreen

                                                "Fitness" ->
                                                    Color(0xFF26A69A)

                                                "Career" ->
                                                    ThriveOrange

                                                "Personal" ->
                                                    ThrivePurple

                                                else ->
                                                    DarkText
                                            }
                                    )
                            ) {

                                Text(item)
                            }
                        }
                    }
                }

                1 -> {

                    Column {

                        suggestedTitles.forEach {

                            Text(
                                text = it,
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable {

                                            title = it
                                        }
                                        .padding(12.dp),

                                fontWeight =
                                    if (title == it)
                                        FontWeight.Bold
                                    else
                                        FontWeight.Normal
                            )
                        }

                        OutlinedTextField(
                            value = title,
                            onValueChange = {
                                title = it
                            },

                            label = {
                                Text(
                                    if (
                                        category ==
                                        "Custom"
                                    )
                                        "Goal title"
                                    else
                                        "Or enter your own"
                                )
                            }
                        )

                        Spacer(
                            Modifier.height(8.dp)
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = {
                                description = it
                            },
                            label = {
                                Text("Description")
                            }
                        )
                    }
                }

                2 -> {

                    Column(
                        verticalArrangement =
                            Arrangement.spacedBy(10.dp)
                    ) {

                        listOf(
                            "Measurable",
                            "Milestones",
                            "Open-ended"
                        ).forEach {

                            FilterChip(
                                selected =
                                    trackingType == it,

                                onClick = {
                                    trackingType = it
                                },

                                label = {
                                    Text(it)
                                }
                            )
                        }

                        if (
                            trackingType ==
                            "Measurable"
                        ) {

                            OutlinedTextField(
                                value = target,
                                onValueChange = {
                                    target = it
                                },
                                label = {
                                    Text("Target value")
                                }
                            )
                        }

                        if (
                            trackingType ==
                            "Milestones"
                        ) {

                            OutlinedTextField(
                                value =
                                    milestonesText,

                                onValueChange = {
                                    milestonesText =
                                        it
                                },

                                label = {
                                    Text(
                                        "Milestones separated by commas"
                                    )
                                }
                            )
                        }
                    }
                }
            }
        },

        confirmButton = {

            if (step < 2) {

                Button(
                    onClick = {

                        if (step == 1 &&
                            title.isBlank()
                        ) {
                            return@Button
                        }

                        step++
                    }
                ) {

                    Text("Next")
                }

            } else {

                Button(
                    onClick = {

                        val milestones =
                            milestonesText
                                .split(",")
                                .map {
                                    it.trim()
                                }
                                .filter {
                                    it.isNotBlank()
                                }

                        onCreate(

                            Goal(
                                id =
                                    System.currentTimeMillis()
                                        .toInt(),

                                title = title,

                                description =
                                    description,

                                category =
                                    category,

                                trackingType =
                                    trackingType,

                                target =
                                    target.toDoubleOrNull()
                                        ?: 0.0,

                                current = 0.0,

                                milestones =
                                    milestones,

                                completedMilestones =
                                    milestones.map {
                                        false
                                    }
                            )
                        )
                    }
                ) {

                    Text("Create Goal")
                }
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Cancel")
            }
        }
    )
}

// ============================================================
// GOAL CARD
// ============================================================



// ============================================================
// PLANNER
// ============================================================

@Composable
fun PlannerScreen(
    tasks: List<Task>,
    goals: List<Goal>,
    onAddTask: (Task) -> Unit,
    onToggleTask: (Int) -> Unit
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    val completed =
        tasks.count { it.completed }

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Background),

        contentPadding =
            PaddingValues(20.dp),

        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {

        item {

            Text(
                "Planner",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = ThriveGreen
            )

            Text(
                "Turn your goals into action.",
                color = GreyText
            )
        }

        item {

            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            LightGreen
                    ),

                shape =
                    RoundedCornerShape(20.dp)
            ) {

                Column(
                    modifier =
                        Modifier.padding(18.dp)
                ) {

                    Text(
                        "Today's Progress",
                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        "$completed / ${tasks.size} tasks completed",
                        fontSize = 22.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color = ThriveGreen
                    )
                }
            }
        }

        item {

            Button(
                onClick = {
                    showDialog = true
                },

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(54.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            ThriveGreen
                    ),

                shape =
                    RoundedCornerShape(16.dp)
            ) {

                Text("+ Add Task")
            }
        }

        if (tasks.isEmpty()) {

            item {

                EmptyCard(
                    "Add tasks that move your goals forward."
                )
            }

        } else {

            items(tasks) { task ->

                TaskCard(
                    task = task,
                    onToggle = {
                        onToggleTask(task.id)
                    }
                )
            }
        }
    }

    if (showDialog) {

        AddTaskDialog(

            goals = goals,

            onDismiss = {
                showDialog = false
            },

            onAdd = { task ->

                onAddTask(task)
                showDialog = false
            }
        )
    }
}

// ============================================================
// TASK CARD
// ============================================================



// ============================================================
// ADD TASK
// ============================================================

@Composable
fun AddTaskDialog(
    goals: List<Goal>,
    onDismiss: () -> Unit,
    onAdd: (Task) -> Unit
) {

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var date by remember {
        mutableStateOf(today())
    }

    var time by remember {
        mutableStateOf("18:00")
    }

    var priority by remember {
        mutableStateOf("Medium")
    }

    var relatedGoal by remember {
        mutableStateOf("")
    }

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text("Add Task")
        },

        text = {

            Column(
                verticalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    label = {
                        Text("Task")
                    }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    label = {
                        Text("Description")
                    }
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = {
                        date = it
                    },
                    label = {
                        Text("Date")
                    }
                )

                OutlinedTextField(
                    value = time,
                    onValueChange = {
                        time = it
                    },
                    label = {
                        Text("Time")
                    }
                )

                Text("Priority")

                Row {

                    listOf(
                        "Low",
                        "Medium",
                        "High"
                    ).forEach {

                        FilterChip(
                            selected =
                                priority == it,

                            onClick = {
                                priority = it
                            },

                            label = {
                                Text(it)
                            }
                        )

                        Spacer(
                            Modifier.width(4.dp)
                        )
                    }
                }

                Text("Related Goal")

                Row(
                    modifier =
                        Modifier.horizontalScroll(
                            rememberScrollState()
                        )
                ) {

                    goals.forEach { goal ->

                        FilterChip(
                            selected =
                                relatedGoal ==
                                        goal.title,

                            onClick = {
                                relatedGoal =
                                    goal.title
                            },

                            label = {
                                Text(goal.title)
                            }
                        )

                        Spacer(
                            Modifier.width(5.dp)
                        )
                    }
                }
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    if (title.isNotBlank()) {

                        onAdd(

                            Task(
                                id =
                                    System.currentTimeMillis()
                                        .toInt(),

                                title = title,

                                description =
                                    description,

                                date = date,

                                time = time,

                                priority =
                                    priority,

                                relatedGoal =
                                    relatedGoal
                            )
                        )
                    }
                }
            ) {

                Text("Add Task")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Cancel")
            }
        }
    )
}

// ============================================================
// REFLECT
// ============================================================

@Composable
fun ReflectScreen(
    reflections: List<Reflection>,
    onSaveReflection: (Reflection) -> Unit
) {

    var mood by remember {
        mutableStateOf("🙂")
    }

    var text by remember {
        mutableStateOf("")
    }

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Background),

        contentPadding =
            PaddingValues(20.dp),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        item {

            Text(
                "Reflect",
                fontSize = 30.sp,
                fontWeight =
                    FontWeight.Bold,
                color = ThrivePurple
            )

            Text(
                "Slow down. Check in with yourself.",
                color = GreyText
            )
        }

        item {

            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            LightPurple
                    ),

                shape =
                    RoundedCornerShape(22.dp)
            ) {

                Column(
                    modifier =
                        Modifier.padding(20.dp)
                ) {

                    Text(
                        "How are you feeling?",
                        fontWeight =
                            FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(
                        Modifier.height(12.dp)
                    )

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceEvenly
                    ) {

                        listOf(
                            "😄",
                            "🙂",
                            "😐",
                            "😔",
                            "😡",
                            "😴"
                        ).forEach {

                            Text(
                                text = it,
                                fontSize = 30.sp,

                                modifier =
                                    Modifier
                                        .clickable {
                                            mood = it
                                        }
                                        .padding(6.dp)
                            )
                        }
                    }
                }
            }
        }

        item {

            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                },

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(180.dp),

                label = {
                    Text(
                        "What's on your mind?"
                    )
                }
            )
        }

        item {

            Button(
                onClick = {

                    if (text.isNotBlank()) {

                        onSaveReflection(

                            Reflection(
                                id =
                                    System.currentTimeMillis()
                                        .toInt(),

                                date = today(),

                                mood = mood,

                                text = text
                            )
                        )

                        text = ""
                    }
                },

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(54.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            ThrivePurple
                    ),

                shape =
                    RoundedCornerShape(16.dp)
            ) {

                Text(
                    "Save Reflection",
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }

        item {

            Text(
                "Previous Reflections",
                fontWeight =
                    FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        if (reflections.isEmpty()) {

            item {

                EmptyCard(
                    "Your reflections will appear here."
                )
            }

        } else {

            items(reflections) { reflection ->

                ReflectionCard(reflection)
            }
        }
    }
}

// ============================================================
// REFLECTION CARD
// ============================================================



// ============================================================
// GENERIC COMPONENTS
// ============================================================





// ============================================================
// DATE
// ============================================================

fun today(): String {

    return SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    ).format(Date())
}
@Composable
fun ThriveAssistantCard(
    bankBalance: Double,
    cashBalance: Double,
    transactions: List<Transaction>,
    financialGoals: List<FinancialGoal>,
    goals: List<Goal>,
    tasks: List<Task>,
    reflections: List<Reflection>
) {

    var insightType by remember {
        mutableStateOf(0)
    }

    val totalBalance =
        bankBalance + cashBalance

    val expenses =
        transactions
            .filter { it.type == "Expense" }

    val totalSpent =
        expenses.sumOf { it.amount }

    val incompleteTasks =
        tasks.count { !it.completed }

    val completedTasks =
        tasks.count { it.completed }

    val completedGoals =
        goals.count {
            it.status == "Completed"
        }

    val activeGoals =
        goals.count {
            it.status != "Completed"
        }

    val insight = when (insightType) {

        0 -> {

            if (transactions.isEmpty()) {

                "You haven't recorded any transactions yet. Adding your spending will help me understand your money."

            } else {

                "You currently have R ${
                    "%.2f".format(totalBalance)
                } available and have recorded ${
                    transactions.size
                } transaction(s)."
            }
        }

        1 -> {

            when {

                goals.isEmpty() ->
                    "You don't have any goals yet. Creating one gives you something specific to work toward."

                completedGoals > 0 ->
                    "You've completed $completedGoals goal(s). You also have $activeGoals active goal(s) to keep working on."

                else ->
                    "You currently have ${goals.size} active goal(s). Keep making small steps toward them."
            }
        }

        2 -> {

            when {

                tasks.isEmpty() ->
                    "Your planner is empty. Add a task that moves one of your goals forward."

                incompleteTasks == 0 ->
                    "Nice work. You've completed all $completedTasks planner task(s)."

                else ->
                    "You have $incompleteTasks unfinished task(s). Pick one and get it done before worrying about everything else."
            }
        }

        else -> {

            when {

                reflections.isEmpty() ->
                    "You haven't written a reflection yet. A quick check-in can help you understand how your week is going."

                else ->
                    "You've recorded ${reflections.size} reflection(s). Taking time to look back can help you notice patterns in how you're doing."
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    LightPurple
            )
    ) {

        Column(
            modifier =
                Modifier.padding(20.dp)
        ) {

            Text(
                text = "🤖 Thrive Assistant",

                fontSize = 20.sp,

                fontWeight =
                    FontWeight.Bold,

                color = ThrivePurple
            )

            Spacer(
                Modifier.height(10.dp)
            )

            Text(
                text = insight,

                color = DarkText,

                fontSize = 16.sp
            )

            Spacer(
                Modifier.height(16.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = {

                        insightType =
                            (insightType + 1) % 4
                    },

                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text("Next insight")
                }
            }

            Spacer(
                Modifier.height(8.dp)
            )

            Text(
                text =
                    "Spending recorded: R ${
                        "%.2f".format(totalSpent)
                    }",

                color = GreyText,

                fontSize = 12.sp
            )
        }
    }
}

fun openNotificationSettings(
    context: android.content.Context
) {

    val intent =
        android.content.Intent(
            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
        )

    context.startActivity(intent)
}