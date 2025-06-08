package com.example.recipecalendar.ui.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipecalendar.R
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = viewModel(factory = CalendarViewModel.factory),
    onRecipeClick: (Int) -> Unit,
    onBackClick: () -> Unit,

) {
    val monthYear by viewModel.currentMonthYear.collectAsState()
    val yearMonth by viewModel.currentYearMonth.collectAsState()

    val selectedDate by viewModel.selectedDate.collectAsState()

    val allRecipes by viewModel.allRecipes.collectAsState()


    var showDialog by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calendar") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(stringResource(R.string.select_a_recipe)) },
                    text = {
                        LazyColumn {
                            items(allRecipes, key = { it.id }) { recipe ->
                                Text(
                                    text = recipe.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.planRecipeForDate(recipe.id, selectedDate)
                                            showDialog = false
                                        }
                                        .padding(8.dp)
                                )
                            }

                        }
                    },
                    confirmButton = {},
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text(stringResource(R.string.cancel))
                        }
                    }
                )
            }

        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { viewModel.goToPreviousMonth() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.previous_month))
                }
                Text(
                    text = monthYear,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                IconButton(onClick = { viewModel.goToNextMonth() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = stringResource(R.string.next_month))
                }
            }

            Spacer(Modifier.height(8.dp))

            DaysOfWeekHeader()

            Spacer(Modifier.height(4.dp))

            CalendarGrid(
                yearMonth = yearMonth,
                selectedDate = selectedDate,
                onDateSelected = viewModel::selectDate
            )

            Spacer(Modifier.height(16.dp))

            Text(stringResource(R.string.planned_recipes_for, selectedDate), style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(8.dp))

            PlannedRecipeList(onRecipeClick = onRecipeClick)

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { showDialog = true },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(R.string.add_recipe_to, selectedDate))
            }
        }
    }
}

@Composable
fun DaysOfWeekHeader() {
    val days = listOf(stringResource(R.string.mon),
        stringResource(R.string.tue), stringResource(R.string.wed),
        stringResource(R.string.thu), stringResource(R.string.fri),
        stringResource(R.string.sat), stringResource(R.string.sun)
    )
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        for (day in days) {
            Text(day, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarGrid(
    yearMonth: YearMonth,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val firstOfMonth = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = (firstOfMonth.dayOfWeek.value + 6) % 7  // Monday = 0

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.height(300.dp)
    ) {
        items(firstDayOfWeek) {
            Box(modifier = Modifier.size(40.dp))
        }
        items(daysInMonth) { index ->
            val day = index + 1
            val dateStr = yearMonth.atDay(day).format(DateTimeFormatter.ISO_DATE)
            val isSelected = dateStr == selectedDate
            Card(
                modifier = Modifier
                    .size(40.dp)
                    .padding(2.dp)
                    .clickable { onDateSelected(dateStr) },
                colors = if (isSelected) CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                else CardDefaults.cardColors()
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = day.toString(),
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlannedRecipeList(
    onRecipeClick: (Int) -> Unit,
) {
    val viewModel: CalendarViewModel = viewModel(factory = CalendarViewModel.factory)

    val plannedRecipeWithNames by viewModel.plannedRecipesWithNames.collectAsState()

    if (plannedRecipeWithNames.isEmpty()) {
        Text(stringResource(R.string.no_recipes_planned_for_this_day))
    } else {
        Column {
            plannedRecipeWithNames.forEach { planned ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = planned.name,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onRecipeClick(planned.id) }
                            .padding(end = 8.dp)
                    )
                    IconButton(onClick = { viewModel.unplanRecipeById(planned.plannedId) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.remove_planned_recipe)
                        )
                    }
                }
            }


        }
    }
}
