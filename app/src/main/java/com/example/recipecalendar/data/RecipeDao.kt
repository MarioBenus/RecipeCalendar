package com.example.recipecalendar.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlannedRecipe(planedRecipe: PlannedRecipe)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Delete
    suspend fun deletePlannedRecipe(planedRecipe: PlannedRecipe)

    @Query("DELETE FROM planned_recipes WHERE id = :id")
    suspend fun deletePlannedRecipeById(id: Int)

    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM planned_recipes WHERE plannedDate = :date")
    fun getPlannedRecipesForDate(date: String): Flow<List<PlannedRecipe>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipe(id: Int): Flow<Recipe?>
}