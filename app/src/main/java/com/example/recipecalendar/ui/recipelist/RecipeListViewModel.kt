package com.example.recipecalendar.ui.recipelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.recipecalendar.RecipeCalendarApp
import com.example.recipecalendar.data.Recipe
import com.example.recipecalendar.data.RecipesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class RecipeListViewModel(private val repository: RecipesRepository) : ViewModel() {
    val allRecipes: StateFlow<List<Recipe>> = repository
        .getAllRecipesStream()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as RecipeCalendarApp)
                RecipeListViewModel(application.container.recipesRepository)
            }
        }
    }
}
