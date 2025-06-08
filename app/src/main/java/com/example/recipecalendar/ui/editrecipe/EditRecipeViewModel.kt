package com.example.recipecalendar.ui.editrecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.recipecalendar.RecipeCalendarApp
import com.example.recipecalendar.data.Recipe
import com.example.recipecalendar.data.RecipesRepository
import com.example.recipecalendar.ui.recipedetail.RecipeDetailViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditRecipeViewModel(
    private val recipeId: Int,
    private val repository: RecipesRepository
) : ViewModel() {

    val recipe: StateFlow<Recipe?> = repository.getRecipeStream(recipeId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun updateRecipe(name: String, description: String, ingredients: String) {
        recipe.value?.let {
            val updated = it.copy(name = name, description = description, ingredients = ingredients)
            viewModelScope.launch {
                repository.updateRecipe(updated)
            }
        }
    }

    companion object {
        fun factory(recipeId: Int): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RecipeCalendarApp)
                    val repository = application.container.recipesRepository
                    val savedStateHandle = createSavedStateHandle()
                    val recipeId = savedStateHandle.get<Int>("recipeId") ?: error("Missing recipeId")

                    EditRecipeViewModel(recipeId, repository)
                }
            }
    }
}
