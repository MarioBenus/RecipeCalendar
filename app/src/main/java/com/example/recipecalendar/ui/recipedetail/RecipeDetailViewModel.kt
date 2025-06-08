package com.example.recipecalendar.ui.recipedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.recipecalendar.RecipeCalendarApp
import com.example.recipecalendar.data.Recipe
import com.example.recipecalendar.data.RecipesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecipeDetailViewModel(
    private val recipeId: Int,
    private val repository: RecipesRepository
) : ViewModel() {

    val recipe: StateFlow<Recipe?> = repository.getRecipeStream(recipeId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.deleteRecipe(recipe)
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RecipeCalendarApp)
                val repository = application.container.recipesRepository
                val savedStateHandle = createSavedStateHandle()
                val recipeId = savedStateHandle.get<Int>("recipeId") ?: error("Missing recipeId")

                RecipeDetailViewModel(recipeId, repository)
            }
        }
    }
}
