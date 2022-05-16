package com.test.newsapp.network

import com.google.gson.Gson
import com.google.gson.JsonElement


/**
 * Created by Nuwan.
 */

class NetException(
    override var message: String? = null,
    var type: String? = null,
    var code: Int
) : Exception() {

    var isMaintain = false
    var tryAgain: (() -> Unit)? = null
    companion object {

        fun init(response: String?, code: Int): NetException {
            return if (code == 503) {
                try {
                    val g = Gson()
                    val js = g.fromJson(response, JsonElement::class.java)
                    val e = g.fromJson(response, NetException::class.java)
                    e.isMaintain = true
                    e
                } catch (e: Exception) {
                    NetException(response, "BODY CANT PARSE", code)
                }

            } else {
                try {
                    val g = Gson()
                    val exp = g.fromJson(response, NetException::class.java)
                    exp.code = code
                    if (exp.message == null) exp.message = response
                    exp
                } catch (e: Exception) {
                    NetException(response, "BODY CANT PARSE", code)
                }

            }

        }
    }
}
