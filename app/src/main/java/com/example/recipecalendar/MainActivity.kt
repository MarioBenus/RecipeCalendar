package com.example.recipecalendar

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipecalendar.ui.addrecipe.AddRecipeScreen
import com.example.recipecalendar.ui.theme.RecipeCalendarTheme

import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.recipecalendar.ui.recipelist.RecipeListScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            RecipeCalendarTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "recipe_list") {
                    composable("recipe_list") {
                        RecipeListScreen(onAddRecipeClick = { navController.navigate("add_recipe") })
                    }

                    composable("add_recipe") {
                        AddRecipeScreen(
                            onBackClick = { navController.popBackStack() },
                            onRecipeSaved = { navController.popBackStack() }
                        )
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