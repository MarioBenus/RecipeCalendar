package com.example.recipecalendar.ui.addrecipe

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipecalendar.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    viewModel: AddRecipeViewModel = viewModel(factory = AddRecipeViewModel.factory),
    onRecipeSaved: () -> Unit,
    onBackClick: () -> Unit
) {
    val name = viewModel.name
    val ingredients = viewModel.ingredients
    val description = viewModel.description
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TopAppBar(title = { Text(stringResource(R.string.add_recipe)) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                }
            }
        )

        OutlinedTextField(
            value = name,
            onValueChange = viewModel::onNameChange,
            label = { Text(stringResource(R.string.recipe_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = ingredients,
            onValueChange = viewModel::onIngredientsChange,
            label = { Text(stringResource(R.string.ingredients)) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 15
        )

        OutlinedTextField(
            value = description,
            onValueChange = viewModel::onDescriptionChange,
            label = { Text(stringResource(R.string.description)) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 15
        )

        Button(
            onClick = {
                viewModel.saveRecipe {
                    Toast.makeText(
                        context,
                        context.getString(R.string.recipe_saved),
                        Toast.LENGTH_SHORT
                    ).show()
                    onRecipeSaved()
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(R.string.save))
        }
    }
}
