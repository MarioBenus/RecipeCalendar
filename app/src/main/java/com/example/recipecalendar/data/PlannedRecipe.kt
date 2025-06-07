package com.example.recipecalendar.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "planned_recipes",
    foreignKeys = [
        ForeignKey(
            entity = Recipe::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class PlannedRecipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val recipeId: Int,
    val plannedDate: String,
)
