package com.test.newsapp.network

class Service {
    interface Success<T> {
        fun success(result: T)
    }

    interface Error {
        fun error(exception: NetException?)
    }
}