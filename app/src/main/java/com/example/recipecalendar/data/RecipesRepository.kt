package com.example.recipecalendar.data

import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    fun getAllRecipesStream(): Flow<List<Recipe>>

    fun getRecipeStream(id: Int): Flow<Recipe?>

    fun getPlannedRecipesForDateStream(date: String): Flow<List<PlannedRecipe>>

    suspend fun insertRecipe(recipe: Recipe)

    suspend fun deleteRecipe(recipe: Recipe)

    suspend fun updateRecipe(recipe: Recipe)

    suspend fun planRecipe(plannedRecipe: PlannedRecipe)

    suspend fun unplanRecipe(plannedRecipe: PlannedRecipe)

    suspend fun unplanRecipeById(plannedRecipeId: Int)
}