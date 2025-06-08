package com.example.recipecalendar.data

import kotlinx.coroutines.flow.Flow

class OfflineRecipesRepository(private val recipeDao: RecipeDao) : RecipesRepository {
    override fun getAllRecipesStream(): Flow<List<Recipe>> = recipeDao.getAllRecipes()

    override fun getRecipeStream(id: Int): Flow<Recipe?> = recipeDao.getRecipe(id)

    override fun getPlannedRecipesForDateStream(date: String): Flow<List<PlannedRecipe>> = recipeDao.getPlannedRecipesForDate(date)

    override suspend fun insertRecipe(recipe: Recipe) = recipeDao.insertRecipe(recipe)

    override suspend fun deleteRecipe(recipe: Recipe) = recipeDao.deleteRecipe(recipe)

    override suspend fun updateRecipe(recipe: Recipe) = recipeDao.updateRecipe(recipe)

    override suspend fun planRecipe(plannedRecipe: PlannedRecipe) = recipeDao.insertPlannedRecipe(plannedRecipe)

    override suspend fun unplanRecipe(plannedRecipe: PlannedRecipe) = recipeDao.deletePlannedRecipe(plannedRecipe)

    override suspend fun unplanRecipeById(plannedRecipeId: Int) = recipeDao.deletePlannedRecipeById(plannedRecipeId)

}