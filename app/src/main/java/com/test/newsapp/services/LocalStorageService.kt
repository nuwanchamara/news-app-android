package com.test.newsapp.services

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.newsapp.BuildConfig
import com.test.newsapp.NewsApp
import com.test.newsapp.models.User

class LocalStorageService {

    companion object {
        val instance = LocalStorageService()
        val PACKAGE = BuildConfig.APPLICATION_ID
    }


    fun saveUser(user: User) {
        val sharedPreferences = NewsApp.appContext
            .getSharedPreferences(
                PACKAGE,
                Context.MODE_PRIVATE
            )
        val er = sharedPreferences.edit()
        val users: HashMap<String, User>? = getAllUsers() ?: hashMapOf()

        users?.put(user.email, user)
        val json = Gson().toJson(users)
        er.putString("allUsers", json)
        er.commit()
    }

    fun getAllUsers(): HashMap<String, User>? {
        val sharedPreferences = NewsApp.appContext.getSharedPreferences(
            PACKAGE, Context.MODE_PRIVATE
        )
        if (!sharedPreferences.contains("allUsers")) {
            return null
        }
        val json: String? = sharedPreferences.getString("allUsers", null);
        if (json == null || json == "null") return null
        val type = object : TypeToken<HashMap<String, User>>() {}.type
        val users: HashMap<String, User> = Gson().fromJson(json, type)
        return users
    }


}