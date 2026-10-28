package com.example.circuitmaster.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.circuitmaster.data.repository.CircuitMasterRepository
import kotlinx.coroutines.launch

// Экраны приложения для бокового меню
enum class DrawerScreen(val title: String, val icon: ImageVector) {
    HOME("Главная", Icons.Default.Home),
    COURSE("Курс", Icons.Default.Book),
    TEXTBOOKS("Учебники", Icons.Default.LibraryBooks),
    TESTS("Задачки и тесты", Icons.Default.Assignment),
    CALCULATOR("Калькулятор", Icons.Default.Calculate),
    CHARTS("Графики и таблицы", Icons.Default.ShowChart)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainerScreen() {
    var selectedScreen by remember { mutableStateOf(DrawerScreen.HOME) }
    var showProfileDialog by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "CircuitMaster",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

                // Пункты бокового меню с иконками и подписями
                DrawerScreen.values().forEach { screen ->
                    NavigationDrawerItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = selectedScreen == screen,
                        onClick = {
                            selectedScreen = screen
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = selectedScreen.title) },
                    navigationIcon = {
                        // Кнопка с тремя полосками (меню)
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Открыть меню"
                            )
                        }
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
            }
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (selectedScreen) {
                    DrawerScreen.HOME -> HomeScreen()
                    DrawerScreen.COURSE -> CourseContentScreen(parts = CircuitMasterRepository.textbookStructure)
                    DrawerScreen.TEXTBOOKS -> TextbooksScreen(textbooks = CircuitMasterRepository.textbooks)
                    DrawerScreen.TESTS -> TestsPlaceholderScreen()
                    DrawerScreen.CALCULATOR -> CalculatorScreen() // Подключен полноценный калькулятор!
                    DrawerScreen.CHARTS -> PlaceholderScreen(title = "Раздел: Графики и таблицы")
                }
            }

            // Окно профиля
            if (showProfileDialog) {
                ProfileProgressDialog(onDismiss = { showProfileDialog = false })
            }
        }
    }
}

// Главная страница: Формула дня + случайный факт
@Composable
fun HomeScreen() {
    val facts = remember {
        listOf(
            "Густав Кирхгоф сформулировал свои знаменитые правила для электрических цепей в 1845 году, когда ему было всего 21 год!",
            "Георг Ом опубликовал свой закон в 1827 году, но сначала его работы были проигнорированы и холодно приняты научным сообществом.",
            "Томас Эдисон и Никола Тесла вели знаменитую «Войну токов» за выбор между постоянным (DC) и переменным (AC) током.",
            "Первый транзистор был создан в лабораториях Bell Labs в 1947 году Джоном Бардином, Уолтером Браттейном и Уильямом Шокли.",
            "Единица измерения электрической емкости — Фарад — названа в честь Майкла Фарадея, открывшего электромагнитную индукцию."
        )
    }

    val randomFact = remember { facts.random() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Карточка: Формула дня
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "⚡ Формула дня",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Закон Ома для участка цепи",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "I = U / R",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Сила тока в участке цепи прямо пропорциональна напряжению и обратно пропорциональна его сопротивлению.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Карточка: Интересный факт
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "💡 Интересный факт",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = randomFact,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

// Заглушка для новых разделов
@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
    }
}

// Окно прогресса профиля
@Composable
fun ProfileProgressDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Профиль и прогресс") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Пользователь: Елизавета (Liza)", style = MaterialTheme.typography.titleMedium)
                Text("Статус: Студент / Разработчик", style = MaterialTheme.typography.bodyMedium)
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Text("Трекер прогресса по всем главам:", style = MaterialTheme.typography.titleSmall)
                LinearProgressIndicator(progress = 0.09f, modifier = Modifier.fillMaxWidth())

                CircuitMasterRepository.textbookStructure.forEach { part ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(part.title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    part.chapters.forEachIndexed { index, chapter ->
                        val isCompleted = (part.id == "part_1" && index == 0)
                        val statusText = if (isCompleted) "100% освоено" else "0% освоено"
                        Text("• ${chapter.title}: $statusText", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Закрыть") }
        }
    )
}