package com.example.circuitmaster.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.circuitmaster.data.repository.CircuitMasterRepository
import com.example.circuitmaster.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainerScreen() {
    var selectedScreen by remember { mutableStateOf(Screen.COURSE) }
    var showProfileDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = when(selectedScreen) {
                        Screen.COURSE -> "Содержание курса"
                        Screen.TEXTBOOKS -> "Электронные учебники"
                        Screen.TESTS -> "Тесты и задачи"
                    })
                },
                actions = {
                    IconButton(onClick = { showProfileDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Профиль и прогресс"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Book, contentDescription = "Содержание") },
                    label = { Text("Курс") },
                    selected = selectedScreen == Screen.COURSE,
                    onClick = { selectedScreen = Screen.COURSE }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.LibraryBooks, contentDescription = "Учебники") },
                    label = { Text("Учебники") },
                    selected = selectedScreen == Screen.TEXTBOOKS,
                    onClick = { selectedScreen = Screen.TEXTBOOKS }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Assignment, contentDescription = "Тесты") },
                    label = { Text("Тесты") },
                    selected = selectedScreen == Screen.TESTS,
                    onClick = { selectedScreen = Screen.TESTS }
                )
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedScreen) {
                Screen.COURSE -> CourseContentScreen(parts = CircuitMasterRepository.textbookStructure)
                Screen.TEXTBOOKS -> TextbooksScreen(textbooks = CircuitMasterRepository.textbooks)
                Screen.TESTS -> TestsPlaceholderScreen()
            }
        }

        if (showProfileDialog) {
            AlertDialog(
                onDismissRequest = { showProfileDialog = false },
                title = { Text("Профиль и прогресс") },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp) // Ограничиваем высоту для прокрутки
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Пользователь: Елизавета (Liza)",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Статус: Студент / Разработчик",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Divider(modifier = Modifier.padding(vertical = 4.dp))

                        Text(
                            text = "Трекер прогресса по всем главам:",
                            style = MaterialTheme.typography.titleSmall
                        )

                        LinearProgressIndicator(
                            progress = 0.09f,
                            modifier = Modifier.fillMaxWidth()
                        )

                        CircuitMasterRepository.textbookStructure.forEach { part ->
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = part.title,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            part.chapters.forEachIndexed { index, chapter ->
                                val isCompleted = (part.id == "part_1" && index == 0)
                                val statusText = if (isCompleted) "100% освоено" else "0% освоено"

                                Text(
                                    text = "• ${chapter.title}: $statusText",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Общий прогресс: ~9%",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showProfileDialog = false }) {
                        Text("Закрыть")
                    }
                }
            )
        }
    }
}