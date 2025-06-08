package com.example.recipecalendar.ui.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.recipecalendar.RecipeCalendarApp
import com.example.recipecalendar.data.PlannedRecipe
import com.example.recipecalendar.data.Recipe
import com.example.recipecalendar.data.RecipesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
class CalendarViewModel(
    private val repository: RecipesRepository
): ViewModel() {

    val allRecipes: StateFlow<List<Recipe>> = repository
        .getAllRecipesStream()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentYearMonth = MutableStateFlow(YearMonth.now())
    val currentYearMonth: StateFlow<YearMonth> = _currentYearMonth

    val currentMonthYear: StateFlow<String> = _currentYearMonth.map { yearMonth ->
        "${yearMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${yearMonth.year}"
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "")

    val selectedDate = MutableStateFlow(currentDateString())
    private val plannedRecipesForSelectedDate = MutableStateFlow<List<PlannedRecipe>>(emptyList())

    init {
        viewModelScope.launch {
            selectedDate
                .flatMapLatest { dateStr ->
                    repository.getPlannedRecipesForDateStream(dateStr)
                }
                .collectLatest { planned ->
                    plannedRecipesForSelectedDate.value = planned
                }
        }
    }

    fun selectDate(date: String) {
        selectedDate.value = date
    }

    fun goToPreviousMonth() {
        _currentYearMonth.value = _currentYearMonth.value.minusMonths(1)
    }

    fun goToNextMonth() {
        _currentYearMonth.value = _currentYearMonth.value.plusMonths(1)
    }

    private fun currentDateString(): String {
        return LocalDate.now().toString() // Format: "2025-06-08"
    }

    fun planRecipeForDate(recipeId: Int, date: String) {
        viewModelScope.launch {
            repository.planRecipe(PlannedRecipe(recipeId = recipeId, plannedDate = date))
        }
    }

    fun unplanRecipeById(plannedRecipeId: Int) {
        viewModelScope.launch {
            repository.unplanRecipeById(plannedRecipeId)
        }
    }


    data class PlannedRecipeWithName(
        val id: Int,
        val name: String,
        val date: String,
        val plannedId: Int
    )

    val plannedRecipesWithNames = selectedDate
        .flatMapLatest { date ->
            combine(
                repository.getPlannedRecipesForDateStream(date),
                repository.getAllRecipesStream()
            ) { plannedList, recipeList ->
                plannedList.mapNotNull { planned ->
                    val recipe = recipeList.find { it.id == planned.recipeId }
                    recipe?.let {
                        PlannedRecipeWithName(
                            id = it.id,
                            name = it.name,
                            date = planned.plannedDate,
                            plannedId = planned.id
                        )
                    }
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())



    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RecipeCalendarApp
                CalendarViewModel(application.container.recipesRepository)
            }
        }
    }
}
