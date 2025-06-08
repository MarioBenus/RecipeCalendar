package com.example.recipecalendar.ui.addrecipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.recipecalendar.RecipeCalendarApp
import com.example.recipecalendar.data.Recipe
import com.example.recipecalendar.data.RecipesRepository
import kotlinx.coroutines.launch

class AddRecipeViewModel(private val repository: RecipesRepository) : ViewModel() {

    var name by mutableStateOf("")
        private set

    var ingredients by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    fun onNameChange(newName: String) {
        name = newName
    }

    fun onIngredientsChange(newIngredients: String) {
        ingredients = newIngredients
    }

    fun onDescriptionChange(newDescription: String) {
        description = newDescription
    }


    fun saveRecipe(onSaved: () -> Unit) {
        viewModelScope.launch {
            val recipe = Recipe(
                name = name,  ingredients = ingredients, description = description,
            )
            repository.insertRecipe(recipe)
            onSaved()
        }
    }

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as RecipeCalendarApp)
                AddRecipeViewModel(application.container.recipesRepository)
            }
        }
    }
}
