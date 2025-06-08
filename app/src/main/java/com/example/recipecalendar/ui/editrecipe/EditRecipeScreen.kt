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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipecalendar.R

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

        var nameError by remember { mutableStateOf(false) }

        Column(Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
        ) {
            Text("Edit Recipe", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = it.isBlank()
                                },
                isError = nameError,
                label = { Text(stringResource(R.string.name)) }
            )
            if (nameError) {
                Text(
                    text = stringResource(R.string.name_cannot_be_empty),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                label = { Text(stringResource(R.string.ingredients)) }
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.description)) }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (name.isBlank()) {
                        nameError = true
                    } else {
                        viewModel.updateRecipe(name, description, ingredients)
                        onRecipeUpdated()
                    }
                }
            ) {
                Text(stringResource(R.string.update_recipe))
            }
        }
    } else {
        Text(stringResource(R.string.loading_recipe))
    }
}
