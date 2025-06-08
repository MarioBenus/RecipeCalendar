package com.example.recipecalendar

import android.app.Application
import com.example.recipecalendar.data.AppContainer
import com.example.recipecalendar.data.AppDataContainer

class RecipeCalendarApp : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}

