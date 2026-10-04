package com.example.thrive2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MoneyActionButton(
    text: String,
    color: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        shape = RoundedCornerShape(16.dp)
    ) {

        Text(
            text = text,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TransactionCard(
    transaction: Transaction
) {

    val positive =
        transaction.type == "Income"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(
                        if (positive)
                            LightGreen
                        else
                            LightOrange
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    if (positive) "+" else "-",
                    fontWeight = FontWeight.Bold,
                    color =
                        if (positive)
                            ThriveGreen
                        else
                            ThriveOrange
                )
            }

            Spacer(
                Modifier.width(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    transaction.description,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "${transaction.category} • ${transaction.date}",
                    fontSize = 12.sp,
                    color = GreyText
                )
            }

            Text(
                "${if (positive) "+" else "-"}R ${
                    "%.2f".format(transaction.amount)
                }",

                fontWeight = FontWeight.Bold,

                color =
                    if (positive)
                        ThriveGreen
                    else
                        ThriveOrange
            )
        }
    }
}

@Composable
fun FinancialGoalCard(
    goal: FinancialGoal,
    onUpdate: (FinancialGoal) -> Unit
) {

    val progress =
        if (goal.target > 0)
            (goal.current / goal.target)
                .coerceIn(0.0, 1.0)
        else
            0.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                goal.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(
                Modifier.height(8.dp)
            )

            LinearProgressIndicator(
                progress = { progress.toFloat() },
                modifier = Modifier.fillMaxWidth(),
                color = ThriveBlue
            )

            Spacer(
                Modifier.height(8.dp)
            )

            Text(
                "R ${"%.2f".format(goal.current)} / R ${
                    "%.2f".format(goal.target)
                }"
            )

            Text(
                "${(progress * 100).toInt()}% complete",
                color = GreyText
            )

            Spacer(
                Modifier.height(8.dp)
            )

            Button(
                onClick = {

                    val increment =
                        minOf(
                            100.0,
                            goal.target - goal.current
                        )

                    onUpdate(
                        goal.copy(
                            current =
                                goal.current + increment
                        )
                    )
                }
            ) {

                Text("+ R100")
            }
        }
    }
}

@Composable
fun GoalCard(
    goal: Goal,
    onUpdate: (Goal) -> Unit
) {

    val progress =

        when (goal.trackingType) {

            "Measurable" -> {

                if (goal.target > 0)
                    (goal.current / goal.target)
                        .coerceIn(0.0, 1.0)
                else
                    0.0
            }

            "Milestones" -> {

                if (goal.milestones.isNotEmpty()) {

                    goal.completedMilestones
                        .count { it }
                        .toDouble() /
                            goal.milestones.size

                } else 0.0
            }

            else -> 0.0
        }

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(22.dp)
    ) {

        Column(
            modifier =
                Modifier.padding(20.dp)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        goal.title,
                        fontSize = 19.sp,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        goal.category,
                        color = ThriveOrange,
                        fontSize = 13.sp
                    )
                }

                Text(
                    "${(progress * 100).toInt()}%",
                    fontWeight =
                        FontWeight.Bold,
                    color = ThriveOrange
                )
            }

            Spacer(
                Modifier.height(12.dp)
            )

            if (goal.trackingType != "Open-ended") {

                LinearProgressIndicator(
                    progress = {
                        progress.toFloat()
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    color = ThriveOrange
                )
            }

            Spacer(
                Modifier.height(10.dp)
            )

            Text(
                goal.description.ifBlank {
                    "No description added."
                },

                color = GreyText
            )

            Spacer(
                Modifier.height(10.dp)
            )

            Text(
                "Tracking: ${goal.trackingType}",
                fontSize = 12.sp,
                color = GreyText
            )

            if (
                goal.trackingType ==
                "Measurable"
            ) {

                Spacer(
                    Modifier.height(8.dp)
                )

                Button(
                    onClick = {

                        val increase =
                            if (goal.target > 0)
                                goal.target * .1
                            else 1.0

                        onUpdate(
                            goal.copy(
                                current =
                                    minOf(
                                        goal.target,
                                        goal.current +
                                                increase
                                    )
                            )
                        )
                    }
                ) {

                    Text("+ Progress")
                }
            }

            if (
                goal.trackingType ==
                "Milestones"
            ) {

                Spacer(
                    Modifier.height(10.dp)
                )

                goal.milestones.forEachIndexed {
                        index,
                        milestone ->

                    val completed =
                        goal.completedMilestones
                            .getOrNull(index)
                            ?: false

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Checkbox(
                            checked = completed,

                            onCheckedChange = { checked ->

                                val updated =
                                    goal.completedMilestones
                                        .toMutableList()

                                if (
                                    index <
                                    updated.size
                                ) {
                                    updated[index] =
                                        checked
                                }

                                onUpdate(
                                    goal.copy(
                                        completedMilestones =
                                            updated
                                    )
                                )
                            }
                        )

                        Text(
                            milestone,
                            modifier =
                                Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    onToggle: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp)
    ) {

        Row(
            modifier =
                Modifier.padding(16.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Checkbox(
                checked = task.completed,
                onCheckedChange = {
                    onToggle()
                }
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    task.title,
                    fontWeight =
                        FontWeight.Bold
                )

                if (
                    task.description.isNotBlank()
                ) {

                    Text(
                        task.description,
                        color = GreyText,
                        fontSize = 13.sp
                    )
                }

                Text(
                    "${task.date} • ${task.time}",
                    color = ThriveGreen,
                    fontSize = 12.sp
                )

                if (
                    task.relatedGoal.isNotBlank()
                ) {

                    Text(
                        "Goal: ${task.relatedGoal}",
                        color = GreyText,
                        fontSize = 12.sp
                    )
                }
            }

            Text(
                task.priority,
                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}

@Composable
fun ReflectionCard(
    reflection: Reflection
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp)
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp)
        ) {

            Row {

                Text(
                    reflection.mood,
                    fontSize = 28.sp
                )

                Spacer(
                    Modifier.width(10.dp)
                )

                Text(
                    reflection.date,
                    fontWeight =
                        FontWeight.Bold
                )
            }

            Spacer(
                Modifier.height(10.dp)
            )

            Text(
                reflection.text
            )
        }
    }
}

@Composable
fun SectionTitle(
    title: String,
    action: String? = null,
    onAction: (() -> Unit)? = null
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.SpaceBetween,

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            title,
            fontWeight =
                FontWeight.Bold,
            fontSize = 20.sp
        )

        if (
            action != null &&
            onAction != null
        ) {

            TextButton(
                onClick = onAction
            ) {

                Text(action)
            }
        }
    }
}

@Composable
fun EmptyCard(
    message: String
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp)
    ) {

        Text(
            text = message,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),

            color = GreyText,

            textAlign =
                TextAlign.Center
        )
    }
}