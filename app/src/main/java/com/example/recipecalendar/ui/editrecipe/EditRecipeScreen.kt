package com.example.recipecalendar.ui.editrecipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun EditRecipeScreen(
    recipeId: Int,
    viewModel: EditRecipeViewModel = viewModel(factory = EditRecipeViewModel.factory(recipeId)),
    onRecipeUpdated: () -> Unit
) {
    val recipe by viewModel.recipe.collectAsState(initial = null)

    if (recipe != null) {
        var name by remember { mutableStateOf(recipe!!.name) }
        var description by remember { mutableStateOf(recipe!!.description) }
        var ingredients by remember { mutableStateOf(recipe!!.ingredients) }

        Column(Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
        ) {
            Text("Edit Recipe", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                label = { Text("Ingredients") }
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.updateRecipe(name, description, ingredients)
                    onRecipeUpdated()
                }
            ) {
                Text("Update Recipe")
            }
        }
    } else {
        Text("Loading recipe...")
    }
}
