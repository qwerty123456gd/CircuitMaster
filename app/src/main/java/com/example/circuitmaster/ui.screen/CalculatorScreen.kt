package com.example.circuitmaster.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalculatorScreen() {
    var expression by remember { mutableStateOf("0") }
    var result by remember { mutableStateOf("") }
    val pagerState = rememberPagerState(pageCount = { 2 })

    fun calculateResult() {
        try {
            val expr = expression
                .replace("×", "*")
                .replace("÷", "/")
                .replace("π", Math.PI.toString())
                .replace("e", Math.E.toString())

            result = when {
                expr.startsWith("abs(") && expr.endsWith(")") -> {
                    val value = expr.substring(4, expr.length - 1).toDoubleOrNull()
                    if (value != null) abs(value).toString() else "Ошибка"
                }
                expr.startsWith("sin(") && expr.endsWith(")") -> {
                    val value = expr.substring(4, expr.length - 1).toDoubleOrNull()
                    if (value != null) sin(Math.toRadians(value)).toString() else "Ошибка"
                }
                expr.startsWith("cos(") && expr.endsWith(")") -> {
                    val value = expr.substring(4, expr.length - 1).toDoubleOrNull()
                    if (value != null) cos(Math.toRadians(value)).toString() else "Ошибка"
                }
                expr.startsWith("tan(") && expr.endsWith(")") -> {
                    val value = expr.substring(4, expr.length - 1).toDoubleOrNull()
                    if (value != null) tan(Math.toRadians(value)).toString() else "Ошибка"
                }
                expr.startsWith("cot(") && expr.endsWith(")") -> {
                    val value = expr.substring(4, expr.length - 1).toDoubleOrNull()
                    if (value != null) (1.0 / tan(Math.toRadians(value))).toString() else "Ошибка"
                }
                expr.startsWith("√(") && expr.endsWith(")") -> {
                    val value = expr.substring(2, expr.length - 1).toDoubleOrNull()
                    if (value != null && value >= 0) sqrt(value).toString() else "Ошибка"
                }
                expr.startsWith("ln(") && expr.endsWith(")") -> {
                    val value = expr.substring(3, expr.length - 1).toDoubleOrNull()
                    if (value != null && value > 0) ln(value).toString() else "Ошибка"
                }
                expr.startsWith("log(") && expr.endsWith(")") -> {
                    val value = expr.substring(4, expr.length - 1).toDoubleOrNull()
                    if (value != null && value > 0) log10(value).toString() else "Ошибка"
                }
                expr.contains("+") -> {
                    val parts = expr.split("+")
                    (parts[0].trim().toDouble() + parts[1].trim().toDouble()).toString()
                }
                expr.contains("-") && !expr.startsWith("-") -> {
                    val parts = expr.split("-")
                    (parts[0].trim().toDouble() - parts[1].trim().toDouble()).toString()
                }
                expr.contains("*") -> {
                    val parts = expr.split("*")
                    (parts[0].trim().toDouble() * parts[1].trim().toDouble()).toString()
                }
                expr.contains("/") -> {
                    val parts = expr.split("/")
                    val denominator = parts[1].trim().toDouble()
                    if (denominator == 0.0) "На ноль нельзя" else (parts[0].trim().toDouble() / denominator).toString()
                }
                else -> expr
            }
        } catch (e: Exception) {
            result = "Ошибка"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.2f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = expression,
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.End,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = result,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.End
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(2) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                        .background(
                            color = if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            shape = MaterialTheme.shapes.small
                        )
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
        ) { page ->
            if (page == 0) {
                BasicKeypad(
                    onButtonClick = { label ->
                        if (expression == "0") expression = label else expression += label
                    },
                    onBackSpace = {
                        expression = if (expression.length > 1) expression.drop(1) else "0"
                    },
                    onClear = {
                        expression = "0"
                        result = ""
                    },
                    onCalculate = {
                        calculateResult()
                    }
                )
            } else {
                ScientificKeypad(
                    onButtonClick = { label ->
                        val insertText = when (label) {
                            "sin" -> "sin("
                            "cos" -> "cos("
                            "tan" -> "tan("
                            "cot" -> "cot("
                            "√" -> "√("
                            "ln" -> "ln("
                            "log" -> "log("
                            "|a|" -> "abs("
                            else -> label
                        }
                        if (expression == "0") expression = insertText else expression += insertText
                    },
                    onBackSpace = {
                        expression = if (expression.length > 1) expression.drop(1) else "0"
                    }
                )
            }
        }
    }
}

@Composable
fun BasicKeypad(
    onButtonClick: (String) -> Unit,
    onBackSpace: () -> Unit,
    onClear: () -> Unit,
    onCalculate: () -> Unit
) {
    val buttons = listOf(
        listOf("C", "⌫", "%", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "=")
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        buttons.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { label ->
                    val isZero = label == "0"
                    val buttonModifier = if (isZero) Modifier.weight(2f) else Modifier.weight(1f)

                    Button(
                        onClick = {
                            when (label) {
                                "C" -> onClear()
                                "⌫" -> onBackSpace()
                                "=" -> onCalculate()
                                else -> onButtonClick(label)
                            }
                        },
                        modifier = buttonModifier.fillMaxHeight(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (label) {
                                "C", "⌫" -> MaterialTheme.colorScheme.errorContainer
                                "÷", "×", "-", "+", "=" -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.surfaceContainerHigh
                            },
                            contentColor = when (label) {
                                "C", "⌫" -> MaterialTheme.colorScheme.onErrorContainer
                                "÷", "×", "-", "+", "=" -> MaterialTheme.colorScheme.onPrimary
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(text = label, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ScientificKeypad(
    onButtonClick: (String) -> Unit,
    onBackSpace: () -> Unit
) {
    val buttons = listOf(
        listOf("sin", "cos", "tan", "cot"),
        listOf("√", "ln", "log", "π"),
        listOf("e", "|a|", "(", ")"),
        listOf("⌫", "", "", "")
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        buttons.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { label ->
                    if (label.isEmpty()) {
                        Spacer(modifier = Modifier.weight(1f))
                    } else {
                        Button(
                            onClick = {
                                if (label == "⌫") onBackSpace() else onButtonClick(label)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (label == "⌫") MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = if (label == "⌫") MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(text = label, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}