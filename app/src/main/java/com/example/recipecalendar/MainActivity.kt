package com.example.recipecalendar

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import com.example.recipecalendar.ui.addrecipe.AddRecipeScreen
import com.example.recipecalendar.ui.theme.RecipeCalendarTheme

import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.recipecalendar.ui.calendar.CalendarScreen
import com.example.recipecalendar.ui.editrecipe.EditRecipeScreen
import com.example.recipecalendar.ui.recipedetail.RecipeDetailScreen
import com.example.recipecalendar.ui.recipelist.RecipeListScreen


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            RecipeCalendarTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "recipe_list") {
                    composable("recipe_list") {
                        RecipeListScreen(
                            onAddRecipeClick = { navController.navigate("add_recipe") },
                            onRecipeClick = { recipe ->
                                navController.navigate("recipe_detail/${recipe.id}")
                            },
                            onCalendarClick = { navController.navigate("calendar") }
                        )
                    }

                    composable("add_recipe") {
                        AddRecipeScreen(
                            onBackClick = { navController.popBackStack() },
                            onRecipeSaved = { navController.popBackStack() }
                        )
                    }

                    composable("recipe_detail/{recipeId}",
                        arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
                    ) {
                        RecipeDetailScreen(
                            onBackClick = { navController.popBackStack() },
                            onEditClick = { recipe ->
                                navController.navigate("edit_recipe/${recipe.id}")
                            },
                        )
                    }

                    composable("calendar") {
                        CalendarScreen(
                            onBackClick = { navController.popBackStack() },
                            onRecipeClick = { recipeId ->
                                navController.navigate("recipe_detail/${recipeId}")
                            },
                        )
                    }

                    composable(
                        route = "edit_recipe/{recipeId}",
                        arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val recipeId = backStackEntry.arguments?.getInt("recipeId")
                        if (recipeId != null) {
                            EditRecipeScreen(
                                recipeId = recipeId,
                                onRecipeUpdated = { navController.popBackStack() }
                            )
                        }
                    }


                }

            }

        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RecipeCalendarTheme {
        Greeting("Android")
    }
}