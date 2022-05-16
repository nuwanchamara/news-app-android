package com.test.newsapp.services

import android.util.Log
import com.test.newsapp.models.User

class UserService {
    companion object {
        var instance = UserService()

    }
    private val logTag="UserService"
    var currentUser: User? = null
    var localStorageService: LocalStorageService = LocalStorageService.instance

    fun login(username: String, password: String): Boolean {
        localStorageService.getAllUsers()?.let {
            val hasAccount = it.containsKey(username)
            if (hasAccount) {
                if (it.get(username)?.password == password) {
                    currentUser = it.get(username)
                }
                return it.get(username)?.password == password
            }
        }
        return false
    }

    fun register(user: User): Boolean {

        localStorageService.getAllUsers()?.let {
            if (it.containsKey(user.email)) {
                return false
            }
        }
        localStorageService.saveUser(user)
        Log.d(logTag,"Registration success")
        return true

    }


}