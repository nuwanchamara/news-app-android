package com.test.newsapp.services

import com.test.newsapp.models.User

class UserService {
    companion object {
        var instance = UserService()

    }

    var currentUser: User? = null
    var localStorageService: LocalStorageService = LocalStorageService.instance

    fun login(username: String, password: String): Boolean {
        localStorageService.getAllUsers()?.let {
            if (it.containsKey(username)) {
                return it.get(username)?.password == password
            }
        }
        return false
    }

    fun register(user: User) {

        localStorageService.getAllUsers()?.let {
            if (it.containsKey(user.email)) {
                return
            }
        }



    }


}